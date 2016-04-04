package br.com.sc.exception;

/**
 * Classe que representa um erro ao tentar persistir uma prova com um nome já
 * existente.
 * 
 */
public class NomeProvaDuplicadoException extends Exception {

	private static final long serialVersionUID = -511386132068846129L;

	/**
	 * Construtor.
	 */
	public NomeProvaDuplicadoException() {
	}

	/**
	 * Construtor.
	 * 
	 * @param mensagem
	 *            - Descrição do erro.
	 */
	public NomeProvaDuplicadoException(String mensagem) {
		super(mensagem);

	}

	/**
	 * Construtor.
	 * 
	 * @param causa
	 *            - Causa do erro.
	 */
	public NomeProvaDuplicadoException(Throwable causa) {
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
	public NomeProvaDuplicadoException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
}