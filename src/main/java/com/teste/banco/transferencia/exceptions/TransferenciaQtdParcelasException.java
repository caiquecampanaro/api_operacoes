package com.teste.banco.transferencia.exceptions;

@SuppressWarnings("serial")
public class TransferenciaQtdParcelasException extends RuntimeException {
	public  TransferenciaQtdParcelasException (String errorMensage ) {
		super(errorMensage);
	}
}
