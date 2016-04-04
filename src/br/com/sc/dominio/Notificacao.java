package br.com.sc.dominio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa uma notificação.
 */
public class Notificacao implements Serializable {

	private static final long serialVersionUID = 7125819861952135085L;

	/**
	 * Descrições da notificação.
	 */
	private List<String> descricoes = new ArrayList<String>();

	/**
	 * Contrutor padrão.
	 */
	public Notificacao() {
	}

	/**
	 * Contrutor que recebe uma mensagem como parâmetro.
	 * 
	 * @param mensagem
	 *            - Mensagem a ser armazenada na notificação.
	 */
	public Notificacao(String mensagem) {
		adicionar(mensagem);
	}

	/**
	 * Recupera uma lista com as descrições da notificação. <br/>
	 * 
	 * (Verbo mantido em inglês por questões de compatibilidade com JSTL)
	 */
	public List<String> getDescricoes() {
		return new ArrayList<String>(descricoes);
	}

	/**
	 * Adiciona uma descrição à notificação.
	 * 
	 * @param texto
	 *            - Descrição a ser adicionada.
	 */
	public void adicionar(String texto) {
		this.descricoes.add(texto);
	}

	/**
	 * Verifica se a notificação possui mensagens.
	 * 
	 * @return se a notificação possui mensagens.
	 */
	public boolean possuiMensagens() {
		return !this.descricoes.isEmpty();
	}

}
