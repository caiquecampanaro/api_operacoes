package com.teste.banco.transferencia.models;

import com.teste.banco.conta.models.Conta;
import com.teste.banco.transferencia.interfaces.ITransferencia;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;


@Entity
@DiscriminatorValue("REVERTIDA")
@NoArgsConstructor
public class TransferenciaRevertida extends Transferencia implements ITransferencia {

	public TransferenciaRevertida(Transferencia aReverter) {
		super(aReverter.getId(), aReverter.getOrigem(), aReverter.getDestino(), aReverter.getValor());
		super.operacao = this;
	}

	@Override
	public Transferencia executar() {
		super.validarTransferencia();
		Conta origemAux = super.getOrigem();
		super.setOrigem(super.getDestino());
		super.setDestino(origemAux);
		return super.realizarTransferencia();
	}

}
