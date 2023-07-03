package com.teste.banco.transferencia.exceptions;

@SuppressWarnings("serial")
public class TransferenciaReverterException extends RuntimeException {
	public  TransferenciaReverterException (String errorMensage ) {
		super(errorMensage);
	}
}
