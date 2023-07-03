package com.teste.banco.transferencia.dtos;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class TransferenciaDTO {
	
	private Long idOrigem;
	
	private Long idDestino;
	
	private BigDecimal valor;
	
}
