package com.portfolio.SistemaNotas.service.Impl;

import com.portfolio.SistemaNotas.model.entity.Clients;
import com.portfolio.SistemaNotas.repository.ClientsRepository;
import com.portfolio.SistemaNotas.repository.NotesRepository;
import com.portfolio.SistemaNotas.service.ClientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientsServiceImpl implements ClientsService {

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private ClientsRepository clientsRepository;

    @Autowired
    private ClientsService clientsService;


    @Override
    public List<Clients> exibeTodosOsClientesEmOrdemAlfabetica() {
        return clientsRepository.findAll(Sort.by(Sort.Direction.ASC, "fullName"));
    }

    @Override
    public Clients buscaClientePorCpf(String cpf_client) {
        return clientsRepository.findByCpf(cpf_client)
                .orElseThrow(() -> new RuntimeException("Cliente" + cpf_client + "não encontrado"));
    }

    @Override
    public Clients salvaUmNovoCliente(Clients clients) {
        if(clientsRepository.findByCpf(clients.getCpf()).isPresent()){
            throw new RuntimeException("O cpf: " + clients +"já existe no banco de dados");
        } else{
            return clientsRepository.save(clients);
        }
    }

    @Override
    public void deletaUmClientePorCpf(String cpf_client) {
        if(!clientsRepository.existsByCpf(cpf_client)){
            throw new RuntimeException("Cliente não encontrado");
        }
        clientsRepository.deleteByCpf(cpf_client);
    }
}
