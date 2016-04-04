package br.com.sc.dominio.extrator;

import java.io.Serializable;
import java.util.List;

import br.com.sc.dominio.Prova;
import br.com.sc.dominio.Questao;
import br.com.sc.exception.ExtracaoQuestoesException;

/**
 * Interface que define o comportamento de um extrator de questões.
 * 
 */
public interface ExtratorQuestoes extends Serializable {

	/**
	 * Obtém as questões de uma prova.
	 * 
	 * @param prova
	 *            - Prova que contém as questões.
	 * @return as questões da prova.
	 * @throws ExtracaoQuestoesException
	 *             - Caso ocorra algum erro na extração das questões.
	 */
	List<Questao> run(Prova prova) throws ExtracaoQuestoesException;

}
