package br.com.sc.dominio.extrator;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import br.com.sc.dominio.Prova;
import br.com.sc.dominio.Questao;
import br.com.sc.dominio.QuestaoSimples;
import br.com.sc.exception.ExtracaoQuestoesException;
import br.com.sc.infra.log.Logger;

/**
 * Extrator de questões genérico.
 * 
 */
public abstract class ExtratorAbstratoQuestoes implements ExtratorQuestoes {

	private static final long serialVersionUID = -4694867251370890113L;

	/**
	 * Classe de log.
	 */
	private static Logger logger = new Logger(ExtratorAbstratoQuestoes.class);

	/**
	 * Obtém as questões de uma prova.
	 * 
	 * @param prova
	 *            - Prova que contém as questões.
	 * @return as questões da prova.
	 * @throws ExtracaoQuestoesException
	 *             - Caso ocorra algum erro na extração das questões.
	 */
	public List<Questao> run(Prova prova)
			throws ExtracaoQuestoesException {

		InputStream stream = null;

		PDDocument documento = null;

		try {

			logger.log("Obtendo questões da prova: " + prova.getNomeArquivo());

			stream = new ByteArrayInputStream(prova.getArquivoProva());

			documento = PDDocument.load(stream);

			PDFTextStripper stripper = new PDFTextStripper();

			// removo as páginas indesejáveis
			removerPaginasDespreziveis(documento);

			// extraio o texto do documento
			String texto = stripper.getText(documento);
			
			// remove o conteúdo indesejado.
			texto = removerConteudoIrrelevante(texto);

			List<Questao> questoes = new ArrayList<Questao>();

			// sequencial dos números das questões
			int numeroQuestao = 1;

			// índice do início da primeira questão
			int indiceInicial = obterIndiceQuestao(numeroQuestao, texto);

			while (indiceInicial != -1) {

				// incremento a variável para a próxima questão
				numeroQuestao++;

				// indice para a próxima questão
				int indiceFinal = obterIndiceQuestao(numeroQuestao, texto);

				// caso um token equivalente ao índice da próxima questão
				// apareça antes do token da questão atual, provavelmente é
				// uma coincidência, portanto buscamos novamente até
				// encontrarmos uma ocorrência com índice posterior ao
				// da questão atual.
				if (indiceFinal != -1 && indiceFinal < indiceInicial) {

					// será usado para guardar o tamanho dos textos
					// descartados na busca
					int proporcional = 0;

					String aux = texto;

					while (indiceFinal < indiceInicial) {
						// adiciono o tamanho do token de busca ao índice final
						// para que ele não apareça no próximo fragmento
						indiceFinal += gerarIdentificadorBusca(numeroQuestao)
								.length();

						// guardo o tamanho deste trecho que será descartado
						// numa variável para um posterior ajuste
						proporcional += indiceFinal;

						// removo o texto do fim da ocorrência para trás
						aux = aux.substring(indiceFinal);

						// recupero o novo índicep ara uma ocorrência do token
						indiceFinal = obterIndiceQuestao(numeroQuestao, aux);

						// o processo se repete até acharmos uma ocorrência
						// após o índice da questão anterior
					}

					if (indiceFinal != -1) {
						// caso uma ocorrência tenha sido encontrada,
						// ajustamos o índice da mesma para que seja
						// equivalente ao índice no texto completo.
						indiceFinal += proporcional;
					}
				}

				// algumas vezes lixo no documento ou quebras de página
				// podem confundir o código, fazendo com que duas ou mais
				// questões sejam extraídas juntas.
				// Para minimizar os danos, caso não seja encontrada
				// nenhuma referência para a próxima questão,
				// procuramos para questões seguintes.

				if (indiceFinal == -1) {

					for (int i = numeroQuestao + 1; i <= obterProvavelNumeroMaximoQuestoes(); i++) {

						int indiceBusca = obterIndiceQuestao(i, texto);

						if (indiceBusca != -1) {
							// caso seja encontrado um índice para uma
							// das próximas questões, atualizamos as
							// variáveis e seguimos o processamento.
							indiceFinal = indiceBusca;
							numeroQuestao = i;
							break;
						}
					}
				}

				String conteudo;

				if (indiceFinal == -1) {
					// caso nao exista ocorrência para uma próxima questão,
					// levamos em consideração que o resto do texto pertence
					// à questão atual.

					conteudo = texto.substring(indiceInicial, texto.length());
				} else {
					// extraimos o texto da próxima questão
					conteudo = texto.substring(indiceInicial, indiceFinal);

					// desprezamos o texto já processado
					texto = texto.substring(indiceFinal);
				}

				// crio o objeto Questao com os dados extraídos
				Questao questao = new QuestaoSimples(conteudo, numeroQuestao - 1,
						prova);

				questoes.add(questao);

				indiceInicial = obterIndiceQuestao(numeroQuestao, texto);
			}

			logger.log("Questões Extraídas: " + questoes.size());

			// retornamos a lista de questões estruturadas.
			return estruturarQuestoes(questoes);
		} catch (Exception e) {
			throw new ExtracaoQuestoesException(e);
		} finally {
			IOUtils.closeQuietly(stream);

			try {
				documento.close();
			} catch (Exception e) {
				// quieto...
			}

		}

	}

