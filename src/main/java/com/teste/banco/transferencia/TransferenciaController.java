package com.teste.banco.transferencia;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teste.banco.transferencia.dtos.TransferenciaDTO;
import com.teste.banco.transferencia.dtos.TransferenciaParceladaDTO;
import com.teste.banco.transferencia.models.Transferencia;

@RestController
@RequestMapping("transferencia")
public class TransferenciaController {

	@Autowired
	TransferenciaService service;

	@PostMapping
	@RequestMapping("a_vista")
	public ResponseEntity<Transferencia> aVista(@RequestBody TransferenciaDTO tranferencia) {

		service.aVista(tranferencia.getIdOrigem(), tranferencia.getIdDestino(), tranferencia.getValor());
		return new ResponseEntity<Transferencia>(HttpStatus.CREATED);
	}

	@PostMapping
	@RequestMapping("{id}/reverter")
	public ResponseEntity<Transferencia> reverter(@PathVariable Long id) {
		HttpStatus statusResponse = Objects.isNull(service.reverter(id)) 
				? HttpStatus.OK
				: HttpStatus.CREATED;
		return new ResponseEntity<Transferencia>(statusResponse);
	}

	@PostMapping
	@RequestMapping("/parcelar")
	public ResponseEntity<Transferencia> parcelada(@RequestBody TransferenciaParceladaDTO tranferencia) {

		service.parcelar(tranferencia.getIdOrigem(), tranferencia.getIdDestino(), tranferencia.getValor(),
				tranferencia.getParcelas());
		return new ResponseEntity<Transferencia>(HttpStatus.CREATED);
	}

}
