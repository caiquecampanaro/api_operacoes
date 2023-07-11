package com.teste.banco.conta.dtos;

import com.teste.banco.conta.models.Conta;
import com.teste.banco.conta.models.ContaPadrao;
import com.teste.banco.core.models.Pessoa;

public class CriarContaDTO {
	
	public String nome; 
	
	public String sobreNome;
	
	public Conta convert() {
		Conta conta = new ContaPadrao();
		Pessoa pessoa = new Pessoa();
		pessoa.setNome(nome);
		pessoa.setSobrenome(sobreNome);
		conta.setPessoa(pessoa);
		
		return conta;
	}


}
