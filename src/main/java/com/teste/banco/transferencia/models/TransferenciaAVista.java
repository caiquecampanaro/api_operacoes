package com.teste.banco.transferencia.models;

import java.math.BigDecimal;

import com.teste.banco.conta.models.Conta;
import com.teste.banco.core.exceptions.SaldoInsuficienteException;
import com.teste.banco.transferencia.interfaces.ITransferencia;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("A_VISTA")
@NoArgsConstructor
public class TransferenciaAVista extends Transferencia implements ITransferencia {

	public TransferenciaAVista(Long id, Conta origem, Conta destino, BigDecimal valor) {
		super(null, origem, destino, valor);
		super.operacao = this;
	}

//	@Override
//	public void validar() throws RuntimeException {
//
//		boolean ehSaldoInsuficiente = getOrigem().getSaldo().compareTo(getValor()) < 0;
//		if (ehSaldoInsuficiente) {
//			throw new SaldoInsuficienteException("Saldo insuficiente!");
//		}
//	}

	@Override
	public Transferencia executar() {
		super.validarTransferencia();
		return super.realizarTransferencia();
	}
	
	

}
