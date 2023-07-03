package com.teste.banco.transferencia;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teste.banco.conta.ContaService;
import com.teste.banco.conta.models.Conta;
import com.teste.banco.transferencia.interfaces.ITransferencia;
import com.teste.banco.transferencia.models.Transferencia;
import com.teste.banco.transferencia.models.TransferenciaAVista;
import com.teste.banco.transferencia.models.TransferenciaParcelada;
import com.teste.banco.transferencia.models.TransferenciaRevertida;

import jakarta.transaction.Transactional;

@Service
public class TransferenciaService {

	@Autowired
	private ContaService contaService;

	@Autowired
	private TransferenciaRepository repository;

	@Transactional
	public void aVista(Long idContaOrigem, Long idContaDestino, BigDecimal valorTransferencia) {
		Optional<Conta> origemOpt = contaService.buscar(idContaOrigem);
		Optional<Conta> destinoOpt = contaService.buscar(idContaDestino);

		TransferenciaAVista aVista = new TransferenciaAVista(null, origemOpt.orElse(null), destinoOpt.orElse(null),
				valorTransferencia);
		
		executarOperacao(aVista);
	}

	@Transactional
	public Transferencia reverter(Long id) {
		Optional<Transferencia> transferenciaOpt = repository.findById(id);
		if (transferenciaOpt.isEmpty()) {
			throw new NoSuchElementException("Transferência não encontrada!"); // Implementar esse TESTE!
		}

		TransferenciaRevertida reverter = new TransferenciaRevertida(transferenciaOpt.get());

		if (transferenciaOpt.get() instanceof TransferenciaParcelada) {
			repository.deleteById(reverter.getId());
			return null;
		}

		return executarOperacao(reverter);
	}
	
	
	@Transactional
	public void parcelar ( Long idOrigem, Long idDestino, BigDecimal valor, int quantidadeParcelas) {
		Optional<Conta> origemOpt = contaService.buscar(idOrigem);
		Optional<Conta> destinoOpt = contaService.buscar(idDestino);
		
		TransferenciaParcelada aParcelar = new TransferenciaParcelada(null, origemOpt.orElse(null), destinoOpt.orElse(null), valor, quantidadeParcelas);
		
		executarOperacao(aParcelar);
	}
	
	private Transferencia executarOperacao(ITransferencia operacao) {
		return repository.save(operacao.executar());
	}
}
