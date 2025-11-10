package com.portfolio.SistemaNotas.repository;

import com.portfolio.SistemaNotas.model.entity.Clients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientsRepository extends JpaRepository<Clients, Integer> {

    Optional<Clients> findByCpf(String cpf_client);

    Optional<Clients> findById(String cpf_client);

    boolean existsByCpf(String cpf_client);

    Optional<Clients> deleteByCpf(String cpf_client);
}
