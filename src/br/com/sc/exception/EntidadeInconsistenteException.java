package br.com.sc.exception;

import br.com.sc.dominio.Notificacao;

/**
 * Classe que representa um erro na verificação de consistência de uma entidade.
 * 
 */
public class EntidadeInconsistenteException extends RuntimeException {

	private static final long serialVersionUID = -511386132068846129L;

	/**
	 * A notificação da exceção.
	 */
	private Notificacao notificacao;

	/**
	 * Construtor.
	 * 
	 * @param notificacao
	 *            - a notificação da exceção.
	 */
	public EntidadeInconsistenteException(Notificacao notificacao) {
		this.notificacao = notificacao;
	}

	public EntidadeInconsistenteException(String msg,
			NomeProvaDuplicadoException e) {
		super(msg, e);
	}

	/**
	 * Obtém a notificação da exceção.
	 * 
	 * @return a notificação da exceção.
	 */
	public Notificacao getNotificacao() {
		return notificacao;
	}

}