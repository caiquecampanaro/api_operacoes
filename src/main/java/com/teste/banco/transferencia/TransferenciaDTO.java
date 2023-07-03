package com.teste.banco.transferencia;

import java.math.BigDecimal;

import com.teste.banco.transferencia.enums.TransferenciaOperacaoEnum;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaDTO {

	private Long idContaOrigem;
	private Long idContaDestino;
	private BigDecimal valorTransferencia;
	@Enumerated(EnumType.STRING)
	private TransferenciaOperacaoEnum tipo;

}
