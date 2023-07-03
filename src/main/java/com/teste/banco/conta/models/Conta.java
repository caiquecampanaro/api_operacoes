package com.teste.banco.conta.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.teste.banco.conta.interfaces.IConta;
import com.teste.banco.core.exceptions.SaldoInsuficienteException;
import com.teste.banco.core.models.Pessoa;
import com.teste.banco.transferencia.models.Transferencia;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CONTA")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
@AllArgsConstructor
public abstract class Conta implements Cloneable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conta_sequence")
	@SequenceGenerator(allocationSize = 1, name = "conta_sequence")
	@Getter
	@Setter
	private Long id;

	@OneToMany(mappedBy = "origem")
	@JsonManagedReference
	@Getter
	@Setter
	private List<Transferencia> transferenciasOrigem = new ArrayList<>();

	@OneToMany(mappedBy = "destino")
	@JsonManagedReference
	@Getter
	@Setter
	private List<Transferencia> transferenciasDestino = new ArrayList<>();;

	@OneToOne(mappedBy = "conta", fetch = FetchType.EAGER)
	@JsonManagedReference
	@Getter
	@Setter
	private Pessoa pessoa;

	@Getter
	@Setter
	private BigDecimal saldo;

	@Getter
	@Setter
	private String numero;

	@Getter
	@Setter
	private String digito;
	
	@Transient
	@Getter
	@Setter
	private IConta tipoConta;

	public Conta(Long id, BigDecimal saldo, String numero, String digito) {

		this.id = id;
		this.saldo = saldo;
		this.numero = numero;
		this.digito = digito;
	}
	
	public void creditar(BigDecimal valor) {
		this.saldo = this.saldo.add(valor);
	}
	
	public void debitar(BigDecimal valor) {
		BigDecimal novoSaldo = this.saldo.subtract(valor);
		this.saldo = novoSaldo;
		if(Objects.nonNull(this.tipoConta)) {
			this.tipoConta.validarDebitar(valor);
		}
	}

	@Override
	public Conta clone() throws CloneNotSupportedException {
		return (Conta) super.clone();
	}
}
