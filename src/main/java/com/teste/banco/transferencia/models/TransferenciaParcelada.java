package com.teste.banco.transferencia.models;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.teste.banco.conta.models.Conta;
import com.teste.banco.transferencia.exceptions.TransferenciaQtdParcelasException;
import com.teste.banco.transferencia.exceptions.TransferenciaValorParcelaException;
import com.teste.banco.transferencia.interfaces.ITransferencia;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@DiscriminatorValue("PARCELADA")
@NoArgsConstructor
public class TransferenciaParcelada extends Transferencia implements ITransferencia {
	
	@Getter
	@Setter
	private int quantidadeParcelas;

	@Getter
	@Setter
	private BigDecimal valorParcela;

	@Getter
	@Setter
	private BigDecimal ultimaParcela;

	public TransferenciaParcelada(Long id, Conta origem, Conta destino, BigDecimal valor, int quantidadeParcelas) {
		super(id, origem, destino, valor);
		this.quantidadeParcelas = quantidadeParcelas;
		super.operacao = this;
	}

	@Override
	public void validar() throws RuntimeException {

		boolean ehValorFuturoInvalido = this.getValor().compareTo(BigDecimal.ONE) < 0;
		if (ehValorFuturoInvalido) {
			throw new TransferenciaValorParcelaException("O valor da transferencia futura não pode ser menor que 1!!!");

		}

		boolean ehQuantidadeParcelasMenorIgualZero = this.getQuantidadeParcelas() <= 0;
		if (ehQuantidadeParcelasMenorIgualZero) {
			throw new TransferenciaQtdParcelasException("Quantidade de parcelas inválida!!!");
		}

	}

	@Override
	public Transferencia executar() {
		super.validarTransferencia();
		this.setValorParcela(
				this.getValor().divide(new BigDecimal(this.getQuantidadeParcelas()), 4, RoundingMode.HALF_EVEN));
		this.setUltimaParcela(this.getValorParcela());
		BigDecimal valorTotalAPagar = this.getValorParcela().multiply(new BigDecimal(this.getQuantidadeParcelas()));
		int quantidadeParcelasMenosUltima = this.getQuantidadeParcelas() - 1;
		BigDecimal valorDasPacerlasMenosUltima = this.getValorParcela()
				.multiply(new BigDecimal(quantidadeParcelasMenosUltima));
		boolean ehDizima = this.getValor().compareTo(valorTotalAPagar) != 0;
		if (ehDizima) {
			this.setUltimaParcela(this.getValor().subtract(valorDasPacerlasMenosUltima));

		}

		this.setValor(valorDasPacerlasMenosUltima.add(this.getUltimaParcela()));
		return this;
	}

}
