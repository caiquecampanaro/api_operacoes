package com.teste.banco.core.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.teste.banco.conta.models.Conta;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PESSOA")
public class Pessoa {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pessoa_sequence")
	@SequenceGenerator(allocationSize = 1, name = "pessoa_sequence")
	@Getter
	@Setter
	private Long id;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "conta_id" , referencedColumnName = "id")
	@JsonBackReference
	private Conta conta;

	@Getter
	@Setter
	private String nome;

	@Getter
	@Setter
	private String sobrenome;

}
