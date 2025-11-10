# Sistema de Pagamento de Notas a Prazo — Backend (Java + Spring Boot)

> **Status:** Em desenvolvimento — documentação inicial. Algumas partes poderão ser editadas posteriormente.

---

## Visão geral

Este repositório contém o backend de um sistema de **pagamento de notas a prazo** voltado para farmácias. O objetivo é automatizar o processo de venda, armazenamento de notas e controle de pagamentos (totais ou parciais), gerando **PDFs de comprovantes** de compra e pagamento, e permitindo que apenas o **dono da farmácia** visualize o histórico completo das transações.

O sistema foi projetado para ser **simples**, **seguro** e **escalável**, utilizando **Java + Spring Boot**, seguindo princípios de **Clean Code**, **Clean Architecture** e **Design Patterns**.

---

## Objetivos principais

* CRUD completo de clientes e notas.
* Registro de compras (notas em status `process`).
* Atualização de pagamentos (parciais ou totais) com geração automática de PDFs.
* Histórico de transações acessível apenas pelo dono da farmácia.
* Documentação via **Swagger**.
* Boas práticas de código e escalabilidade.

---

## Tecnologias

* Java 17+
* Spring Boot
* Spring Data JPA
* Hibernate
* MySQL / PostgreSQL
* Lombok (opcional)
* Swagger (OpenAPI)
* iTextPDF ou Apache PDFBox — geração de PDFs
* Flyway / Liquibase — migração de banco
* JUnit / Mockito — testes unitários

---

## Arquitetura e organização do código

Padrão de **Clean Architecture**, separando bem camadas de domínio, aplicação e infraestrutura:

```
src/main/java
 └─ com.farmacia.notas
    ├─ adapter
    │  ├─ in (controllers, DTOs de entrada)
    │  └─ out (repositories, geradores de PDF)
    ├─ application (serviços e casos de uso)
    ├─ domain (entidades, regras de negócio, exceções)
    └─ config (Swagger, banco de dados, beans)
```

* **domain** — contém entidades como `Client`, `Note`, `Payment`, e enums como `StatusNote`, `PaymentMethod`.
* **application** — serviços de negócio (ex.: `RegisterSaleService`, `ProcessPaymentService`, `GeneratePdfService`).
* **adapter.in** — Controllers REST e DTOs.
* **adapter.out** — persistência e integração com geração de PDFs.

---

## Modelos principais (Domain)

### Client

* `id` (Long)
* `fullName` (String)
* `cpf` (String, único)
* `address` (String)
* `phone` (String)

### Note (Nota de compra)

* `id` (Long)
* `client` (Client)
* `sellerName` (String)
* `productList` (List<ProductItem>)
* `purchaseDate` (LocalDateTime)
* `totalValue` (BigDecimal)
* `remainingValue` (BigDecimal)
* `status` (Enum: PROCESS / PAID)

### ProductItem

* `id` (Long)
* `productName` (String)
* `quantity` (Integer)
* `price` (BigDecimal)

### Payment

* `id` (Long)
* `noteId` (Long)
* `paymentDate` (LocalDateTime)
* `sellerName` (String)
* `method` (Enum: PIX, CREDITO, DEBITO, ESPECIE)
* `amountPaid` (BigDecimal)
* `remainingValue` (BigDecimal)
* `changeGiven` (BigDecimal)

---

## Regras de negócio

1. **Registro de compra:**

   * Quando o cliente realiza uma compra, o sistema cria uma nova nota (`Note`) com status `PROCESS`.
   * Gera automaticamente um **PDF da nota** com informações do cliente, produtos, valor total e vendedor responsável.

2. **Consulta por CPF:**

   * O vendedor pode buscar notas pelo CPF do cliente para verificar todas as notas em andamento (`PROCESS`).

3. **Pagamento (parcial, total ou excedente):**

   * O vendedor informa o valor pago e o método de pagamento.
   * Se o valor for **igual ao restante**, o status da nota muda para `PAID`.
   * Se o valor for **menor**, o status permanece `PROCESS`, e o valor restante é atualizado.
   * Se o valor for **maior** que o valor restante, o sistema **calcula o troco** e registra o campo `changeGiven` no pagamento.
   * O sistema gera um **PDF de comprovante de pagamento**, com informações de data, método, valor pago, troco (se houver) e vendedor.

4. **Atualização dinâmica:**

   * Caso o cliente faça novas compras antes de quitar o valor total, o sistema soma o novo valor à nota em `PROCESS`.

5. **Histórico:**

   * O **dono da farmácia** tem acesso completo a um histórico com todas as notas, pagamentos e status, atualizado em tempo real.

---

## Endpoints (API REST) — Proposta inicial

> Base path: `/api/v1`

### Clientes

* `GET /clients` — listar clientes
* `POST /clients` — cadastrar cliente
* `GET /clients/{cpf}` — buscar cliente por CPF
* `PUT /clients/{id}` — atualizar cliente
* `DELETE /clients/{id}` — remover cliente

### Notas

* `POST /notes` — registrar nova compra (gera PDF automático)
* `GET /notes/{cpf}` — listar notas de um cliente pelo CPF
* `PUT /notes/{id}` — atualizar nota (ex.: adicionar produtos)
* `DELETE /notes/{id}` — excluir nota (opcional)

### Pagamentos

