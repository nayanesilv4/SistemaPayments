package com.portfolio.SistemaNotas.service.Impl;

import com.portfolio.SistemaNotas.model.entity.Clients;
import com.portfolio.SistemaNotas.model.entity.Notes;
import com.portfolio.SistemaNotas.repository.ClientsRepository;
import com.portfolio.SistemaNotas.repository.NotesRepository;
import com.portfolio.SistemaNotas.service.ClientsService;
import com.portfolio.SistemaNotas.service.NotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotesServiceImpl implements NotesService {

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private ClientsRepository clientsRepository;

    @Autowired
    private ClientsService clientsService;

    @Override
    public Notes criarUmaNovaNota(String cpf_client, Notes notes){
        Clients clients = clientsService.buscaClientePorCpf(cpf_client);
        if(clients == null){
            throw new RuntimeException("Cliente não encontrado");
        }
        notes.setClient(clients);
        return notesRepository.save(notes);
    }

    @Override
    public List<Notes> buscaTodasAsNotasReferentesAoCpfDoCliente(String cpf_client) {
        List<Notes> notes = notesRepository.buscaPorCpfDoCliente(cpf_client);

        if(notes.isEmpty()){
            throw new RuntimeException("Notas refernetes ao cliente" + cpf_client + "não encontradas");
        }

        return notes;
    }

    @Override
    public Notes salvaUmaNovaNota(Notes notes) {
        String cpf_client = notes.getClient().getCpf();

        return clientsRepository.findById(cpf_client).map(
                clienteExiste -> {
                    notes.setClient(clienteExiste);
                    return notesRepository.save(notes);
                }).orElseThrow(() -> {
                    return new RuntimeException("Cliente não encontrado");
        });
    }

    @Override
    public void deletaUmaNotaPorCpf(String cpf_client, int id_note) {
        int deletedCount = notesRepository.deletaPeloCpfDoClienteEPeloIdDaNota(cpf_client, id_note);

        if (deletedCount == 0) {
            throw new RuntimeException("Nota não encontrada");
        }
    }

}
