package com.portfolio.SistemaNotas.service;

import com.portfolio.SistemaNotas.model.entity.Notes;

import java.util.List;

public interface NotesService {

    List<Notes> buscaTodasAsNotasReferentesAoCpfDoCliente(String cpf_client);

    Notes salvaUmaNovaNota(Notes notes);

    void deletaUmaNotaPorCpf(String cpf_client, int id_note);

    Notes criarUmaNovaNota(String cpf_client, Notes notes);
}
