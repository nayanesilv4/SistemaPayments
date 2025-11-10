package com.portfolio.SistemaNotas.service.Impl;

import com.portfolio.SistemaNotas.model.entity.Clients;
import com.portfolio.SistemaNotas.model.entity.Notes;
import com.portfolio.SistemaNotas.model.entity.Payments;
import com.portfolio.SistemaNotas.repository.ClientsRepository;
import com.portfolio.SistemaNotas.repository.NotesRepository;
import com.portfolio.SistemaNotas.repository.PaymentsRepository;
import com.portfolio.SistemaNotas.service.PaymentsSevice;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentsServiceImpl implements PaymentsSevice {

    @Autowired
    private ClientsRepository clientsRepository;

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private PaymentsRepository paymentsRepository;

    @Override
    public Payments registraUmPagamentoPeloCpfDoCliente(String cpf_client, Long id_notes, BigDecimal amountPaid) {

        Clients clients = clientsRepository.findByCpf(cpf_client)
                .orElseThrow(() -> new IllegalArgumentException("Cliente com CPF " + cpf_client + " não encontrado."));

        Notes notes = clients.getNotes().stream()
                .filter(n -> n.getId().equals(id_notes))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Nota com número " + id_notes + " não encontrada para o cliente."));

        Payments newPayment = new Payments();
        newPayment.setAmountPaid(amountPaid);
        newPayment.setPaymentDate(LocalDateTime.now());
        newPayment.setNote(notes);
        paymentsRepository.save(newPayment);

        notes.getPayments().add(newPayment.getNote());

        BigDecimal valorPagoTotal = notes.getPayments().stream()
                .map(Payments::getAmountPaid)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal valorRestante = notes.getValorTotal().subtract(valorPagoTotal);

        if (valorRestante.compareTo(BigDecimal.ZERO) <= 0) {
            note.setStatus(StatusNota.TOTAL);
            valorRestante = BigDecimal.ZERO; // Garante que o valor restante não seja negativo
        } else {
            note.setStatus(StatusNota.PARCIAL);
        }

        // 8. Salva a nota atualizada
        noteRepository.save(note);

        // 9. Retorna o valor restante
        return valorRestante;
    }
    }

    @Override
    public List<Payments> buscaPagamentosPeloCpfDaNota(String cpf_client) {
        return paymentsRepository.buscaPorCpfDoCliente(cpf_client);
    }


}
