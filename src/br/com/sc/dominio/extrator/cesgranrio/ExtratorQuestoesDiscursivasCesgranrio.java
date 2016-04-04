package br.com.sc.dominio.extrator.cesgranrio;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import br.com.sc.dominio.Questao;
import br.com.sc.dominio.extrator.ExtratorAbstratoQuestoes;

/**
 * Extrator de quest�es discursivas de provas da funda��o CESGRANRIO.
 * 
 */
public class ExtratorQuestoesDiscursivasCesgranrio extends
		ExtratorAbstratoQuestoes {

	private static final long serialVersionUID = 3611815857062935982L;

	/**
	 * Sufixo do identificador da quest�o.
	 */
	private static final String SUFIXO_IDENTIFICADOR_QUESTAO = "\r\n";

	/**
	 * Prefixo do identificador da quest�o.
	 */
	private static final String PREFIXO_IDENTIFICADOR_QUESTAO = "\nQuest�o no ";

	/**
	 * Lista de p�ginas a serem removidas.
	 */
	private static final int[] PAGINAS_DESPREZIVEIS = { 0 };

	/**
	 * Atributo que cont�m o provav�m n�mero m�ximo de quest�es em uma prova.<BR/>
	 * Esta informa��o auxilia na an�lise dos dados.
	 */
	private static final int PROVAVEL_NUMERO_MAXIMO_QUESTOES = 15;

	/**
	 * Obt�m os caracteres que representam o identificador de uma quest�o no
	 * texto.<BR/>
	 * 
	 * @param numeroQuestao
	 *            - Numero da quest�o a ter o identificador gerado.
	 * @return os caracteres que representam o identificador de uma quest�o no
	 *         texto.
	 */
	@Override
	protected String gerarIdentificadorBusca(int numeroQuestao) {
		return PREFIXO_IDENTIFICADOR_QUESTAO.concat(String.valueOf(
				numeroQuestao).concat(SUFIXO_IDENTIFICADOR_QUESTAO));
	}

	/**
	 * Obt�m a rela��o de p�ginas a serem desprezadas.
	 * 
	 * @return a rela��o de p�ginas a serem desprezadas.
	 */
	@Override
	protected int[] obterPaginasDespreziveis() {
		return PAGINAS_DESPREZIVEIS;
	}

	/**
	 * Obt�m a rela��o de palavras a serem removidas do texto.
	 * 
	 * @return a rela��o de palavras a serem removidas do texto.
	 */
	@Override
	protected String[] obterRelacaoLixo() {
		// sem lixo, por enquanto...
		return null;
	}

	/**
	 * Obt�m o atributo que cont�m o provav�m n�mero m�ximo de quest�es em uma
	 * prova.<BR/>
	 * Esta estimativa � importante, pois auxilia na an�lise dos dados.
	 * 
	 * @return o prov�vel n�mero m�ximo de quest�es da prova.
	 */
	@Override
	protected int obterProvavelNumeroMaximoQuestoes() {
		return PROVAVEL_NUMERO_MAXIMO_QUESTOES;
	}

	@Override
	protected List<Questao> estruturarQuestoes(List<Questao> questoes) {
		throw new NotImplementedException();
	}

}
