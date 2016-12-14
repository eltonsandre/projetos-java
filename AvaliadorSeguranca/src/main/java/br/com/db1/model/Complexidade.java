package br.com.db1.model;

public enum Complexidade {

	MUITO_CURTA("Muito Curta","label-danger"),
	MUITO_FRACO("Muito fraca","label-warning"),
	FRACO("Fraca","label-default"),
	BOA("Boa","label-info"),
	FORTE("Forte","label-primary"),
	MUITO_FORTE("Muito forte","label-success");

	private String descricao;
	private String style;

	private Complexidade(String descricao, String style) {
		this.descricao = descricao;
		this.style = style;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getStyle() {
		return style;
	}


}
