package com.portfolio.SistemaNotas.service;

import com.portfolio.SistemaNotas.model.entity.Payments;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentsSevice {

    Payments registraUmPagamentoPeloCpfDoCliente(String cpf_client, Long id_notes, BigDecimal amountPaid);

    List<Payments> buscaPagamentosPeloCpfDaNota(String cpf_client);
}
