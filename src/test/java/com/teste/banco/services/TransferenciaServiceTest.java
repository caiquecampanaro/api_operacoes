package com.teste.banco.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import com.teste.banco.conta.ContaService;
import com.teste.banco.conta.models.Conta;
import com.teste.banco.conta.models.ContaEspecial;
import com.teste.banco.conta.models.ContaPadrao;
import com.teste.banco.core.exceptions.SaldoInsuficienteException;
import com.teste.banco.transferencia.TransferenciaRepository;
import com.teste.banco.transferencia.TransferenciaService;
import com.teste.banco.transferencia.exceptions.TransferenciaQtdParcelasException;
import com.teste.banco.transferencia.exceptions.TransferenciaValorParcelaException;
import com.teste.banco.transferencia.models.Transferencia;
import com.teste.banco.transferencia.models.TransferenciaAVista;
import com.teste.banco.transferencia.models.TransferenciaParcelada;

@DataJpaTest
class TransferenciaServiceTest {

	@TestConfiguration
	static class ContaServiceTestConfiguration {
		@Bean
		public TransferenciaService service() {
			return new TransferenciaService();
		}
	}

	@Autowired
	TransferenciaService service;

	@MockBean
	ContaService contaService;

	@MockBean
	TransferenciaRepository repository;

	private ContaPadrao origem;
	private ContaPadrao destino;

	@BeforeEach
	public void setUp() {

		ContaPadrao origem = new ContaPadrao();
		origem.setId(1L);
		ContaPadrao destino = new ContaPadrao();
		destino.setId(2L);

		this.origem = origem;
		this.destino = destino;
	}

	@Test
	void deve_realizarTransferencia_quando_AVista() {

		this.origem.setSaldo(new BigDecimal(1000));
		this.destino.setSaldo(new BigDecimal(1000));
		BigDecimal valorTransferencia = new BigDecimal(500);

		Mockito.when(contaService.buscar(this.origem.getId())).thenReturn(Optional.of(origem));
		Mockito.when(contaService.buscar(this.destino.getId())).thenReturn(Optional.of(destino));

		service.aVista(1L, 2L, valorTransferencia);

		assertEquals(new BigDecimal(500), origem.getSaldo());
		assertEquals(new BigDecimal(1500), destino.getSaldo());
		verify(repository, times(1)).save(Mockito.any(Transferencia.class));
	}

	@Test
	void nao_deve_realizarTransferencia_quando_origemInexistente() {

		this.origem.setSaldo(new BigDecimal(1000));
		this.destino.setSaldo(new BigDecimal(1000));
		BigDecimal valorTransferencia = new BigDecimal(500);

		Mockito.when(contaService.buscar(this.origem.getId())).thenReturn(Optional.empty());
		Mockito.when(contaService.buscar(this.destino.getId())).thenReturn(Optional.of(destino));

		NoSuchElementException origemException = assertThrows(NoSuchElementException.class,
				() -> service.aVista(this.origem.getId(), this.destino.getId(), valorTransferencia));
		assertEquals(origemException.getMessage(), "A conta de origem não foi encontrada!");

		verify(contaService, times(1)).buscar(this.origem.getId());
		verify(repository, never()).save(Mockito.any(Transferencia.class));

	}

	@Test
	void nao_deve_realizarTransferencia_quando_destinoInexistente() {

		this.origem.setSaldo(new BigDecimal(1000));
		this.destino.setSaldo(new BigDecimal(1000));
		BigDecimal valorTransferencia = new BigDecimal(500);

		Mockito.when(contaService.buscar(this.origem.getId())).thenReturn(Optional.of(origem));
		Mockito.when(contaService.buscar(this.destino.getId())).thenReturn(Optional.empty());

		NoSuchElementException destinoException = assertThrows(NoSuchElementException.class,
				() -> service.aVista(this.origem.getId(), this.destino.getId(), valorTransferencia));
		assertEquals(destinoException.getMessage(), "A conta de destino não foi encontrada!");

		verify(contaService, times(1)).buscar(this.origem.getId());
		verify(contaService, times(1)).buscar(this.destino.getId());
		verify(repository, never()).save(Mockito.any(Transferencia.class));
	}

	@Test
	void nao_deve_realizarTransferencia_quando_transferenciaInapropriada() {

		BigDecimal valorTransferencia = new BigDecimal(-0.1); // qualquer valor menor ou igual a 0.

		IllegalArgumentException valorTransferenciaException = assertThrows(IllegalArgumentException.class,
				() -> service.aVista(this.origem.getId(), this.destino.getId(), valorTransferencia));
		assertEquals(valorTransferenciaException.getMessage(),
				"O valor da transferencia não deve possuir valor zerado ou negativo!");

		verify(repository, never()).save(Mockito.any(Transferencia.class));
	}

