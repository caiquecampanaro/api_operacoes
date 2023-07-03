package com.teste.banco.transferencia.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TransferenciaParceladaDTO extends TransferenciaDTO {

	private int parcelas;

}
