package br.com.sc.exception;

/**
 * Classe que representa um erro na extra��o de quest�es.
 * 
 */
public class ExtracaoQuestoesException extends RuntimeException {

	private static final long serialVersionUID = -511386132068846129L;

	/**
	 * Construtor.
	 */
	public ExtracaoQuestoesException() {
	}

	/**
	 * Construtor.
	 * 
	 * @param mensagem
	 *            - Descri��o do erro.
	 */
	public ExtracaoQuestoesException(String mensagem) {
		super(mensagem);

	}

	/**
	 * Construtor.
	 * 
	 * @param causa
	 *            - Causa do erro.
	 */
	public ExtracaoQuestoesException(Throwable causa) {
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
	public ExtracaoQuestoesException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
}