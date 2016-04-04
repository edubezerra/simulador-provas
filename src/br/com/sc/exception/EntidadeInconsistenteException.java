package br.com.sc.exception;

import br.com.sc.dominio.Notificacao;

/**
 * Classe que representa um erro na verifica��o de consist�ncia de uma entidade.
 * 
 */
public class EntidadeInconsistenteException extends RuntimeException {

	private static final long serialVersionUID = -511386132068846129L;

	/**
	 * A notifica��o da exce��o.
	 */
	private Notificacao notificacao;

	/**
	 * Construtor.
	 * 
	 * @param notificacao
	 *            - a notifica��o da exce��o.
	 */
	public EntidadeInconsistenteException(Notificacao notificacao) {
		this.notificacao = notificacao;
	}

	public EntidadeInconsistenteException(String msg,
			NomeProvaDuplicadoException e) {
		super(msg, e);
	}

	/**
	 * Obt�m a notifica��o da exce��o.
	 * 
	 * @return a notifica��o da exce��o.
	 */
	public Notificacao getNotificacao() {
		return notificacao;
	}

}