package com.portfolio.SistemaNotas.repository;

import com.portfolio.SistemaNotas.model.entity.Notes;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotesRepository extends JpaRepository<Notes, Integer> {

    Optional<Notes> buscaPorCpfDoCliente(String cpf_client);

    @Modifying
    @Transactional
    int deletaPeloCpfDoClienteEPeloIdDaNota(String cpf, int id_note);
}
