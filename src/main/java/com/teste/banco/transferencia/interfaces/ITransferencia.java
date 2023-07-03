package com.teste.banco.transferencia.interfaces;

import com.teste.banco.transferencia.models.Transferencia;

public interface ITransferencia {
	
	default void validar() throws RuntimeException {};
	
	Transferencia executar();

}
