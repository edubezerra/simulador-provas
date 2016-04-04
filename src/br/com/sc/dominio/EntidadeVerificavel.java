package br.com.sc.dominio;

import br.com.sc.exception.EntidadeInconsistenteException;

/**
 * Interface que define o comportamento de uma entidade capaz de verificar seu
 * estado.
 * 
 */
public interface EntidadeVerificavel {

	/**
	 * Valida o estado da entidade.
	 * 
	 * @throws EntidadeInconsistenteException
	 *             - Caso a entidade se encontre em um estado inconsistente.
	 */
	void validarEstado() throws EntidadeInconsistenteException;

}
