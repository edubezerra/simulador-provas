package br.com.sc.infra.dao;

import java.util.List;

import br.com.sc.dominio.Assunto;

public interface AssuntoDAO {

	public abstract List<Assunto> obterAssuntos();

	public abstract Assunto obterAssuntoPorId(int idAssunto);

}