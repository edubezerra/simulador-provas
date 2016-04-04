package br.com.sc.exception;

/**
 * Classe que representa um erro na tentativa de utilizar um extrator de
 * quest�es n�o cadastrado.
 * 
 */
public class ExtratorQuestoesNaoCadastradoException extends RuntimeException {

	private static final long serialVersionUID = -511386132068846129L;

	/**
	 * Construtor.
	 */
	public ExtratorQuestoesNaoCadastradoException() {
	}

	/**
	 * Construtor.
	 * 
	 * @param mensagem
	 *            - Descri��o do erro.
	 */
	public ExtratorQuestoesNaoCadastradoException(String mensagem) {
		super(mensagem);

	}

	/**
	 * Construtor.
	 * 
	 * @param causa
	 *            - Causa do erro.
	 */
	public ExtratorQuestoesNaoCadastradoException(Throwable causa) {
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
	public ExtratorQuestoesNaoCadastradoException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
}