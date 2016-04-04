package br.com.sc.exception;

/**
 * Classe que representa um erro camada de indexa��o de quest�es.
 * 
 */
public class IndexacaoException extends RuntimeException {

	private static final long serialVersionUID = -511386132068846129L;

	/**
	 * Construtor.
	 */
	public IndexacaoException() {
	}

	/**
	 * Construtor.
	 * 
	 * @param mensagem
	 *            - Descri��o do erro.
	 */
	public IndexacaoException(String mensagem) {
		super(mensagem);

	}

	/**
	 * Construtor.
	 * 
	 * @param causa
	 *            - Causa do erro.
	 */
	public IndexacaoException(Throwable causa) {
		super(causa);

	}

	/**
	 * Construtor.
	 * 
	 * @param mensagem
	 *            - Descri��o do erro.
	 * @param causa
	 *            - Causa do erro.
	 */
	public IndexacaoException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
}