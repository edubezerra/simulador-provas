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
 * Servi�o respons�vel pela indexa��o e busca de quest�es.<BR/>
 * 
 * Seus m�todos s�o sincronizados para evitar problmas de concorr�ncia,
 * 
 */
public class ServicoIndexacao {

	/**
	 * Classe de log.
	 */
	private static Logger logger = new Logger(ServicoIndexacao.class);

	/**
	 * Caminho onde os �ndices ser�o armazenados.
	 */
	private String caminhoDiretorioIndice;

	/**
	 * Caminho relativo para o arquivo contendo palavras a serem ignoradas na
	 * busca.
	 */
	private String arquivoPalavras;

	/**
	 * Flag que indica o estado do servi�o.
	 */
	private boolean iniciado = false;

	/**
	 * Analizador de �ndices.
	 */
	private Analyzer analizador;

	/**
	 * Diret�rio de armazenamento de �ndices.
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
	 *            - Caminho onde os �ndices ser�o armazenados.
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
	 * Prepara as depend�ncias do servi�o.
	 * 
	 * @throws IndexacaoException
	 *             - Caso ocorra algum erro na prepara��o das depend�ncias.
	 */
	@SuppressWarnings("unchecked")
	private synchronized void iniciar() throws IndexacaoException {

		if (!iniciado) {

			logger.log("Iniciando servi�o de indexa��o...");

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

			logger.log("Servi�o de indexa��o iniciado.");
		}
	}

	/**
	 * Cria �ndices para as quest�es informadas.
	 * 
	 * @param questoes
	 *            - Quest�es a serem indexadas.
	 */
	public synchronized void indexarQuestoes(Prova prova) {
		indexarQuestoes(prova, false);
	}

	/**
	 * Cria �ndices para as quest�es informadas.
	 * 
	 * @param questoes
	 *            - Quest�es a serem indexadas.
	 * @param forcarCriacao
	 *            - Indica��o se j� houver algum �ndice criado, deve ser
	 *            sobresctrito.
	 */
	public synchronized void indexarQuestoes(Prova prova, boolean forcarCriacao) {

		try {

			iniciar();

			logger.log("indexando " + prova.getQuestoes().size()
					+ " quest�es...");
			logger.log("Forcar Criacao: " + forcarCriacao);

			IndexWriter criadorIndice = null;

			try {

				// adicionando a um �ndice existente
				criadorIndice = new IndexWriter(diretorioIndice, analizador,
						forcarCriacao, MaxFieldLength.UNLIMITED);

			} catch (FileNotFoundException e) {
				// nenhum �ndice no diret�rio, criando...
				criadorIndice = new IndexWriter(diretorioIndice, analizador,
						true, MaxFieldLength.UNLIMITED);
			}

			Document documento;

			for (Questao questao : prova.getQuestoes()) {

				documento = new Document();

				String idQuestao = String.valueOf(questao.getId());

				logger.log("Adicionando quest�o ao �ndice: " + idQuestao);

				// campos n�o analizados na busca

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

				// campos que s�o analizados na busca

				documento.add(new Field("conteudo", questao.toString(),
						Field.Store.YES, Field.Index.ANALYZED));

				criadorIndice.addDocument(documento);
			}

			criadorIndice.close();

			logger.log("Quest�es indexadas com sucesso.");

		} catch (IndexacaoException e) {
			throw e;
		} catch (Exception e) {
			throw new IndexacaoException(e);
		}
	}

	/**
	 * Obt�m quest�es relevantes aos termos informados.
	 * 
	 * @param termosBusca
	 *            - termos levados em considera��o na busca.
	 * @param maximoResultados
	 *            - N�mero m�ximo de resultados da busca.
	 * @return quest�es relevantes aos termos informados.
	 * @throws IndexacaoException
	 *             - Caso ocorra algum erro na busca.
	 */

	/**
	 * Obt�m quest�es relevantes aos termos informados.
	 * 
	 * @param termosBusca
	 *            - termos levados em considera��o na busca.
	 * @param maximoResultados
	 *            - numero m�ximo de resultados.
	 * @return quest�es relevantes aos termos informados.
	 */
	public synchronized List<Questao> recuperarQuestoes(String termosBusca,
			int maximoResultados) throws IndexacaoException {
		try {

			iniciar();

			logger.log("Buscando quest�es pelo termo: " + termosBusca);
			logger.log("M�ximo de Resultados: " + maximoResultados);

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
					logger.log("Recuperando quest�o: " + documento.get("id"));
					Questao questao = repositorioProva.obterQuestaoPorId(id);
					if (questao == null) {
						logger.log("Quest�o n�o encontrada para identificador "
								+ id);
						continue;
					}
					if (questao.isValida()) {
						questoes.add(questao);
					}
				} catch (NumberFormatException e) {
					logger.log("Identificador inv�lido para quest�o ("
							+ documento.get("id") + ")");
				}
			}

			localizador.close();

			logger.log("Quest�es efetivamente adicionadas: " + questoes.size());

			return questoes;

		} catch (IndexacaoException e) {
			throw e;
		} catch (Exception e) {
			throw new IndexacaoException(e);
		}
	}

	/**
	 * Remove os �ndices das quest�es de uma prova.
	 * 
	 * @param prova
	 *            - Prova a ter as quest�es removidas do �ndice.
	 */
	public synchronized void remover(Prova prova) {

		try {
			iniciar();

			IndexReader leitor = IndexReader.open(diretorioIndice, false);

			Term termo = new Term("idProva", String.valueOf(prova.getId()));

			logger.log("Removendo quest�es da prova: " + prova.toString());

			int questoesRemovidas = leitor.deleteDocuments(termo);

			logger.log("Quest�es removidas: " + questoesRemovidas);

			leitor.flush();

			leitor.close();

		} catch (Exception e) {
			throw new IndexacaoException(e);
		}
	}

	/**
	 * Remove os �ndices atuais e cria novos �ndices levando em considera��o
	 * todas as provas cadastradas.
	 */
	public synchronized void recriarIndices() {

		try {
			iniciar();

			logger.log("Iniciando processo de recria��o de �ndices...");

			List<Prova> provas = repositorioProva.obterTodas();

			boolean criarIndice = true;

			for (Prova prova : provas) {

				indexarQuestoes(prova, criarIndice);

				// pois o �ndice s� deve ser criado na primeira itera��o, depois
				// os outros �ndices ir�o complementa-lo
				criarIndice = false;
			}

			logger.log("�ndices recriados com sucesso.");

		} catch (IndexacaoException e) {
			throw e;
		} catch (ProvaNaoEncontradaException e) {
			throw e;
		} catch (Exception e) {
			throw new IndexacaoException(e);
		}
	}

	/**
	 * N�mero m�ximo de resultados padr�o.
	 */
	private static final int NUMERO_RESULTADOS_PADRAO = 50;

	/**
	 * Obt�m quest�es relevantes aos termos informados.
	 * 
	 * @param termosBusca
	 *            - termos levados em considera��o na busca.
	 * @return quest�es relevantes aos termos informados.
	 */
	public List<Questao> recuperarQuestoes(String termosBusca) {
		return recuperarQuestoes(termosBusca, NUMERO_RESULTADOS_PADRAO);
	}

	/**
	 * Salva o arquivo de prova, cria �ndices para suas quest�es e retorna as
	 * mesmas.
	 */
	public List<Questao> indexarProva(Prova prova) {
		// crio os �ndices
		indexarQuestoes(prova);

		return prova.getQuestoes();

	}
}
