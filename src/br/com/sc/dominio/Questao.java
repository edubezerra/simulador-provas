package br.com.sc.dominio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.apache.commons.lang.StringUtils;

import br.com.sc.exception.EntidadeInconsistenteException;
import br.com.sc.infra.log.Logger;

/**
 * Entidade que representa uma questão.
 * 
 */
@Entity
public abstract class Questao implements Serializable, EntidadeVerificavel {

	/**
	 * Classe de log.
	 */
	protected static Logger logger = new Logger(Questao.class);

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	Long id = null;

	@ManyToMany
	private List<Assunto> assuntos;

	/**
	 * Prova que contém a questão.
	 */
	@ManyToOne
	protected Prova prova;

	/**
	 * Alternativa que responde corretamente à questão.
	 */
	@OneToOne
	private Alternativa alternativaCorreta;

	/**
	 * Alternativas disponíveis para a questão.
	 */
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_QUESTAO", referencedColumnName = "ID")
	List<Alternativa> alternativas = new ArrayList<Alternativa>();
	
	/**
	 * Número da questão na prova.<BR/>
	 * É iniciada com o valor Integer.MIN_VALUE para facilitar a validação.
	 */
	protected int numero = Integer.MIN_VALUE;

	@Column(length = 4096)
	private String textoBase;

	/**
	 * Conteúdo da questão.
	 */
	@Column(length = 4096)
	protected String conteudo;

	/**
	 * Construtor default.
	 */
	Questao() {
	}

	/**
	 * Construtor.
	 * 
	 * @param conteudo
	 *            - Conteúdo da questão..
	 * @param numero
	 *            - Número da questão na prova.
	 * @param prova
	 *            - Prova que contém a questão.
	 * @throws EntidadeInconsistenteException
	 *             - Caso os dados informados não sejam consistentes.
	 */
	public Questao(String conteudo, int numero, Prova prova)
			throws EntidadeInconsistenteException {
		super();
		this.conteudo = conteudo;
		this.numero = numero;
		this.prova = prova;
		validarEstado();
	}

	/**
	 * Valida o estado da questão.
	 * 
	 * @throws EntidadeInconsistenteException
	 *             - Caso a questão esteja incosistente.
	 */
	public void validarEstado() throws EntidadeInconsistenteException {

		Notificacao notificacao = new Notificacao();

		if (StringUtils.isBlank(conteudo)) {
			notificacao.adicionar("Conteúdo obrigatório.");
		}

		if (numero == Integer.MIN_VALUE) {
			notificacao.adicionar("Número obrigatório.");
		}

		if (notificacao.possuiMensagens()) {
			throw new EntidadeInconsistenteException(notificacao);
		}
	}

	public String getConteudo() {
		return this.conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	/**
	 * Obtém o número da questão na prova.
	 * 
	 * @return o número da questão na prova.
	 */
	public int getNumero() {
		return numero;
	}

	public void addAlternativa(String texto, String opcao) {
		Alternativa alternativa = new Alternativa(texto, opcao);
		this.alternativas.add(alternativa);
	}

	public void setTextoBase(String textoBase) {
		this.textoBase = textoBase;
	}

	public String getTextoBase() {
		return textoBase;
	}

	public String toString() {
		StringBuilder result = new StringBuilder();
		String NEW_LINE = System.getProperty("line.separator");
		result.append(this.textoBase);
		result.append(NEW_LINE);
		for (Alternativa alternativa : alternativas) {
			result.append(alternativa.toString());
			result.append(NEW_LINE);
		}
		return result.toString();
	}

	public boolean isValida() {
		if (StringUtils.isBlank(this.textoBase)) {
			return false;
		}
		for (Alternativa alternativa : alternativas) {
			if (StringUtils.isBlank(alternativa.getConteudo())) {
				return false;
			}
		}
		return true;
	}


	public abstract void estruturar();

	public Long getId() {
		return id;
	}

	public Alternativa getAlternativaCorreta() {
		return alternativaCorreta;
	}
	
	public Prova getProva() {
		return prova;
	}
}
