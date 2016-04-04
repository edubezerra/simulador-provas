package br.com.sc.infra.dao.impl;

import java.util.List;

import br.com.sc.dominio.Assunto;
import br.com.sc.infra.dao.AssuntoDAO;

public class AssuntoDAOJPA implements AssuntoDAO {
	GenericDAOJPA<Assunto> daoGenerico = new GenericDAOJPA<Assunto>();

	@Override
	public List<Assunto> obterAssuntos() {
		return daoGenerico.obterTodos(Assunto.class);
	}

	@Override
	public Assunto obterAssuntoPorId(int idAssunto) {
		return daoGenerico.obterPorId(Assunto.class, idAssunto);
	}

}
