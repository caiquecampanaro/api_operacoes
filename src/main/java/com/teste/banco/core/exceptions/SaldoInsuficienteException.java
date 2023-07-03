package com.teste.banco.core.exceptions;


@SuppressWarnings("serial")
public class SaldoInsuficienteException extends RuntimeException {
	
 public	SaldoInsuficienteException (String errorMenssage) {
	 super(errorMenssage);
 }
}
