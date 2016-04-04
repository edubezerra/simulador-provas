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
 * Extrator de quest�es gen�rico.
 * 
 */
public abstract class ExtratorAbstratoQuestoes implements ExtratorQuestoes {

	private static final long serialVersionUID = -4694867251370890113L;

	/**
	 * Classe de log.
	 */
	private static Logger logger = new Logger(ExtratorAbstratoQuestoes.class);

	/**
	 * Obt�m as quest�es de uma prova.
	 * 
	 * @param prova
	 *            - Prova que cont�m as quest�es.
	 * @return as quest�es da prova.
	 * @throws ExtracaoQuestoesException
	 *             - Caso ocorra algum erro na extra��o das quest�es.
	 */
	public List<Questao> run(Prova prova)
			throws ExtracaoQuestoesException {

		InputStream stream = null;

		PDDocument documento = null;

		try {

			logger.log("Obtendo quest�es da prova: " + prova.getNomeArquivo());

			stream = new ByteArrayInputStream(prova.getArquivoProva());

			documento = PDDocument.load(stream);

			PDFTextStripper stripper = new PDFTextStripper();

			// removo as p�ginas indesej�veis
			removerPaginasDespreziveis(documento);

			// extraio o texto do documento
			String texto = stripper.getText(documento);
			
			// remove o conte�do indesejado.
			texto = removerConteudoIrrelevante(texto);

			List<Questao> questoes = new ArrayList<Questao>();

			// sequencial dos n�meros das quest�es
			int numeroQuestao = 1;

			// �ndice do in�cio da primeira quest�o
			int indiceInicial = obterIndiceQuestao(numeroQuestao, texto);

			while (indiceInicial != -1) {

				// incremento a vari�vel para a pr�xima quest�o
				numeroQuestao++;

				// indice para a pr�xima quest�o
				int indiceFinal = obterIndiceQuestao(numeroQuestao, texto);

				// caso um token equivalente ao �ndice da pr�xima quest�o
				// apare�a antes do token da quest�o atual, provavelmente �
				// uma coincid�ncia, portanto buscamos novamente at�
				// encontrarmos uma ocorr�ncia com �ndice posterior ao
				// da quest�o atual.
				if (indiceFinal != -1 && indiceFinal < indiceInicial) {

					// ser� usado para guardar o tamanho dos textos
					// descartados na busca
					int proporcional = 0;

					String aux = texto;

					while (indiceFinal < indiceInicial) {
						// adiciono o tamanho do token de busca ao �ndice final
						// para que ele n�o apare�a no pr�ximo fragmento
						indiceFinal += gerarIdentificadorBusca(numeroQuestao)
								.length();

						// guardo o tamanho deste trecho que ser� descartado
						// numa vari�vel para um posterior ajuste
						proporcional += indiceFinal;

						// removo o texto do fim da ocorr�ncia para tr�s
						aux = aux.substring(indiceFinal);

						// recupero o novo �ndicep ara uma ocorr�ncia do token
						indiceFinal = obterIndiceQuestao(numeroQuestao, aux);

						// o processo se repete at� acharmos uma ocorr�ncia
						// ap�s o �ndice da quest�o anterior
					}

					if (indiceFinal != -1) {
						// caso uma ocorr�ncia tenha sido encontrada,
						// ajustamos o �ndice da mesma para que seja
						// equivalente ao �ndice no texto completo.
						indiceFinal += proporcional;
					}
				}

				// algumas vezes lixo no documento ou quebras de p�gina
				// podem confundir o c�digo, fazendo com que duas ou mais
				// quest�es sejam extra�das juntas.
				// Para minimizar os danos, caso n�o seja encontrada
				// nenhuma refer�ncia para a pr�xima quest�o,
				// procuramos para quest�es seguintes.

				if (indiceFinal == -1) {

					for (int i = numeroQuestao + 1; i <= obterProvavelNumeroMaximoQuestoes(); i++) {

						int indiceBusca = obterIndiceQuestao(i, texto);

						if (indiceBusca != -1) {
							// caso seja encontrado um �ndice para uma
							// das pr�ximas quest�es, atualizamos as
							// vari�veis e seguimos o processamento.
							indiceFinal = indiceBusca;
							numeroQuestao = i;
							break;
						}
					}
				}

				String conteudo;

				if (indiceFinal == -1) {
					// caso nao exista ocorr�ncia para uma pr�xima quest�o,
					// levamos em considera��o que o resto do texto pertence
					// � quest�o atual.

					conteudo = texto.substring(indiceInicial, texto.length());
				} else {
					// extraimos o texto da pr�xima quest�o
					conteudo = texto.substring(indiceInicial, indiceFinal);

					// desprezamos o texto j� processado
					texto = texto.substring(indiceFinal);
				}

				// crio o objeto Questao com os dados extra�dos
				Questao questao = new QuestaoSimples(conteudo, numeroQuestao - 1,
						prova);

				questoes.add(questao);

				indiceInicial = obterIndiceQuestao(numeroQuestao, texto);
			}

			logger.log("Quest�es Extra�das: " + questoes.size());

			// retornamos a lista de quest�es estruturadas.
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
	 * Obt�m o �ndice do identificador da quest�o no texto.
	 * 
	 * @param numeroQuestao
	 *            - N�mero da quest�o a ser localizada.
	 * @param texto
	 *            - Texto contendo a quest�o.
	 * @return o �ndice do identificador da quest�o no texto.
	 */
	private int obterIndiceQuestao(int numeroQuestao, String texto) {
		return texto.indexOf(gerarIdentificadorBusca(numeroQuestao));
	}

	/**
	 * Obt�m os caracteres que representam o identificador de uma quest�o no
	 * texto.<BR/>
	 * 
	 * @param numeroQuestao
	 *            - Numero da quest�o a ter o identificador gerado.
	 * @return os caracteres que representam o identificador de uma quest�o no
	 *         texto.
	 */
	protected abstract String gerarIdentificadorBusca(int numeroQuestao);

	/**
	 * Obt�m uma representa��o do texto sem palavras indesejadas.
	 * 
	 * @param texto
	 *            - Texto a ser analisado.
	 * @return representa��o do texto sem palavras indesejadas.
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
	 * Obt�m a rela��o de palavras a serem removidas do texto.
	 * 
	 * @return a rela��o de palavras a serem removidas do texto.
	 */
	protected abstract String[] obterRelacaoLixo();

	/**
	 * Remove as p�ginas desprez�veis do documento.
	 * 
	 * @param documento
	 *            - Documento a ter as p�ginas removidas.
	 */
	protected void removerPaginasDespreziveis(PDDocument documento) {

		int[] paginasDespreziveis = obterPaginasDespreziveis();

		for (int numeroPagina : paginasDespreziveis) {
			documento.removePage(numeroPagina);
		}

	}

	/**
	 * Obt�m a rela��o de p�ginas a serem desprezadas.
	 * 
	 * @return a rela��o de p�ginas a serem desprezadas.
	 */
	protected abstract int[] obterPaginasDespreziveis();

	/**
	 * Obt�m o atributo que cont�m o provav�m n�mero m�ximo de quest�es em uma
	 * prova.<BR/>
	 * Esta estimativa � importante, pois auxilia na an�lise dos dados.
	 * 
	 * @return o prov�vel n�mero m�ximo de quest�es da prova.
	 */
	protected abstract int obterProvavelNumeroMaximoQuestoes();
}
