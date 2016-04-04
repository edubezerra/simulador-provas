package br.com.sc.infra.dao;

import java.util.Collection;
import java.util.List;

import br.com.sc.dominio.Prova;
import br.com.sc.dominio.Questao;
import br.com.sc.exception.NomeProvaDuplicadoException;
import br.com.sc.exception.ProvaDuplicadaException;
import br.com.sc.exception.ProvaNaoEncontradaException;

/**
 * Classe de acesso a dados das provas.
 * 
 */
public interface ProvaDAO {

	/**
	 * Persiste uma prova.
	 * 
	 * @param prova
	 *            - Prova a ser persistida.
	 * @throws NomeProvaDuplicadoException
	 *             - Caso já exista uma prova com o nome de arquivo
	 *             especificado.
	 */
	void incluir(Prova prova) throws ProvaDuplicadaException;

	/**
	 * Recupera todas as provas cadastradas.
	 * 
	 * @return lista com todas as provas cadastradas.
	 * @throws ProvaNaoEncontradaException
	 *             - Caso não exista nenhuma prova cadastrada.
	 */
	List<Prova> obterTodas() throws ProvaNaoEncontradaException;

	/**
	 * Remove uma prova do repositório.
	 * 
	 * @param prova
	 *            - Prova a ser removida.
	 */
	void remover(Prova prova);

	Collection<String> obterBancasExaminadoras();

	Questao obterQuestaoPorId(Long idQuestao);

	Prova obterProvaPorId(Long idProva);
}
