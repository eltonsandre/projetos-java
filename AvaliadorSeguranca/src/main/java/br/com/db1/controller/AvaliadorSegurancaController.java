package br.com.db1.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.db1.model.Complexidade;
import br.com.db1.service.SenhaService;

@ManagedBean
@ViewScoped
public class AvaliadorSegurancaController implements Serializable {

	private static final long serialVersionUID = 1L;

	private SenhaService senhaService;

	private String senha;
	private Integer percentadem;
	private Complexidade complexidade;

	public AvaliadorSegurancaController() {
		senhaService = new SenhaService();
		percentadem = 0;
		complexidade = Complexidade.MUITO_CURTA;
	}

	public void avaliarSenha() {
		this.senhaService.validate(senha);
		this.complexidade = this.senhaService.getComplexidade();
		this.percentadem = this.senhaService.getPercentScore();
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Integer getPercentadem() {
		return percentadem;
	}

	public Complexidade getComplexidade() {
		return complexidade;
	}

}
