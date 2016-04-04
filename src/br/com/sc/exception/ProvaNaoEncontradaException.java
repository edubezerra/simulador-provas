package br.com.sc.exception;

/**
 * Classe que representa um erro quando uma prova não é localizada.
 * 
 */
public class ProvaNaoEncontradaException extends RuntimeException {

	private static final long serialVersionUID = -511386132068846129L;

	/**
	 * Construtor.
	 */
	public ProvaNaoEncontradaException() {
	}

	/**
	 * Construtor.
	 * 
	 * @param mensagem
	 *            - Descrição do erro.
	 */
	public ProvaNaoEncontradaException(String mensagem) {
		super(mensagem);

	}

	/**
	 * Construtor.
	 * 
	 * @param causa
	 *            - Causa do erro.
	 */
	public ProvaNaoEncontradaException(Throwable causa) {
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
	public ProvaNaoEncontradaException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
}