package br.com.sc.servico;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.sc.dominio.Prova;
import br.com.sc.dominio.Questao;
import br.com.sc.dominio.RepositorioProva;

public class ServicoProvas {

	private RepositorioProva repositorio;

	@Autowired
	public void setRepositorioProva(RepositorioProva repositorio) {
		this.repositorio = repositorio;
	}
	
	List<Prova> getProvas() {
		return repositorio.obterTodas();
	}

	public List<Questao> getQuestoes(Prova prova) {
		return prova.getQuestoes();
	}
}
