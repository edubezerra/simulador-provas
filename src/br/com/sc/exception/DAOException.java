package br.com.sc.exception;

/**
 * Classe que representa um erro na camada de acesso a dados.
 * 
 */
public class DAOException extends RuntimeException {

	private static final long serialVersionUID = 2738639933160296138L;

	/**
	 * Construtor.
	 */
	public DAOException() {
	}

	/**
	 * Construtor.
	 * 
	 * @param mensagem
	 *            - Descrição do erro.
	 */
	public DAOException(String mensagem) {
		super(mensagem);

	}

	/**
	 * Construtor.
	 * 
	 * @param causa
	 *            - Causa do erro.
	 */
	public DAOException(Throwable causa) {
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
	public DAOException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
}
