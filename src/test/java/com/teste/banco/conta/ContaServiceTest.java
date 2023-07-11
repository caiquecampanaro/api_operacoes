package com.teste.banco.conta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class ContaServiceTest {

	@Test
	void test() {

		List<String> numeroExistentes = new ArrayList<>(Arrays.asList("3", "1", "2"));
		int min = 100000;
		int max = 999999;
		int numero;
		boolean ehNumeroRepetido; 
		 
		do {
			numero = (int) Math.floor(Math.random() * (max - min + 1) + min);
			//Substituir por uma consulta no banco
			ehNumeroRepetido = numeroExistentes.contains(String.valueOf(numero));
//			ehNumeroRepetido = contaRepository.findAll().contains(String.valueOf(numero));
		} while (ehNumeroRepetido);
		
		assertEquals(1, String.valueOf(numero).length());
		assertTrue(!numeroExistentes.contains(String.valueOf(numero)));

	}
}
