package br.com.sc.dominio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;

import br.com.sc.exception.EntradaSaidaException;
import br.com.sc.exception.NomeProvaDuplicadoException;
import br.com.sc.exception.ProvaNaoEncontradaException;
import br.com.sc.infra.log.Logger;

/**
 * Classe de acesso a dados das provas. <BR/>
 * Utiliza o sistema de arquivos como reposit�rio.
 * 
 */
public class FachadaDiretorioProvas {

	/**
	 * Classe de log.
	 */
	private static Logger logger = new Logger(FachadaDiretorioProvas.class);

	/**
	 * Diret�rio onde as provas s�o armazenadas.
	 */
	private String caminhoDiretorioProvas;

	/**
	 * Construtor.
	 * 
	 * @param caminhoDiretorioProvas
	 *            - Diret�rio onde as provas s�o armazenadas.
	 */
	public FachadaDiretorioProvas(String caminhoDiretorioProvas) {
		this.caminhoDiretorioProvas = caminhoDiretorioProvas;
	}

	/**
	 * Recupera todas as provas cadastradas.
	 * 
	 * @return lista com todas as provas cadastradas.
	 * @throws ProvaNaoEncontradaException
	 *             - Caso n�o exista nenhuma prova cadastrada.
	 */
	// public synchronized List<Prova> obterTodas()
	// throws ProvaNaoEncontradaException {
	//
	// try {
	//
	// logger.log("Obtendo todas as provas cadastradas...");
	//
	// List<Prova> listaProvas = new ArrayList<Prova>();
	//
	// File fachadaAcessoProvas = new File(caminhoDiretorioProvas);
	//
	// File[] diretoriosInstituicoes = fachadaAcessoProvas.listFiles();
	//
	// if (ArrayUtils.isNotEmpty(diretoriosInstituicoes)) {
	//
	// for (File diretorioInstituicao : diretoriosInstituicoes) {
	//
	// if (!diretorioInstituicao.isDirectory()) {
	// continue;
	// }
	//
	// String instituicao = diretorioInstituicao.getName();
	//
	// File[] provas = diretorioInstituicao.listFiles();
	//
	// if (ArrayUtils.isNotEmpty(provas)) {
	//
	// for (File arquivoProva : provas) {
	//
	// // n�o adicionamos o conte�do da prova, pois � feito
	// // via lazy load
	// Prova prova = new Prova(arquivoProva.getName(),
	// instituicao);
	// listaProvas.add(prova);
	//
	// }
	// }
	// }
	//
	// logger.log("Provas Encontradas: " + listaProvas.size());
	//
	// if (!listaProvas.isEmpty()) {
	// return listaProvas;
	// }
	//
	// }
	// } catch (Exception e) {
	// throw new DAOException(e);
	// }
	//
	// logger.log("Nenhuma prova cadastrada...");
	//
	// throw new ProvaNaoEncontradaException();
	//
	// }

	/**
	 * Obt�m o arquivo que representa a prova.
	 * 
	 * @param prova
	 *            - Prova a ter seu arquivo recuperado.
	 * @return Arquivo que representa a prova.
	 * @throws ProvaNaoEncontradaException
	 *             - Caso o arquivo n�o seja localizado.
	 */
	public synchronized byte[] obterArquivo(Prova prova)
			throws ProvaNaoEncontradaException {

		InputStream streamArquivo = null;

		try {

			logger.log("Abrindo arquivo da prova: " + prova.getNomeArquivo());

			File diretorio = new File(caminhoDiretorioProvas + File.separator + prova.getBancaOrganizadora() + File.separator + prova.getAnoRealizacao());

			if (!diretorio.exists()) {
				logger.log("O diret�rio do arquivo da prova n�o existe."
						+ diretorio.getAbsolutePath());
				throw new EntradaSaidaException(
						"O diret�rio do arquivo da prova n�o existe: "
								+ diretorio.getAbsolutePath());
			}

			File[] arquivos = diretorio.listFiles();

			if (ArrayUtils.isNotEmpty(arquivos)) {
				for (File arquivo : arquivos) {

					if (arquivo.isFile()
							&& arquivo.getName().equals(prova.getNomeArquivo())) {

						streamArquivo = new BufferedInputStream(
								new FileInputStream(arquivo));

						return IOUtils.toByteArray(streamArquivo);
					}
				}
			}

		} catch (ProvaNaoEncontradaException e) {
			throw e;
		} catch (Exception e) {
			throw new EntradaSaidaException(e);
		} finally {
			IOUtils.closeQuietly(streamArquivo);
		}

		throw new ProvaNaoEncontradaException(prova.getNomeArquivo());
	}

