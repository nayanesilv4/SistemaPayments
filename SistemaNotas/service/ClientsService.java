package com.portfolio.SistemaNotas.service;

import com.portfolio.SistemaNotas.model.entity.Clients;

import java.util.List;

public interface ClientsService {

    List<Clients> exibeTodosOsClientesEmOrdemAlfabetica();

    Clients buscaClientePorCpf(String cpf);

    Clients salvaUmNovoCliente(Clients clients);

    void deletaUmClientePorCpf(String cpf);
}
