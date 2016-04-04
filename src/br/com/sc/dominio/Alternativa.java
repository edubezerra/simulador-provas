package br.com.sc.dominio;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang.StringUtils;

import br.com.sc.exception.EntidadeInconsistenteException;

@Entity
public class Alternativa implements Serializable, EntidadeVerificavel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Long id;

	@Column(length=4096)
	String conteudo;
	
	String letra;

	private Alternativa() {
	}

	public Alternativa(String conteudo, String letra) {
		super();
		this.conteudo = conteudo;
		this.letra = letra;
		validarEstado();
	}

	public Long getId() {
		return id;
	}

	public String getLetra() {
		return letra;
	}

	public String getConteudo() {
		return conteudo;
	}

	public String toString() {
		return this.letra + " " + this.conteudo;
	}

	@Override
	public void validarEstado() throws EntidadeInconsistenteException {
		Notificacao notificacao = new Notificacao();
		if (StringUtils.isBlank(conteudo)) {
			notificacao.adicionar("Conteúdo da questão é vazio.");
			throw new EntidadeInconsistenteException(notificacao);
		}
		if (StringUtils.isBlank(letra)) {
			notificacao.adicionar("Opção inválida.");
			throw new EntidadeInconsistenteException(notificacao);
		}
	}
}
