package br.com.sc.exception;

/**
 * Classe que representa um erro camada de indexação de questões.
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
	 *            - Descrição do erro.
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
	 *            - Descrição do erro.
	 * @param causa
	 *            - Causa do erro.
	 */
	public IndexacaoException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
}