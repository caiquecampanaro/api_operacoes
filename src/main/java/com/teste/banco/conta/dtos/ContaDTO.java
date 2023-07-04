package com.teste.banco.conta.dtos;

import java.math.BigDecimal;

import com.teste.banco.core.models.Pessoa;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter  @Setter
public class ContaDTO {
	
	private Pessoa pessoa; 
	
	private BigDecimal saldo;
	
	private String numero;
	
	private String digito;
	
	

}
