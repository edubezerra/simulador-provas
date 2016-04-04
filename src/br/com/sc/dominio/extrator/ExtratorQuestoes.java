package br.com.sc.dominio.extrator;

import java.io.Serializable;
import java.util.List;

import br.com.sc.dominio.Prova;
import br.com.sc.dominio.Questao;
import br.com.sc.exception.ExtracaoQuestoesException;

/**
 * Interface que define o comportamento de um extrator de quest�es.
 * 
 */
public interface ExtratorQuestoes extends Serializable {

	/**
	 * Obt�m as quest�es de uma prova.
	 * 
	 * @param prova
	 *            - Prova que cont�m as quest�es.
	 * @return as quest�es da prova.
	 * @throws ExtracaoQuestoesException
	 *             - Caso ocorra algum erro na extra��o das quest�es.
	 */
	List<Questao> run(Prova prova) throws ExtracaoQuestoesException;

}
