package br.com.sc.dominio;

import java.util.ArrayList;
import java.util.List;

import br.com.sc.exception.EntidadeInconsistenteException;

public class QuestaoComposta extends Questao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	List<Questao> questoes = new ArrayList<Questao>();
	
	/**
	 * Construtor.
	 * 
	 * @param conteudo
	 *            - Conte�do da quest�o..
	 * @param numero
	 *            - N�mero da quest�o na prova.
	 * @param prova
	 *            - Prova que cont�m a quest�o.
	 * @throws EntidadeInconsistenteException
	 *             - Caso os dados informados n�o sejam consistentes.
	 */
	public QuestaoComposta(String enunciado, int numero, Prova prova)
			throws EntidadeInconsistenteException {
		super(enunciado, numero, prova);
	}
	
	public void estruturar() {
		
		logger.log("---IN�CIO DA QUEST�O---: ");
		logger.log("---N�mero: " + this.getNumero());
		logger.log("---Enunciado: " + conteudo);
		
		for (Questao questao : questoes) {
			questao.estruturar();
		}
		
		logger.log("---FIM DA QUEST�O---: ");
	}
	
	public void setSubquestoes(List<Questao> questoes) {
		this.questoes= questoes;
	}
	
	public List<Questao> setSubquestoes() {
		return this.questoes;
	}
	
	public void addQuestao(Questao questao) {
		// TODO Auto-generated method stub
		this.questoes.add(questao);
	}

	public void removeQuestao(Questao questao) {
		// TODO Auto-generated method stub
		this.questoes.remove(questao);
	}

}
