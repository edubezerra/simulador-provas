package br.com.sc.dominio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa uma notifica��o.
 */
public class Notificacao implements Serializable {

	private static final long serialVersionUID = 7125819861952135085L;

	/**
	 * Descri��es da notifica��o.
	 */
	private List<String> descricoes = new ArrayList<String>();

	/**
	 * Contrutor padr�o.
	 */
	public Notificacao() {
	}

	/**
	 * Contrutor que recebe uma mensagem como par�metro.
	 * 
	 * @param mensagem
	 *            - Mensagem a ser armazenada na notifica��o.
	 */
	public Notificacao(String mensagem) {
		adicionar(mensagem);
	}

	/**
	 * Recupera uma lista com as descri��es da notifica��o. <br/>
	 * 
	 * (Verbo mantido em ingl�s por quest�es de compatibilidade com JSTL)
	 */
	public List<String> getDescricoes() {
		return new ArrayList<String>(descricoes);
	}

	/**
	 * Adiciona uma descri��o � notifica��o.
	 * 
	 * @param texto
	 *            - Descri��o a ser adicionada.
	 */
	public void adicionar(String texto) {
		this.descricoes.add(texto);
	}

	/**
	 * Verifica se a notifica��o possui mensagens.
	 * 
	 * @return se a notifica��o possui mensagens.
	 */
	public boolean possuiMensagens() {
		return !this.descricoes.isEmpty();
	}

}
