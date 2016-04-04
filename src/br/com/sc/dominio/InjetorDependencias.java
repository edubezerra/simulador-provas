package br.com.sc.dominio;

import br.com.sc.dominio.extrator.FabricaExtratorQuestoes;
import br.com.sc.servico.ServicoIndexacao;

/**
 * Classe que injeta a refer�ncia dos reposit�rios nas entidades.
 * 
 */
public class InjetorDependencias {

	/**
	 * Injeta a refer�ncia dos reposit�rios nas entidades.
	 * 
	 * @param repositorioProva
	 *            - Reposit�rio de provas.
	 * @param repositorioQuestao
	 *            - Reposit�rio de quest�es.
	 */
	public InjetorDependencias(RepositorioProva repositorioProva,
			FabricaExtratorQuestoes fabricaExtratorQuestoes,
			FachadaDiretorioProvas diretorioProvas) {
		Prova.fabricaExtratorQuestoes = fabricaExtratorQuestoes;
		Prova.fachadaAcessoProvas = diretorioProvas;
		ServicoIndexacao.repositorioProva = repositorioProva;
	}

}
