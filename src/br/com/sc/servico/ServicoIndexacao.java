package br.com.sc.servico;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import br.com.sc.dominio.Prova;
import br.com.sc.dominio.Questao;
import br.com.sc.dominio.RepositorioProva;
import br.com.sc.exception.IndexacaoException;
import br.com.sc.exception.ProvaNaoEncontradaException;
import br.com.sc.infra.log.Logger;

/**
 * Serviço responsável pela indexação e busca de questões.<BR/>
 * 
 * Seus métodos são sincronizados para evitar problmas de concorrência,
 * 
 */
public class ServicoIndexacao {

	/**
	 * Classe de log.
	 */
	private static Logger logger = new Logger(ServicoIndexacao.class);

	/**
	 * Caminho onde os índices serão armazenados.
	 */
	private String caminhoDiretorioIndice;

	/**
	 * Caminho relativo para o arquivo contendo palavras a serem ignoradas na
	 * busca.
	 */
	private String arquivoPalavras;

	/**
	 * Flag que indica o estado do serviço.
	 */
	private boolean iniciado = false;

	/**
	 * Analizador de índices.
	 */
	private Analyzer analizador;

	/**
	 * Diretório de armazenamento de índices.
	 */
	private Directory diretorioIndice;

	/**
	 * Palavras a serem ignoradas na busca.
	 */
	private Set<String> palavrasIgnoradas;

	public static RepositorioProva repositorioProva;

	/**
	 * 
	 * @param caminhoDiretorioIndice
	 *            - Caminho onde os índices serão armazenados.
	 * @param arquivoPalavras
	 *            - Caminho relativo para o arquivo contendo palavras a serem
	 *            ignoradas na busca.
	 */
	public ServicoIndexacao(String caminhoDiretorioIndice,
			String arquivoPalavras) {
		this.caminhoDiretorioIndice = caminhoDiretorioIndice;
		this.arquivoPalavras = arquivoPalavras;
	}

	/**
	 * Prepara as dependências do serviço.
	 * 
	 * @throws IndexacaoException
	 *             - Caso ocorra algum erro na preparação das dependências.
	 */
	@SuppressWarnings("unchecked")
	private synchronized void iniciar() throws IndexacaoException {

		if (!iniciado) {

			logger.log("Iniciando serviço de indexação...");

			InputStream inputStream = null;

			try {
				inputStream = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(arquivoPalavras);

				palavrasIgnoradas = new HashSet<String>(
						IOUtils.readLines(inputStream));

				analizador = new StandardAnalyzer(Version.LUCENE_29,
						palavrasIgnoradas);

				diretorioIndice = FSDirectory.open(new File(
						caminhoDiretorioIndice));

				iniciado = true;

			} catch (Exception e) {
				throw new IndexacaoException(e);
			} finally {
				IOUtils.closeQuietly(inputStream);
			}

			logger.log("Serviço de indexação iniciado.");
		}
	}

	/**
	 * Cria índices para as questões informadas.
	 * 
	 * @param questoes
	 *            - Questões a serem indexadas.
	 */
	public synchronized void indexarQuestoes(Prova prova) {
		indexarQuestoes(prova, false);
	}

	/**
	 * Cria índices para as questões informadas.
	 * 
	 * @param questoes
	 *            - Questões a serem indexadas.
	 * @param forcarCriacao
	 *            - Indicação se já houver algum índice criado, deve ser
	 *            sobresctrito.
	 */
	public synchronized void indexarQuestoes(Prova prova, boolean forcarCriacao) {

		try {

			iniciar();

			logger.log("indexando " + prova.getQuestoes().size()
					+ " questões...");
			logger.log("Forcar Criacao: " + forcarCriacao);

			IndexWriter criadorIndice = null;

			try {

				// adicionando a um índice existente
				criadorIndice = new IndexWriter(diretorioIndice, analizador,
						forcarCriacao, MaxFieldLength.UNLIMITED);

			} catch (FileNotFoundException e) {
				// nenhum índice no diretório, criando...
				criadorIndice = new IndexWriter(diretorioIndice, analizador,
						true, MaxFieldLength.UNLIMITED);
			}

			Document documento;

			for (Questao questao : prova.getQuestoes()) {

				documento = new Document();

				String idQuestao = String.valueOf(questao.getId());

				logger.log("Adicionando questão ao índice: " + idQuestao);

				// campos não analizados na busca

				documento.add(new Field("id", idQuestao, Field.Store.YES,
						Field.Index.NOT_ANALYZED));

				documento.add(new Field("numero", String.valueOf(questao
						.getNumero()), Field.Store.YES,
						Field.Index.NOT_ANALYZED));

				documento.add(new Field("idProva", String.valueOf(prova.getId()),
						Field.Store.YES, Field.Index.NOT_ANALYZED));

				documento.add(new Field("bancaOrganizadora", String
						.valueOf(prova.getBancaOrganizadora()),
						Field.Store.YES, Field.Index.NOT_ANALYZED));

				// campos que são analizados na busca

				documento.add(new Field("conteudo", questao.toString(),
						Field.Store.YES, Field.Index.ANALYZED));

				criadorIndice.addDocument(documento);
			}

			criadorIndice.close();

			logger.log("Questões indexadas com sucesso.");

		} catch (IndexacaoException e) {
			throw e;
		} catch (Exception e) {
			throw new IndexacaoException(e);
		}
	}

