package br.com.sc.infra.log;

import java.io.Serializable;

/**
 * Classe que abstrai a implementação do sistema de log.
 * 
 */
public class Logger implements Serializable {

	private static final long serialVersionUID = 871638434465177055L;
	
	/**
	 * Classe que contém o logger.
	 */
	private Class<?> classe;

	/**
	 * Contrutor.
	 * 
	 * @param classe
	 *            - Classe que contém o logger.
	 */
	public Logger(Class<?> classe) {
		this.classe = classe;
		System.out.println("Criando logger para a classe: " + this.classe);
	}

	/**
	 * Registra uma mensagem no log.
	 * 
	 * @param mensagem
	 *            - Mensagem a ser registrada.
	 */
	public void log(String mensagem) {
		System.out.println(classe.getName() + ": " + mensagem);
	}

	/**
	 * Registra um erro no log.
	 * 
	 * @param mensagem
	 *            - Erro a ser registrado.
	 */
	public void log(Throwable erro) {
		erro.printStackTrace(System.err);
	}
}
