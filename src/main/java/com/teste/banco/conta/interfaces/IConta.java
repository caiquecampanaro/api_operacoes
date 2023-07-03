package com.teste.banco.conta.interfaces;

import java.math.BigDecimal;

public interface IConta {
	void validarDebitar(BigDecimal valor) throws RuntimeException;
}
