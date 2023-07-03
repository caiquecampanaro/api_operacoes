package com.teste.banco.conta;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teste.banco.conta.models.ContaEspecial;
import com.teste.banco.conta.models.ContaPadrao;

@RestController
@RequestMapping("conta")
public class ContaController {
	
	@Autowired
	ContaService contaService;
	
	@Autowired
	ContaRepository contaRepository;
	
	@GetMapping("{id}/saldo")
	public BigDecimal consultarSaldo (@PathVariable Long id) {
		return contaService.buscarSaldo(id); 
	}
	@PostMapping
	public void save () {
		 contaRepository.save(new ContaPadrao(null, new BigDecimal(100) ,"1234", "1"));
		 contaRepository.save(new ContaPadrao(null, new BigDecimal(200) ,"12345", "2"));
		 contaRepository.save(new ContaEspecial(null, new BigDecimal(300) ,"123456", "3"));
	}
}