	protected abstract List<Questao> estruturarQuestoes(List<Questao> questoes);

	/**
	 * Obtém o índice do identificador da questão no texto.
	 * 
	 * @param numeroQuestao
	 *            - Número da questão a ser localizada.
	 * @param texto
	 *            - Texto contendo a questão.
	 * @return o índice do identificador da questão no texto.
	 */
	private int obterIndiceQuestao(int numeroQuestao, String texto) {
		return texto.indexOf(gerarIdentificadorBusca(numeroQuestao));
	}

	/**
	 * Obtém os caracteres que representam o identificador de uma questão no
	 * texto.<BR/>
	 * 
	 * @param numeroQuestao
	 *            - Numero da questão a ter o identificador gerado.
	 * @return os caracteres que representam o identificador de uma questão no
	 *         texto.
	 */
	protected abstract String gerarIdentificadorBusca(int numeroQuestao);

	/**
	 * Obtém uma representação do texto sem palavras indesejadas.
	 * 
	 * @param texto
	 *            - Texto a ser analisado.
	 * @return representação do texto sem palavras indesejadas.
	 */
	protected String removerConteudoIrrelevante(String texto) {

		String[] lixo = obterRelacaoLixo();

		for (int i = 0; lixo != null && i < lixo.length; i++) {

			if (texto.contains(lixo[i])) {
				texto = texto.replaceAll(lixo[i], "");
			}
		}

		return texto;
	}

	/**
	 * Obtém a relação de palavras a serem removidas do texto.
	 * 
	 * @return a relação de palavras a serem removidas do texto.
	 */
	protected abstract String[] obterRelacaoLixo();

	/**
	 * Remove as páginas desprezíveis do documento.
	 * 
	 * @param documento
	 *            - Documento a ter as páginas removidas.
	 */
	protected void removerPaginasDespreziveis(PDDocument documento) {

		int[] paginasDespreziveis = obterPaginasDespreziveis();

		for (int numeroPagina : paginasDespreziveis) {
			documento.removePage(numeroPagina);
		}

	}

	/**
	 * Obtém a relação de páginas a serem desprezadas.
	 * 
	 * @return a relação de páginas a serem desprezadas.
	 */
	protected abstract int[] obterPaginasDespreziveis();

	/**
	 * Obtém o atributo que contém o provavém número máximo de questões em uma
	 * prova.<BR/>
	 * Esta estimativa é importante, pois auxilia na análise dos dados.
	 * 
	 * @return o provável número máximo de questões da prova.
	 */
	protected abstract int obterProvavelNumeroMaximoQuestoes();
}
