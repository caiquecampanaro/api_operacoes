package com.teste.banco.conta.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.teste.banco.conta.interfaces.IConta;
import com.teste.banco.core.exceptions.SaldoInsuficienteException;
import com.teste.banco.core.models.Pessoa;
import com.teste.banco.transferencia.models.Transferencia;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("ESPECIAL")
public class ContaEspecial extends Conta implements IConta {

	@Getter
	@Setter
	private BigDecimal limiteCredito = new BigDecimal(100);
	
	public ContaEspecial() {
		super();
		this.setTipoConta(this);	
	}

	public ContaEspecial(Long id, BigDecimal saldo, String numero, String digito) {
		super(id, saldo, numero, digito);
		this.setTipoConta(this);
	}

	@Override
	public void validarDebitar(BigDecimal valor) throws RuntimeException {
		BigDecimal saldoMaisLimite = this.getSaldo().add(this.limiteCredito);

		boolean ehSaldoInsuficiente = saldoMaisLimite.compareTo(BigDecimal.ZERO) < 0;

		if (ehSaldoInsuficiente) {
			throw new SaldoInsuficienteException("Saldo insuficiente!");
		}
	}

}
