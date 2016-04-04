package br.com.sc.exception;

/**
 * Classe que representa um erro na extração de questões.
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
	 *            - Descrição do erro.
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
	 *            - Descrição do erro.
	 * @param causa
	 *            - Causa do erro.
	 */
	public ExtracaoQuestoesException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
}