	@Test
	void nao_deve_realizarTransferenciaAvista_quando_saldoInsuficiente() {
		this.origem.setSaldo(new BigDecimal(2));
		this.destino.setSaldo(new BigDecimal(1000));
		BigDecimal valorTransferencia = new BigDecimal(10);

		Mockito.when(contaService.buscar(this.origem.getId())).thenReturn(Optional.of(origem));
		Mockito.when(contaService.buscar(this.destino.getId())).thenReturn(Optional.of(destino));

		assertThrows(SaldoInsuficienteException.class,
				() -> service.aVista(this.origem.getId(), this.destino.getId(), valorTransferencia));

		verify(contaService, times(1)).buscar(this.origem.getId());
		verify(repository, never()).save(Mockito.any(Transferencia.class));
	}

	@Test
	void deve_executarOperacaoDeReverter_quando_parcelada() {
		Transferencia transferenciaMock = new TransferenciaParcelada(1L, this.origem, this.destino, new BigDecimal(10), 5);

		Mockito.when(repository.findById(transferenciaMock.getId())).thenReturn(Optional.of(transferenciaMock));

		service.reverter(transferenciaMock.getId());

		verify(contaService, never()).buscar(this.origem.getId());
		verify(contaService, never()).buscar(this.destino.getId());
		verify(repository, never()).save(Mockito.any(Transferencia.class));

		verify(repository, times(1)).findById(transferenciaMock.getId());
		verify(repository, times(1)).deleteById(transferenciaMock.getId());
	}

	@Test
	void deve_executarOperacaoDeReverter_quando_aVista() {
		this.origem.setSaldo(new BigDecimal(10));
		this.destino.setSaldo(new BigDecimal(20));
		BigDecimal valorTransferencia = new BigDecimal(10);

		Transferencia transferenciaMock = new TransferenciaAVista(1L, this.origem, this.destino, valorTransferencia);

		Mockito.when(contaService.buscar(this.origem.getId())).thenReturn(Optional.of(origem));
		Mockito.when(contaService.buscar(this.destino.getId())).thenReturn(Optional.of(destino));

		Mockito.when(repository.findById(transferenciaMock.getId())).thenReturn(Optional.of(transferenciaMock));

		service.reverter(transferenciaMock.getId());

		verify(repository, times(1)).findById(transferenciaMock.getId());
		verify(repository, times(1)).save(Mockito.any(Transferencia.class));

		assertEquals(origem.getSaldo(), new BigDecimal(20));
		assertEquals(destino.getSaldo(), new BigDecimal(10));

	}

	@Test
	void naoDeve_reverterTransferenciaAVista_quando_nao_existir_saldo() {
		this.origem.setSaldo(new BigDecimal(10));
		this.destino.setSaldo(new BigDecimal(20));
		BigDecimal valorTransferencia = new BigDecimal(100);

		Transferencia transferenciaMock = new TransferenciaAVista(1L, this.origem, this.destino, valorTransferencia);

		Mockito.when(contaService.buscar(this.origem.getId())).thenReturn(Optional.of(origem));
		Mockito.when(contaService.buscar(this.destino.getId())).thenReturn(Optional.of(destino));

		Mockito.when(repository.findById(transferenciaMock.getId())).thenReturn(Optional.of(transferenciaMock));

		assertThrows(SaldoInsuficienteException.class, () -> service.reverter(transferenciaMock.getId()));

		verify(repository, times(1)).findById(transferenciaMock.getId());
		verify(repository, never()).save(Mockito.any(Transferencia.class));

	}

	@Test
	void deve_realizarTransferencia_quando_parcelada() {

		BigDecimal valor = new BigDecimal(1000);
		int quantidadeParcelas = 101;
		BigDecimal valorParcela = BigDecimal.ZERO;
		BigDecimal valorTotalDasParcelas = BigDecimal.ZERO;
		BigDecimal quantidadeDasParcelasMenosUltima = new BigDecimal(quantidadeParcelas).subtract(BigDecimal.ONE);

		valorParcela = valor.divide(new BigDecimal(quantidadeParcelas), 2, RoundingMode.HALF_EVEN);
		BigDecimal ultimaParcela = valorParcela;
		valorTotalDasParcelas = valorParcela.multiply(new BigDecimal(quantidadeParcelas));
		boolean ehUltimaParcelaDiferente = !valor.equals(valorTotalDasParcelas);
		if (ehUltimaParcelaDiferente) {

			ultimaParcela = valor.subtract(valorParcela.multiply(quantidadeDasParcelasMenosUltima));

		}
		valorTotalDasParcelas = ultimaParcela.add(quantidadeDasParcelasMenosUltima.multiply(valorParcela));

		BigDecimal valorEsperado = new BigDecimal(1000).setScale(2, RoundingMode.HALF_EVEN);

		assertEquals(valorTotalDasParcelas, valorEsperado);

	}

