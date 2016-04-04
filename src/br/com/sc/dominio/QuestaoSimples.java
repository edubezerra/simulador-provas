package br.com.sc.dominio;

public class QuestaoSimples extends Questao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public QuestaoSimples(String conteudo, int numero, Prova prova) {
		// TODO Auto-generated constructor stub
		super(conteudo, numero, prova);
	}
	
	public void estruturar() {
		String opcoes[] = { "(A)", "(B)", "(C)", "(D)", "(E)" };
		String conteudo = this.conteudo;

		logger.log("---Número: " + this.getNumero());
		logger.log("---Conteúdo: " + conteudo);

		try {
			/**
			 * Remove a primeira linha do conteúdo, que corresponde ao número da
			 * questão na prova.
			 */
			String regex = "[0123456789]{1,2}\\r\\n";
			conteudo = conteudo.replaceFirst(regex, "");

			String textoBase = conteudo.substring(0,
					conteudo.indexOf(opcoes[0]));
			textoBase = textoBase.trim().replaceAll("-\\r\\n", "");
			this.setTextoBase(textoBase);
			logger.log("textoBase: " + textoBase);
			logger.log("fim - textoBase");
		} catch (StringIndexOutOfBoundsException ex2) {
			logger.log("Erro durante obtenção do texto base da questão "
					+ this.getNumero());
			ex2.printStackTrace();
		}

		for (int i = 0; i < opcoes.length; i++) {
			int beginIndex = conteudo.indexOf(opcoes[i]) + opcoes[i].length();
			int endIndex;
			if (i + 1 < opcoes.length) {
				endIndex = conteudo.lastIndexOf(opcoes[i + 1]);
			} else {
				endIndex = conteudo.length() - 1;
			}
			try {
				if(endIndex > -1)
				{
					String texto = conteudo.substring(beginIndex, endIndex).trim();
					texto = texto.trim().replaceAll("-\\r\\n", "");
					texto = texto.replaceAll("\\r\\n", " ");
					if (texto.length() == 0) {
						this.setTextoBase("");
						throw new IllegalStateException("Texto da opção é vazio.");
					}
	
					this.addAlternativa(texto, opcoes[i]);
					Alternativa alternativa = new Alternativa(texto, opcoes[i]);
	
					logger.log(alternativa.toString());
				}
			} catch (IllegalStateException ex1) {
				ex1.printStackTrace();
				break;
			} catch (IllegalArgumentException ex1) {
				logger.log("Erro durante obtenção de opções da questão "
						+ this.getNumero());
				ex1.printStackTrace();
				break;
			} catch (StringIndexOutOfBoundsException ex2) {
				logger.log("Erro durante obtenção de opçoes da questão "
						+ this.getNumero());
				ex2.printStackTrace();
				break;
			}
		}
		System.out.println("===\n");
	}

}