* `POST /payments` — registrar pagamento (parcial, total ou com troco)
* `GET /payments/{cpf}` — listar pagamentos de um cliente
* `GET /history` — listar histórico completo (acesso restrito ao dono)

---

## Exemplo de payloads

**Criar nota** (`POST /notes`)

```json
{
  "clientCpf": "12345678900",
  "sellerName": "João Vendas",
  "products": [
    {"productName": "Dipirona 500mg", "quantity": 2, "price": 8.50},
    {"productName": "Paracetamol 750mg", "quantity": 1, "price": 6.00}
  ]
}
```

**Registrar pagamento** (`POST /payments`)

```json
{
  "noteId": 1,
  "sellerName": "Ana Souza",
  "method": "ESPECIE",
  "amountPaid": 25.00
}
```

Resposta:

```json
{
  "paymentId": 10,
  "noteId": 1,
  "amountPaid": 25.00,
  "remainingValue": 0.00,
  "changeGiven": 2.00,
  "message": "Pagamento concluído. Troco: R$ 2,00."
}
```

---

## Banco de dados — DDL simplificado

```sql
CREATE TABLE client (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  full_name VARCHAR(200),
  cpf VARCHAR(11) UNIQUE,
  address VARCHAR(255),
  phone VARCHAR(20)
);

CREATE TABLE note (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  client_id BIGINT NOT NULL,
  seller_name VARCHAR(200),
  total_value DECIMAL(10,2),
  remaining_value DECIMAL(10,2),
  status VARCHAR(20),
  purchase_date TIMESTAMP,
  FOREIGN KEY (client_id) REFERENCES client(id)
);

CREATE TABLE product_item (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  note_id BIGINT,
  product_name VARCHAR(200),
  quantity INT,
  price DECIMAL(10,2),
  FOREIGN KEY (note_id) REFERENCES note(id)
);

CREATE TABLE payment (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  note_id BIGINT,
  seller_name VARCHAR(200),
  method VARCHAR(20),
  amount_paid DECIMAL(10,2),
  remaining_value DECIMAL(10,2),
  change_given DECIMAL(10,2),
  payment_date TIMESTAMP,
  FOREIGN KEY (note_id) REFERENCES note(id)
);
```

---

## Validações e DTOs

* `@Valid` em todos os DTOs.
* Validação de CPF e valores positivos.
* DTOs: `ClientRequest`, `ClientResponse`, `NoteRequest`, `NoteResponse`, `PaymentRequest`, `PaymentResponse`.

---

## Exceções e tratamentos de erro

* `ClientNotFoundException`
* `NoteNotFoundException`
* `InvalidPaymentAmountException` (para valores negativos ou inválidos)
* `PdfGenerationException`

> ⚠️ Se o **valor pago for maior que o valor restante**, o sistema **não lança exceção** — ele **calcula automaticamente o troco** e o inclui no comprovante de pagamento (`changeGiven`).

Formato de erro padrão:

```json
{
  "timestamp": "2025-11-10T15:32:10Z",
  "status": 400,
  "error": "InvalidPaymentAmountException",
  "message": "O valor pago é inválido.",
  "path": "/api/v1/payments"
}
```

---

## Swagger / OpenAPI

* Acessível em `/swagger-ui.html`.
* Documentação de endpoints e modelos.
* Uso de anotações `@Operation` e `@ApiResponses`.

---

## Segurança

* Autenticação via JWT.
* Perfis de acesso:

  * `ROLE_OWNER` — acesso total e histórico.
  * `ROLE_SELLER` — registro de vendas e pagamentos.

---

## Testes

* Testes unitários de domínio e serviços.
* Testes de integração com banco em memória (H2).
* Teste de geração de PDFs.
* Teste de cálculo de troco em pagamentos.

---

## CI / CD

* GitHub Actions para build e testes.
* Dockerfile para containerização.

---

## Exemplo de docker-compose

```yaml
version: '3.8'
services:
  db:
    image: mysql:8
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: notas_prazo
    ports:
      - "3306:3306"
  app:
    image: notas-prazo-backend:latest
    depends_on:
      - db
    ports:
      - "8080:8080"
```

---

## Padrões de projeto

* **Repository Pattern** — Spring Data JPA.
* **Strategy Pattern** — para cálculo de pagamentos parciais/totais/troco.
* **Builder Pattern** — criação de PDFs e DTOs.
* **Factory Pattern** — inicialização de entidades complexas.

---

## Checklist / TODOs

* [x] Adicionar cálculo de troco e inclusão no PDF de comprovante
* [ ] Implementar geração automática de PDF (venda e pagamento)
* [ ] Criar endpoint de histórico restrito ao dono
* [ ] Implementar autenticação JWT
* [ ] Adicionar testes de integração
* [ ] Criar migrations com Flyway
* [ ] Configurar CI/CD com GitHub Actions

---

## Contribuição

1. Fork o repositório.
2. Crie uma branch: `feature/nome-da-feature`.
3. Faça commits pequenos e descritivos.
4. Abra um PR para revisão.

---

## Observação final

Esta documentação é uma **base inicial** do projeto. À medida que o sistema evolui, novas funcionalidades poderão ser adicionadas (ex.: notificações, dashboards, relatórios de inadimplência). Mantenha o README sempre atualizado conforme as mudanças de arquitetura e endpoints.

