package br.com.sc.dominio.extrator.cespe;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import br.com.sc.dominio.Prova;
import br.com.sc.dominio.Questao;
import br.com.sc.dominio.QuestaoComposta;
import br.com.sc.dominio.QuestaoSimples;
import br.com.sc.dominio.extrator.ExtratorAbstratoQuestoes;
import br.com.sc.exception.ExtracaoQuestoesException;
import br.com.sc.infra.log.Logger;

public class ExtratorQuestoesObjetivasCespe extends ExtratorAbstratoQuestoes {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6018153560568256445L;
	
	/**
	 * Classe de log.
	 */
	private static Logger logger = new Logger(
			ExtratorQuestoesObjetivasCespe.class);

	/**
	 * Lista de p�ginas a serem removidas.
	 */
	private static final int[] PAGINAS_DESPREZIVEIS = { 7 };
	
	//^[A-Z].*.[^0-9]
	
	/**
	 * Sufixo do identificador da quest�o.
	 */
	private static final String SUFIXO_IDENTIFICADOR_QUESTAO = "\r\n";

	/**
	 * Prefixo do identificador da quest�o.
	 */
	private static final String PREFIXO_IDENTIFICADOR_QUESTAO = "\r\n";
	
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
			"www.pciconcursos.com.br", "CONHECIMENTOS ESPEC�FICOS / N�VEL SUPERIOR",
			"PSP-RH-1/2004 � Aplica��o: 28/3/2004", "UnB / CESPE � PETROBRAS",
			"Cargo 5: Analista de Sistemas Pleno", "� permitida a reprodu��o apenas para fins did�ticos, desde que citada a fonte."};
	
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
		// No caso do Cespe, o numero da questao nao e utilizado...
		return PREFIXO_IDENTIFICADOR_QUESTAO;
	}
	
	/**
	 * Obt�m as quest�es de uma prova.
	 * 
	 * @param prova
	 *            - Prova que cont�m as quest�es.
	 * @return as quest�es da prova.
	 * @throws ExtracaoQuestoesException
	 *             - Caso ocorra algum erro na extra��o das quest�es.
	 */
	public List<Questao> run(Prova prova)	{
		
		/* Gostaria de reescrever o m�todo visto que as provas 
		 * elaboradas pelo CESPE n�o possuem n�mero
		 * Os n�meros s�o referentes as op��es de cada quest�o
		 */
		
		InputStream stream = null;
		PDDocument documento = null;
		
		try {
			
			// Vamos ser inteligentes e manter o LOG que o Eduardo criou ...
			logger.log("Obtendo quest�es da prova: " + prova.getNomeArquivo());
			
			// 1 - Todo o conteudo da prova � extra�do para uma string
			stream = new ByteArrayInputStream(prova.getArquivoProva());
			documento = PDDocument.load(stream);
			PDFTextStripper stripper = new PDFTextStripper();
			
			// EB: removo as p�ginas indesej�veis
			// Lembrete: Professor, precisei alterar a visibilidade deste m�todo
			removerPaginasDespreziveis(documento);

			// EB: extraio o texto do documento
			String texto = stripper.getText(documento);
			
			texto = texto.replaceAll("�", "-");
			// Remover os caracteres invalidos
			texto = texto.replaceAll("([^\\p{L}+\\s\\.\\,\\-\\?\\n\\r\\)\\(1-9])", "OPT");
			
			// EB: remove o conte�do indesejado.
			// Lembrete: Professor, precisei alterar a visibilidade deste m�todo
			texto = removerConteudoIrrelevante(texto);
			
			List<Questao> questoes= new ArrayList<Questao>();
			List<Questao> subquestoes= new ArrayList<Questao>();
			
			// As questoes elaboradas pelo CESPE n�o possuem sequencial de questoes
			
			// estou pensando em navegar no texto e prosseguir desprezando os blocos de 
			// string j� utilizados
			
			// Primeiro teste: String questaoPattern = "[A-Z].*.[^\\w]";
			// Segundo teste: String questaoPattern= "(\\w)+\\.(\\r\\n|\\n)";
			// Terceiro teste: Capturar o in�cio da pr�xima quest�o(ou fim da quest�o atual que consiste em um ponto precedido de quebra de linha e seguido de word character)
			String fimDaLinha= "\\.([\\n\\r])";
			String opcaoQuestaPattern= "([\\r\\n])([OPT+]+\\s\\w)";
			
			
			String conteudo, enunciado= "";
			Boolean capturouOpcao= false;
			int endIndex, numeroQuestao=0;
			
			int contadorOpcoes= 0;
			
			Pattern pattern = Pattern.compile(fimDaLinha, Pattern.DOTALL);
			Pattern patternOpcaoQuestao = Pattern.compile(opcaoQuestaPattern, Pattern.DOTALL);
			
			Matcher matcher = pattern.matcher(texto);
			while(matcher.find())
			{
				// Estamos navegando entre os par�grafos do texto
				endIndex= matcher.end() - 1;
				
				// Carregamos o par�grafo na variavel conteudo
				conteudo= texto.substring(0, endIndex);
				
				Matcher matcherQuestao= patternOpcaoQuestao.matcher(conteudo);
				// Verificamos se o paragrafo � o inicio de uma quest�o
				if(!matcherQuestao.find())
				{
					// A questao so estara completa se o paragrafo atual for um cabecalho e o anterior por uma opcao
					if(capturouOpcao)
					{
						QuestaoComposta questao = new QuestaoComposta(enunciado, numeroQuestao++,	prova);
						questao.setSubquestoes(subquestoes);
						
						questoes.add(questao);
						
						enunciado= "";
						subquestoes.removeAll(subquestoes);
						contadorOpcoes= 0;
					}
					
					capturouOpcao= false;
					enunciado= enunciado.concat(conteudo);
				}
				else
				{
					// Constatamos que o par�grafo � uma op��o de quest�o
					capturouOpcao= true;
					
					// Inserimos a marca��o do op��o no inicio do conteudo
					conteudo= conteudo.replaceAll("([OPT+]+)", String.valueOf(contadorOpcoes));
					conteudo= conteudo.concat("\n(A) SIM \n (B) N�O");
					
					// Inserir esta op��o como uma quest�o SIMPLES, com a alternativa SIM | NAO
					Questao questao = new QuestaoSimples(conteudo, contadorOpcoes, prova);
					subquestoes.add(questao);
					
					contadorOpcoes++;
				}
				
				// EB: desprezamos o texto j� processado
				texto = texto.substring(endIndex);
				// Atualizamos o Matcher
				matcher = pattern.matcher(texto);
			}
			
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
