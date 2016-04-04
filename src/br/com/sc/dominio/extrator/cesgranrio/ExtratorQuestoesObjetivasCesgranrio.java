package br.com.sc.dominio.extrator.cesgranrio;

import java.util.List;

import br.com.sc.dominio.Questao;
import br.com.sc.dominio.extrator.ExtratorAbstratoQuestoes;
import br.com.sc.infra.log.Logger;

/**
 * Extrator de questões objetivas de provas da fundação CESGRANRIO.
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
	 * Sufixo do identificador da questão.
	 */
	private static final String SUFIXO_IDENTIFICADOR_QUESTAO = "\r\n";

	/**
	 * Prefixo do identificador da questão.
	 */
	private static final String PREFIXO_IDENTIFICADOR_QUESTAO = "\n";

	/**
	 * Lista de páginas a serem removidas.
	 */
	private static final int[] PAGINAS_DESPREZIVEIS = { 0 };

	/**
	 * Atributo que contém o provavém número máximo de questões em uma prova.<BR/>
	 * Esta informação auxilia na análise dos dados.
	 */
	private static final int PROVAVEL_NUMERO_MAXIMO_QUESTOES = 70;

	/**
	 * Palavras que devem ser removidas do texto.
	 */
	private static final String[] LIXO = { "ANALISTA DE SISTEMAS JÚNIOR",
			"ANALISTA DE SISTEMAS PLENO", "DESENVOLVIMENTO DE SOLUÇÕES",
			"www.pciconcursos.com.br" };

	/**
	 * Obtém os caracteres que representam o identificador de uma questão no
	 * texto.<BR/>
	 * 
	 * @param numeroQuestao
	 *            - Numero da questão a ter o identificador gerado.
	 * @return os caracteres que representam o identificador de uma questão no
	 *         texto.
	 */
	@Override
	protected String gerarIdentificadorBusca(int numeroQuestao) {
		return PREFIXO_IDENTIFICADOR_QUESTAO.concat(String.valueOf(
				numeroQuestao).concat(SUFIXO_IDENTIFICADOR_QUESTAO));
	}

	/**
	 * Obtém a relação de páginas a serem desprezadas.
	 * 
	 * @return a relação de páginas a serem desprezadas.
	 */
	@Override
	protected int[] obterPaginasDespreziveis() {
		return PAGINAS_DESPREZIVEIS;
	}

	/**
	 * Obtém a relação de palavras a serem removidas do texto.
	 * 
	 * @return a relação de palavras a serem removidas do texto.
	 */
	@Override
	protected String[] obterRelacaoLixo() {
		return LIXO;
	}

	/**
	 * Obtém o atributo que contém o provavém número máximo de questões em uma
	 * prova.<BR/>
	 * Esta estimativa é importante, pois auxilia na análise dos dados.
	 * 
	 * @return o provável número máximo de questões da prova.
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
