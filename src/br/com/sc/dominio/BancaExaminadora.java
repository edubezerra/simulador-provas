package br.com.sc.dominio;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang.StringUtils;

import br.com.sc.exception.EntidadeInconsistenteException;

@Entity
public class BancaExaminadora implements EntidadeVerificavel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	Long id;

	String nome;

	public BancaExaminadora() {
	}

	public BancaExaminadora(String nome) {
		super();
		this.nome = nome;
		validarEstado();
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public void validarEstado() throws EntidadeInconsistenteException {
		Notificacao notificacao = new Notificacao();
		if (StringUtils.isBlank(nome)) {
			notificacao.adicionar("Nome é informação obrigar");
			throw new EntidadeInconsistenteException(notificacao);
		}
	}
}
