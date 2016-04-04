package br.com.sc.dominio.extrator;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import br.com.sc.dominio.Prova;
import br.com.sc.exception.ExtratorQuestoesNaoCadastradoException;

/**
 * Classe responsável por organizar os diferentes extratores de questões do
 * sistema.
 * 
 */
public class FabricaExtratorQuestoes {

	/**
	 * Mapa que contém os extratores.
	 */
	private Map<String, ExtratorQuestoes> mapaExtratores;

	/**
	 * Contrutor.
	 * 
	 * @param mapa
	 *            - Mapa contendo os extratores de questões relacionados com as
	 *            instituições da prova.
	 */
	public FabricaExtratorQuestoes(Map<String, ExtratorQuestoes> mapa) {
		mapaExtratores = mapa;
	}

	/**
	 * Obtém as instituições associadas aos extratores.
	 * 
	 * @return as instituições associadas aos extratores.
	 */
	public Collection<String> obterInstituicoesCadastradas() {

		return Collections.unmodifiableCollection(mapaExtratores.keySet());
	}

	/**
	 * Obtém um extrator de questões para a prova informada.
	 * 
	 * @param prova
	 *            - Prova a ser levada em consideração na escolha do extrator.
	 * @return um extrator de questões para a prova informada.
	 * @throws ExtratorQuestoesNaoCadastradoException
	 *             - Caso não existe um extrator de questões para a prova
	 *             informada.
	 */
	public ExtratorQuestoes obterExtratorProva(Prova prova)
			throws ExtratorQuestoesNaoCadastradoException {

		if (!mapaExtratores.containsKey(prova.getBancaOrganizadora())) {
			throw new ExtratorQuestoesNaoCadastradoException(
					"Extrator não encontrado: " + prova.getBancaOrganizadora());
		}

		return mapaExtratores.get(prova.getBancaOrganizadora());
	}
}
