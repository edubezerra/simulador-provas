package br.com.sc.dominio.extrator;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import br.com.sc.dominio.Prova;
import br.com.sc.exception.ExtratorQuestoesNaoCadastradoException;

/**
 * Classe respons�vel por organizar os diferentes extratores de quest�es do
 * sistema.
 * 
 */
public class FabricaExtratorQuestoes {

	/**
	 * Mapa que cont�m os extratores.
	 */
	private Map<String, ExtratorQuestoes> mapaExtratores;

	/**
	 * Contrutor.
	 * 
	 * @param mapa
	 *            - Mapa contendo os extratores de quest�es relacionados com as
	 *            institui��es da prova.
	 */
	public FabricaExtratorQuestoes(Map<String, ExtratorQuestoes> mapa) {
		mapaExtratores = mapa;
	}

	/**
	 * Obt�m as institui��es associadas aos extratores.
	 * 
	 * @return as institui��es associadas aos extratores.
	 */
	public Collection<String> obterInstituicoesCadastradas() {

		return Collections.unmodifiableCollection(mapaExtratores.keySet());
	}

	/**
	 * Obt�m um extrator de quest�es para a prova informada.
	 * 
	 * @param prova
	 *            - Prova a ser levada em considera��o na escolha do extrator.
	 * @return um extrator de quest�es para a prova informada.
	 * @throws ExtratorQuestoesNaoCadastradoException
	 *             - Caso n�o existe um extrator de quest�es para a prova
	 *             informada.
	 */
	public ExtratorQuestoes obterExtratorProva(Prova prova)
			throws ExtratorQuestoesNaoCadastradoException {

		if (!mapaExtratores.containsKey(prova.getBancaOrganizadora())) {
			throw new ExtratorQuestoesNaoCadastradoException(
					"Extrator n�o encontrado: " + prova.getBancaOrganizadora());
		}

		return mapaExtratores.get(prova.getBancaOrganizadora());
	}
}
