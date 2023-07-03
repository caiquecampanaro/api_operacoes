package com.teste.banco.conta;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teste.banco.conta.models.Conta;

public interface ContaRepository extends JpaRepository<Conta, Long> {

}
