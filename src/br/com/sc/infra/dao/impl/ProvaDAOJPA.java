package br.com.sc.infra.dao.impl;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.sc.dominio.BancaExaminadora;
import br.com.sc.dominio.Prova;
import br.com.sc.dominio.Questao;
import br.com.sc.infra.dao.ProvaDAO;

public class ProvaDAOJPA implements ProvaDAO {

	GenericDAOJPA<Prova> daoGenerico = new GenericDAOJPA<Prova>();

	@Override
	public void incluir(Prova prova) {
		daoGenerico.incluir(prova);
	}

	@Override
	public List<Prova> obterTodas() {
		return daoGenerico.obterTodos(Prova.class);
	}

	// @Override
	// public byte[] obterArquivo(Prova prova) throws
	// ProvaNaoEncontradaException {
	// // TODO Auto-generated method stub
	// return null;
	// }

	@Override
	public void remover(Prova prova) {
		daoGenerico.excluir(Prova.class, prova.getId());
	}

	@Override
	public Collection<String> obterBancasExaminadoras() {
		try {
			EntityManager em = daoGenerico.getEntityManager();
			TypedQuery<String> q = em.createQuery(
					"select b.nome from BancaExaminadora b", String.class);
			return q.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public Questao obterQuestaoPorId(Long id) {
		try {
			EntityManager em = daoGenerico.getEntityManager();
			TypedQuery<Questao> q = em.createQuery(
					"from Questao q where q.id = :id", Questao.class);
			q.setParameter("id", id);
			return q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public Prova obterProvaPorId(Long idProva) {
		return daoGenerico.obterPorId(Prova.class, idProva);
	}
}
