package com.teste.banco.transferencia;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teste.banco.transferencia.models.Transferencia;

public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {

}