	@Test
	void naoDeve_executarTransferenciaParcelada_quando_QuantidadeParcelasMenorOuIgualZero() {

		BigDecimal valorTransferencia = new BigDecimal(1000);
		int quantidadeParcelas = -100;

		Mockito.when(contaService.buscar(this.origem.getId())).thenReturn(Optional.of(origem));
		Mockito.when(contaService.buscar(this.destino.getId())).thenReturn(Optional.of(destino));

		TransferenciaQtdParcelasException qtdParcelasException = assertThrows(TransferenciaQtdParcelasException.class, () -> service
				.parcelar(this.origem.getId(), this.destino.getId(), valorTransferencia, quantidadeParcelas));

		assertEquals(qtdParcelasException.getMessage(), "Quantidade de parcelas inválida!!!");

	}

	@Test
	void nao_deve_RelizarTransferenciaParcelada_quando_ValorMenorUm() {

		BigDecimal valorTransferencia = new BigDecimal(0.50);
		this.origem.setSaldo(new BigDecimal(100));
		int quantidadeParcelas = 3;

		Mockito.when(contaService.buscar(this.origem.getId())).thenReturn(Optional.of(origem));
		Mockito.when(contaService.buscar(this.destino.getId())).thenReturn(Optional.of(destino));

		TransferenciaValorParcelaException valorFuturoInvalidoException = assertThrows(TransferenciaValorParcelaException.class,
				() -> service.parcelar(this.origem.getId(), this.destino.getId(), valorTransferencia,
						quantidadeParcelas));

		assertEquals(valorFuturoInvalidoException.getMessage(),
				"O valor da transferencia futura não pode ser menor que 1!!!");
	}

	@Test
	void nao_deve_RealizarTransferenciaFutura_quando_OrigemInexistente() {

		int quantidadeParcelas = 5;
		BigDecimal valorTransferencia = new BigDecimal(1000);

		Mockito.when(repository.findById(this.origem.getId())).thenReturn(Optional.empty());

		NoSuchElementException origemFuturaInvalidaException = assertThrows(NoSuchElementException.class, () -> service
				.parcelar(this.origem.getId(), this.destino.getId(), valorTransferencia, quantidadeParcelas));

		verify(contaService, times(1)).buscar(this.origem.getId());

		assertEquals(origemFuturaInvalidaException.getMessage(), "A conta de origem não foi encontrada!");

	}

	@Test
	void nao_deve_RealizarTransferenciaFutura_quando_DestinoInexistente() {
		this.origem.setSaldo(new BigDecimal(1000));
		int quantidadeParcelas = 5;
		BigDecimal valorTransferencia = new BigDecimal(1000);

		Mockito.when(contaService.buscar(this.origem.getId())).thenReturn(Optional.of(origem));
		Mockito.when(contaService.buscar(this.destino.getId())).thenReturn(Optional.empty());

		NoSuchElementException destinoFuturaInvalidaException = assertThrows(NoSuchElementException.class, () -> service
				.parcelar(this.origem.getId(), this.destino.getId(), valorTransferencia, quantidadeParcelas));

		verify(contaService, times(1)).buscar(this.origem.getId());
		verify(contaService, times(1)).buscar(this.destino.getId());
		verify(repository, never()).save(Mockito.any(Transferencia.class));

		assertEquals(destinoFuturaInvalidaException.getMessage(), "A conta de destino não foi encontrada!");

	}

	@Test
	void deve_realizarTransferenciaFutura_quando_ValorParcelaIgualDizima() {
		BigDecimal valorTransferencia = new BigDecimal(1000);
		int quantidadeParcelas = 9;
		origem.setSaldo(new BigDecimal(1000));
		destino.setSaldo(BigDecimal.ZERO);

		Mockito.when(contaService.buscar(this.origem.getId())).thenReturn(Optional.of(origem));

		Mockito.when(contaService.buscar(this.destino.getId())).thenReturn(Optional.of(destino));

		service.parcelar(this.origem.getId(), this.destino.getId(), valorTransferencia, quantidadeParcelas);

		verify(contaService, times(1)).buscar(this.origem.getId());
		verify(contaService, times(1)).buscar(this.destino.getId());

		verify(repository, times(1)).save(Mockito.any(Transferencia.class));

	}
	
	@Test
	void deve_deixarSaldoNegativo_quando_valorMaiorQueSaldo() {
		BigDecimal meuSaldo = new BigDecimal(100);
		BigDecimal limiteCredito = new BigDecimal(100);
		BigDecimal valorTrasferencia = new BigDecimal(150);
		
		ContaEspecial conta = new ContaEspecial();
		
		conta.setSaldo(meuSaldo);
		conta.setLimiteCredito(limiteCredito);
		
		conta.debitar(valorTrasferencia);
		assertEquals(new BigDecimal(-50), conta.getSaldo());
		//debita até o limite do credito
	}
	

}
