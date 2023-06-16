package org.exercicio.banco.template.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import org.exercicio.banco.template.model.enumerator.TipoTransacao;

public class ContaCorrente implements Serializable, IConta {

	private static final long serialVersionUID = 1L;
	private Integer numeroConta;
	private BigDecimal saldo;
	private LocalDateTime dataAbertura;
	private boolean status;
	private List<RegistroTransacao> transacoes;

	public ContaCorrente() {
		this.numeroConta = new Random().nextInt(999999999);
		this.saldo = BigDecimal.ZERO;
		saldo.setScale(4, RoundingMode.HALF_UP);
		this.dataAbertura = LocalDateTime.now();
		this.status = true;
		transacoes = new ArrayList<>();
	}

	public Integer getNumeroConta() {
		return numeroConta;
	}

	public void setNumeroConta(Integer numeroConta) {
		this.numeroConta = numeroConta;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public LocalDateTime getDataAbertura() {
		return dataAbertura;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public List <RegistroTransacao> getTransacoes() {
		return transacoes;
	}

	@Override
	public int hashCode() {
		return Objects.hash(numeroConta);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContaCorrente other = (ContaCorrente) obj;
		return Objects.equals(numeroConta, other.numeroConta);
	}

	@Override
	public String toString() {
		return "Conta Corrente - [ Numero da conta: " + numeroConta + " | Saldo: " + saldo + " | Data de Abertura: " + dataAbertura
				+ " | Status: " + status + "]";
	}

	public void depositar(BigDecimal quantia) {
		if (status) {
			if (quantia.compareTo(BigDecimal.ZERO) > 0) {
				this.saldo = this.saldo.add(quantia);
				transacoes.add(new RegistroTransacao(quantia, TipoTransacao.CREDITO, LocalDateTime.now()));
				System.out.println("Deposito realizado com sucesso.");
			} else {
				System.err.println("Valor invalido para deposito.");
			}
		} else {
			System.err.println("Operação não permitida. Conta desativada.");
		}
	}

	public void sacar(BigDecimal quantia) {
		if (status) {
			if (quantia.compareTo(BigDecimal.ZERO) > 0) {
				if (this.saldo.compareTo(quantia) > 0) {
					this.saldo = this.saldo.subtract(quantia);
					transacoes.add(new RegistroTransacao(quantia, TipoTransacao.DEBITO, LocalDateTime.now()));
					System.out.println("Saque realizado com sucesso!");
				} else {
					System.err.println("Saldo insuficiente.");
				}
			} else {
				System.err.println("Valor invalido para saque.");
			}
		} else {
			System.err.println("Operação não permitida. Conta desativada.");
		}
	}

	public void transferir(IConta c, BigDecimal quantia) {
	    BigDecimal taxa = BigDecimal.ZERO;
	    BigDecimal tarifa = new BigDecimal("0.06");

	    if (status && c.isStatus()) {
	        if (quantia.compareTo(BigDecimal.ZERO) < 0) {
	            System.err.println("Valor inválido para transferência.");
	        } else {
	            if (c instanceof ContaPoupanca) {
	                taxa = quantia.multiply(tarifa);
	            }
	        }

	        BigDecimal valorTotalTransferencia = quantia.add(taxa);

	        if (valorTotalTransferencia.compareTo(saldo) <= 0) {
	            setSaldo(saldo.subtract(valorTotalTransferencia));
	            c.setSaldo(c.getSaldo().add(quantia));
	            c.getTransacoes().add(new RegistroTransacao(quantia, TipoTransacao.TRANSACAO_CREDITO, LocalDateTime.now()));
	            transacoes.add(new RegistroTransacao(quantia, TipoTransacao.TRANSACAO_DEBITO, LocalDateTime.now()));
	            System.out.println("Transferência realizada com sucesso.");
	        } else {
	            System.err.println("Saldo insuficiente para realizar a transferência.");
	        }
	    } else {
	        System.err.println("Operação não pode ser realizada entre contas desativadas.");
	    }
	}

	public void imprimirExtratoConta(int month, int year) {
		for (RegistroTransacao transacao : transacoes) {
			if (transacao.getData().getMonthValue() == month && transacao.getData().getYear() == year) {
				System.out.println("Extrato - [ Data e hora: " + transacao.getData() + " | Tipo: " + transacao.getTipo() + " | Quantia: " + transacao.getValor() + " ]"); 
			}
	     }
	}
}