package org.exercicio.banco.template.model;

import java.math.BigDecimal;
import java.util.List;

public interface IConta {
	public boolean isStatus();
	public Integer getNumeroConta();
	public BigDecimal getSaldo();
	public void depositar(BigDecimal valor);
	public void transferir(IConta destino, BigDecimal valor);
	public void sacar(BigDecimal quantia);
	public void setSaldo (BigDecimal saldo);
	public List <RegistroTransacao> getTransacoes();
	public void imprimirExtratoConta (int month, int year);
}