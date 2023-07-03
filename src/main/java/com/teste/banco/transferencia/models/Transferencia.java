package com.teste.banco.transferencia.models;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.teste.banco.conta.models.Conta;
import com.teste.banco.core.exceptions.SaldoInsuficienteException;
import com.teste.banco.transferencia.interfaces.ITransferencia;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "TRANSFERENCIA")
@NoArgsConstructor
@AllArgsConstructor
public abstract class Transferencia {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transferencia_sequence")
	@SequenceGenerator(allocationSize = 1, name = "transferencia_sequence")
	@Getter
	@Setter
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_origem", nullable = false)
	@JsonBackReference
	@Getter
	@Setter
	private Conta origem;

	@ManyToOne
	@JoinColumn(name = "id_destino", nullable = false)
	@JsonBackReference
	@Getter
	@Setter
	private Conta destino;

	@Getter
	@Setter
	private BigDecimal valor;

	@Transient
	protected ITransferencia operacao;

	public Transferencia(Long id, Conta origem, Conta destino, BigDecimal valor) {
		super();
		this.id = id;
		this.origem = origem;
		this.destino = destino;
		this.valor = valor;
	}

	protected void validarTransferencia() {

		boolean ehTransferenciaInvalida = this.valor.compareTo(BigDecimal.ZERO) <= 0;
		if (ehTransferenciaInvalida) {
			throw new IllegalArgumentException("O valor da transferencia não deve possuir valor zerado ou negativo!");
		}

		boolean ehOrigemInexistente = Objects.isNull(origem);
		if (ehOrigemInexistente) {
			throw new NoSuchElementException("A conta de origem não foi encontrada!");
		}

		if (Objects.nonNull(this.operacao))
			this.operacao.validar();
		
		boolean ehDestinoInexistente = Objects.isNull(destino);
		if (ehDestinoInexistente) {
			throw new NoSuchElementException("A conta de destino não foi encontrada!");
		}
	}

	protected Transferencia realizarTransferencia() {
		this.origem.debitar(this.valor);
		this.destino.creditar(this.valor);
		return this;
	}

}
