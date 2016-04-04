package br.com.sc.dominio;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Assunto {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Integer idAssunto;

	@Column(nullable = false, unique = true, length = 50)
	private String descricao;

	public Assunto() {

	}

	public Integer getIdAssunto() {
		return idAssunto;
	}

	public void setIdAssunto(Integer idAssunto) {
		this.idAssunto = idAssunto;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
