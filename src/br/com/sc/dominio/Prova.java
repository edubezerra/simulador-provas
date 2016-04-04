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
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import br.com.sc.dominio.extrator.ExtratorQuestoes;
import br.com.sc.dominio.extrator.FabricaExtratorQuestoes;
import br.com.sc.exception.EntidadeInconsistenteException;
import br.com.sc.exception.NomeProvaDuplicadoException;
import br.com.sc.exception.ProvaNaoEncontradaException;

/**
 * Entidade que representa uma prova.
 * 
 */
@Entity
public class Prova implements Serializable, EntidadeVerificavel {

	/**
	 * Classe que recupera extratores para uma prova.
	 */
	@Transient
	static FabricaExtratorQuestoes fabricaExtratorQuestoes;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	Long id;

	@OneToMany(mappedBy = "prova", cascade = CascadeType.ALL)
	// @JoinColumn(name = "ID_PROVA", referencedColumnName = "ID")
	List<Questao> questoes = new ArrayList<Questao>();

	private static final long serialVersionUID = 6404730707564474905L;

	static FachadaDiretorioProvas fachadaAcessoProvas;

	/**
	 * Nome do arquivo que representa a prova.
	 */
	private String nomeArquivo;

	/**
	 * Arquivo que representa a prova.
	 */
	@Transient
	private byte[] arquivoProva = null;

	// /**
	// * Instituição para a qual a prova foi organizada (Cesgranrio, CESPE,
	// etc).
	// */
	// private String instituicao;

	/**
	 * Banca organizadora da prova.
	 */
	private String bancaOrganizadora;

	/**
	 * Ano de realização da prova.
	 */
	private Integer anoRealizacao;

	/**
	 * Construtor.
	 * 
	 * @param nomeArquivo
	 *            - Nome do arquivo que representa a prova.
	 * @param instituicao
	 *            - Instituição que a qual a prova pertence.
	 * @param arquivoProva
	 *            - Arquivo que representa a prova.
	 * @throws EntidadeInconsistenteException
	 *             - Caso os argumentos informados sejam inválidos.
	 */
	public Prova(String nomeArquivo, String bancaOrganizadora,
			Integer anoRealizacao, byte[] arquivoProva)
			throws EntidadeInconsistenteException {
		super();
		this.nomeArquivo = nomeArquivo;
		this.bancaOrganizadora = bancaOrganizadora;
		this.anoRealizacao = anoRealizacao;
		this.arquivoProva = arquivoProva;

		try {
			fachadaAcessoProvas.armazenarArquivo(this);
		} catch (NomeProvaDuplicadoException e) {
			throw new EntidadeInconsistenteException("Prova duplicada.", e);
		}

		ExtratorQuestoes extrator = fabricaExtratorQuestoes
				.obterExtratorProva(this);

		questoes = extrator.run(this);

		validarEstado();
	}

	/**
	 * Valida o estado da prova.
	 * 
	 * @throws EntidadeInconsistenteException
	 *             - Caso a prova esteja incosistente.
	 */
	public void validarEstado() throws EntidadeInconsistenteException {

		Notificacao notificacao = new Notificacao();

		if (StringUtils.isBlank(bancaOrganizadora)) {
			notificacao.adicionar("A banca organizadora é item obrigatório");
		}

		if (StringUtils.isBlank(nomeArquivo)) {
			notificacao.adicionar("O nome do arquivo é item obrigatório");
		}

		if (notificacao.possuiMensagens()) {
			throw new EntidadeInconsistenteException(notificacao);
		}
	}

	/**
	 * Obtém o nome do arquivo que representa a prova.
	 * 
	 * @return nome do arquivo que representa a prova.
	 */
	public String getNomeArquivo() {
		return nomeArquivo;
	}

	// /**
	// * Obtém o nome da instituição que a qual a prova pertence.
	// *
	// * @return nome da instituição que a qual a prova pertence.
	// */
	// public String getInstituicao() {
	// return instituicao;
	// }

	/**
	 * Obtém o arquivo que representa a prova.
	 * 
	 * @return Arquivo que representa a prova.
	 * @throws ProvaNaoEncontradaException
	 *             - Caso o arquivo não seja localizado.
	 */
	public synchronized byte[] getArquivoProva()
			throws ProvaNaoEncontradaException {

		// lazy load
		if (arquivoProva == null) {
			arquivoProva = fachadaAcessoProvas.obterArquivo(this);
		}

		return arquivoProva;
	}

	/**
	 * Define o arquivo que representa a prova.
	 * 
	 * @param arquivoProva
	 *            - Arquivo que representa a prova.
	 */
	public void setArquivoProva(byte[] arquivoProva) {
		this.arquivoProva = arquivoProva;
	}

	/**
	 * Obtém as questões da prova.
	 * 
	 * @return as questões da prova.
	 */
	public List<Questao> getQuestoes() {
		return questoes;
	}

	/**
	 * Define um novo nome para o arquivo.
	 */
	public void definirNovoNomeArquivo() {
		String timestamp = String.valueOf(System.currentTimeMillis());
		timestamp = timestamp.concat("-");
		nomeArquivo = timestamp.concat(nomeArquivo);
	}

	public Long getId() {
		return id;
	}

	public String getBancaOrganizadora() {
		return bancaOrganizadora;
	}

	public Integer getAnoRealizacao() {
		return anoRealizacao;
	}

	public String toString() {
		return this.bancaOrganizadora + "-" + this.anoRealizacao + "-"
				+ this.id;
	}
}
