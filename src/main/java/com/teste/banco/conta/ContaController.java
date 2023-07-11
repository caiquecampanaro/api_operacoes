package com.teste.banco.conta;

import java.math.BigDecimal;
import java.util.List;

import org.apache.catalina.startup.ClassLoaderFactory.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teste.banco.conta.dtos.ContaDTO;
import com.teste.banco.conta.dtos.CriarContaDTO;
import com.teste.banco.conta.models.Conta;

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
	
//	@PostMapping
//	public ResponseEntity<Conta> criarConta (@RequestBody List<CriarContaDTO> criarContaDTO) {
//		
//		
//		return ResponseEntity<Conta>;
//		 
//	}
}
