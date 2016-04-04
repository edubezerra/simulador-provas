package br.com.sc.dominio.extrator.cesgranrio;

import java.util.List;

import br.com.sc.dominio.Questao;
import br.com.sc.dominio.extrator.ExtratorAbstratoQuestoes;
import br.com.sc.infra.log.Logger;

/**
 * Extrator de quest�es objetivas de provas da funda��o CESGRANRIO.
 * 
 */
public class ExtratorQuestoesObjetivasCesgranrio extends
		ExtratorAbstratoQuestoes {

	private static final long serialVersionUID = 8741451055154649115L;

	/**
	 * Classe de log.
	 */
	private static Logger logger = new Logger(
			ExtratorQuestoesObjetivasCesgranrio.class);

	/**
	 * Sufixo do identificador da quest�o.
	 */
	private static final String SUFIXO_IDENTIFICADOR_QUESTAO = "\r\n";

	/**
	 * Prefixo do identificador da quest�o.
	 */
	private static final String PREFIXO_IDENTIFICADOR_QUESTAO = "\n";

	/**
	 * Lista de p�ginas a serem removidas.
	 */
	private static final int[] PAGINAS_DESPREZIVEIS = { 0 };

	/**
	 * Atributo que cont�m o provav�m n�mero m�ximo de quest�es em uma prova.<BR/>
	 * Esta informa��o auxilia na an�lise dos dados.
	 */
	private static final int PROVAVEL_NUMERO_MAXIMO_QUESTOES = 70;

	/**
	 * Palavras que devem ser removidas do texto.
	 */
	private static final String[] LIXO = { "ANALISTA DE SISTEMAS J�NIOR",
			"ANALISTA DE SISTEMAS PLENO", "DESENVOLVIMENTO DE SOLU��ES",
			"www.pciconcursos.com.br" };

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
		return LIXO;
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
		for (Questao questao : questoes) {
			questao.estruturar();
		}
		return questoes;
	}
}
