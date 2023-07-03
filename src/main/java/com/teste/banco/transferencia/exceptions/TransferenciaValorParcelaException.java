package com.teste.banco.transferencia.exceptions;

@SuppressWarnings("serial")
public class TransferenciaValorParcelaException extends RuntimeException{
	public TransferenciaValorParcelaException (String errorMensage) {
		super(errorMensage);
	}

}