	/**
	 * Armazena o arquivo de uma prova.
	 * 
	 * @param prova
	 *            - Prova cujo arquivo deve ser armazenado.
	 * @throws NomeProvaDuplicadoException
	 *             - Caso j� exista uma prova com o nome de arquivo especificado
	 *             no diret�rio.
	 */
	public synchronized void armazenarArquivo(Prova prova)
			throws NomeProvaDuplicadoException {

		OutputStream streamSaida = null;

		try {

			logger.log("Armazenando arquivo da prova: "
					+ prova.getNomeArquivo());

			File diretorio = new File(caminhoDiretorioProvas,
					prova.getBancaOrganizadora() + File.separator
							+ prova.getAnoRealizacao());

			if (!diretorio.exists()) {

				if (!diretorio.mkdirs()) {
					throw new IOException(
							"N�o foi poss�vel criar o diret�rio para armazenar a prova.");
				}
			} else {
				if (verificarArquivoExistente(diretorio, prova.getNomeArquivo())) {
					throw new NomeProvaDuplicadoException(
							prova.getNomeArquivo());
				}
			}

			File novoArquivo = new File(diretorio, prova.getNomeArquivo());

			streamSaida = new FileOutputStream(novoArquivo);

			IOUtils.write(prova.getArquivoProva(), streamSaida);

			logger.log("Prova cadastrada com sucesso.");

		} catch (NomeProvaDuplicadoException e) {
			throw e;
		} catch (FileNotFoundException e) {
			throw new EntradaSaidaException(e);
		} catch (IOException e) {
			throw new EntradaSaidaException(e);
		} finally {
			IOUtils.closeQuietly(streamSaida);
		}

	}

	/**
	 * Remove uma prova do diret�rio.
	 * 
	 * @param prova
	 *            - Prova a ser removida.
	 */
	public synchronized void remover(Prova prova) {

		try {

			logger.log("Removendo prova: " + prova.getBancaOrganizadora() + "/"
					+ prova.getNomeArquivo());

			File diretorioInstituicao = new File(caminhoDiretorioProvas,
					prova.getBancaOrganizadora());

			if (diretorioInstituicao.exists()) {

				File[] provas = diretorioInstituicao.listFiles();

				if (ArrayUtils.isNotEmpty(provas)) {

					for (File arquivoProva : provas) {

						if (arquivoProva.isFile()
								&& arquivoProva.getName().equals(
										prova.getNomeArquivo())) {

							FileUtils.forceDelete(arquivoProva);

							if (ArrayUtils.isEmpty(diretorioInstituicao.list())) {
								// diret�rio ficou vazio
								FileUtils.forceDelete(diretorioInstituicao);
								logger.log("Prova removida com sucesso.");
								return;
							}
						}
					}
				}
			}

			logger.log("Arquivo n�o localizado.");

		} catch (Exception e) {
			throw new EntradaSaidaException(e);
		}
	}

	/**
	 * Verifica a exist�ncia de um determinado arquivo.
	 * 
	 * @param diretorio
	 *            - Diret�rio que cont�m o arquivo.
	 * @param nomeArquivo
	 *            - Nome do arquivo a ser verificado.
	 * @return indica��o se o arquivo existe ou n�o no reposit�rio.
	 */
	private boolean verificarArquivoExistente(File diretorio, String nomeArquivo) {

		try {

			String[] nomesArquivos = diretorio.list();

			if (!ArrayUtils.isEmpty(nomesArquivos)) {

				for (String nome : nomesArquivos) {

					if (nome.equals(nomeArquivo)) {
						return true;
					}
				}
			}

			return false;
		} catch (Exception e) {
			throw new EntradaSaidaException(e);
		}
	}
}
