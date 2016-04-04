package br.com.sc.dominio;

import java.util.Collection;
import java.util.List;

import br.com.sc.exception.NomeProvaDuplicadoException;
import br.com.sc.exception.ProvaDuplicadaException;
import br.com.sc.exception.ProvaNaoEncontradaException;
import br.com.sc.infra.dao.ProvaDAO;

/**
 * Reposit�rio de provas.
 * 
 */
public class RepositorioProva {

	/**
	 * Classe de acesso a dados das provas.
	 */
	private ProvaDAO provaDAO;

	/**
	 * Construtor.
	 * 
	 * @param provaDAO
	 *            - Classe de acesso a dados das provas.
	 * @param fabricaExtratorQuestoes
	 *            - Classe que recupera extratores para uma prova.
	 */
	public RepositorioProva(ProvaDAO provaDAO) {
		this.provaDAO = provaDAO;
	}

	/**
	 * Persiste uma prova.
	 * 
	 * @param prova
	 *            - Prova a ser persistida.
	 * @throws NomeProvaDuplicadoException
	 *             - Caso j� exista uma prova com o nome de arquivo
	 *             especificado.
	 */
	public void salvar(Prova prova) throws ProvaDuplicadaException {
		provaDAO.incluir(prova);
	}

//	/**
//	 * Obt�m o arquivo que representa a prova.
//	 * 
//	 * @param prova
//	 *            - Prova a ter seu arquivo recuperado.
//	 * @return Arquivo que representa a prova.
//	 * @throws ProvaNaoEncontradaException
//	 *             - Caso o arquivo n�o seja localizado.
//	 */
//	public byte[] obterArquivo(Prova prova) throws ProvaNaoEncontradaException {
//		return provaDAO.obterArquivo(prova);
//	}

	/**
	 * Recupera todas as provas cadastradas.
	 * 
	 * @return lista com todas as provas cadastradas.
	 * @throws ProvaNaoEncontradaException
	 *             - Caso n�o exista nenhuma prova cadastrada.
	 */
	public List<Prova> obterTodas() throws ProvaNaoEncontradaException {
		return provaDAO.obterTodas();
	}

	/**
	 * Remove uma prova do reposit�rio.
	 * 
	 * @param prova
	 *            - Prova a ser removida.
	 */
	public void remover(Prova prova) {
		provaDAO.remover(prova);
	}

	/**
	 * Obt�m as quest�es da prova.
	 * 
	 * @return as quest�es da prova.
	 */
//	public List<Questao> extrairQuestoes(Prova prova) {
//
//		ExtratorQuestoes extrator = fabricaExtratorQuestoes
//				.obterExtratorProva(prova);
//
//		return extrator.run(prova);
//	}

	/**
	 * Obt�m as institui��es cadastradas.
	 * 
	 * @return as institui��es cadastradas.
	 */
	public Collection<String> obterBancasExaminadoras() {
		return provaDAO.obterBancasExaminadoras();
		//fabricaExtratorQuestoes.obterInstituicoesCadastradas();
	}

	public Questao obterQuestaoPorId(Long id) {
		return provaDAO.obterQuestaoPorId(id);
	}

	public Prova obterProvaPorId(Long idProva) {
		return provaDAO.obterProvaPorId(idProva);
	}
}
