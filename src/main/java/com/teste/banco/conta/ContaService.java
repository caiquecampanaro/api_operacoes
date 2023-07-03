package com.teste.banco.conta;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teste.banco.conta.models.Conta;

@Service
public class ContaService {

	@Autowired
	ContaRepository contaRepository;

	public Optional<Conta> buscar(Long id) {

		return contaRepository.findById(id);
	}

	public BigDecimal buscarSaldo(Long id) throws NoSuchElementException {
		try {
			return contaRepository.findById(id).get().getSaldo();
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException("Conta não encontrada");
		}
	}
}
