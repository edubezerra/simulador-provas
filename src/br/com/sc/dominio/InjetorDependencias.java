package br.com.sc.dominio;

import br.com.sc.dominio.extrator.FabricaExtratorQuestoes;
import br.com.sc.servico.ServicoIndexacao;

/**
 * Classe que injeta a referência dos repositórios nas entidades.
 * 
 */
public class InjetorDependencias {

	/**
	 * Injeta a referência dos repositórios nas entidades.
	 * 
	 * @param repositorioProva
	 *            - Repositório de provas.
	 * @param repositorioQuestao
	 *            - Repositório de questões.
	 */
	public InjetorDependencias(RepositorioProva repositorioProva,
			FabricaExtratorQuestoes fabricaExtratorQuestoes,
			FachadaDiretorioProvas diretorioProvas) {
		Prova.fabricaExtratorQuestoes = fabricaExtratorQuestoes;
		Prova.fachadaAcessoProvas = diretorioProvas;
		ServicoIndexacao.repositorioProva = repositorioProva;
	}

}