	/**
	 * Obtém questões relevantes aos termos informados.
	 * 
	 * @param termosBusca
	 *            - termos levados em consideração na busca.
	 * @param maximoResultados
	 *            - Número máximo de resultados da busca.
	 * @return questões relevantes aos termos informados.
	 * @throws IndexacaoException
	 *             - Caso ocorra algum erro na busca.
	 */

	/**
	 * Obtém questões relevantes aos termos informados.
	 * 
	 * @param termosBusca
	 *            - termos levados em consideração na busca.
	 * @param maximoResultados
	 *            - numero máximo de resultados.
	 * @return questões relevantes aos termos informados.
	 */
	public synchronized List<Questao> recuperarQuestoes(String termosBusca,
			int maximoResultados) throws IndexacaoException {
		try {

			iniciar();

			logger.log("Buscando questões pelo termo: " + termosBusca);
			logger.log("Máximo de Resultados: " + maximoResultados);

			Query busca = new QueryParser(Version.LUCENE_29, "conteudo",
					analizador).parse(termosBusca);

			IndexSearcher localizador = new IndexSearcher(diretorioIndice, true);

			TopScoreDocCollector coletor = TopScoreDocCollector.create(
					maximoResultados, true);

			localizador.search(busca, coletor);

			ScoreDoc[] resultados = coletor.topDocs().scoreDocs;

			List<Questao> questoes = new ArrayList<Questao>();

			logger.log("Resultados Encontrados: " + resultados.length);

			for (ScoreDoc resultado : resultados) {
				int identificadorDocumento = resultado.doc;
				Document documento = localizador.doc(identificadorDocumento);

				try {
					Long id = Long.parseLong(documento.get("id"));
					logger.log("Recuperando questão: " + documento.get("id"));
					Questao questao = repositorioProva.obterQuestaoPorId(id);
					if (questao == null) {
						logger.log("Questão não encontrada para identificador "
								+ id);
						continue;
					}
					if (questao.isValida()) {
						questoes.add(questao);
					}
				} catch (NumberFormatException e) {
					logger.log("Identificador inválido para questão ("
							+ documento.get("id") + ")");
				}
			}

			localizador.close();

			logger.log("Questões efetivamente adicionadas: " + questoes.size());

			return questoes;

		} catch (IndexacaoException e) {
			throw e;
		} catch (Exception e) {
			throw new IndexacaoException(e);
		}
	}

	/**
	 * Remove os índices das questões de uma prova.
	 * 
	 * @param prova
	 *            - Prova a ter as questões removidas do índice.
	 */
	public synchronized void remover(Prova prova) {

		try {
			iniciar();

			IndexReader leitor = IndexReader.open(diretorioIndice, false);

			Term termo = new Term("idProva", String.valueOf(prova.getId()));

			logger.log("Removendo questões da prova: " + prova.toString());

			int questoesRemovidas = leitor.deleteDocuments(termo);

			logger.log("Questões removidas: " + questoesRemovidas);

			leitor.flush();

			leitor.close();

		} catch (Exception e) {
			throw new IndexacaoException(e);
		}
	}

	/**
	 * Remove os índices atuais e cria novos índices levando em consideração
	 * todas as provas cadastradas.
	 */
	public synchronized void recriarIndices() {

		try {
			iniciar();

			logger.log("Iniciando processo de recriação de índices...");

			List<Prova> provas = repositorioProva.obterTodas();

			boolean criarIndice = true;

			for (Prova prova : provas) {

				indexarQuestoes(prova, criarIndice);

				// pois o índice só deve ser criado na primeira iteração, depois
				// os outros índices irão complementa-lo
				criarIndice = false;
			}

			logger.log("Índices recriados com sucesso.");

		} catch (IndexacaoException e) {
			throw e;
		} catch (ProvaNaoEncontradaException e) {
			throw e;
		} catch (Exception e) {
			throw new IndexacaoException(e);
		}
	}

	/**
	 * Número máximo de resultados padrão.
	 */
	private static final int NUMERO_RESULTADOS_PADRAO = 50;

	/**
	 * Obtém questões relevantes aos termos informados.
	 * 
	 * @param termosBusca
	 *            - termos levados em consideração na busca.
	 * @return questões relevantes aos termos informados.
	 */
	public List<Questao> recuperarQuestoes(String termosBusca) {
		return recuperarQuestoes(termosBusca, NUMERO_RESULTADOS_PADRAO);
	}

	/**
	 * Salva o arquivo de prova, cria índices para suas questões e retorna as
	 * mesmas.
	 */
	public List<Questao> indexarProva(Prova prova) {
		// crio os índices
		indexarQuestoes(prova);

		return prova.getQuestoes();

	}
}
