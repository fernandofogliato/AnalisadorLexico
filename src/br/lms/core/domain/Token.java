package br.lms.core.domain;

public class Token {

	private int codigo;
	private String simbolo;
	private String descricao;
	private int posicao;

	public Token() {

	}
	
	public Token(int codigo, String simbolo, String descricao) {
		this.codigo = codigo;
		this.simbolo = simbolo;
		this.descricao = descricao;
		this.posicao = 0;
	}
	
	public Token(int codigo, String simbolo, String descricao, int posicao) {
		this.codigo = codigo;
		this.simbolo = simbolo;
		this.descricao = descricao;
		this.posicao = posicao;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public int getPosicao() {
		return posicao;
	}

	public void setPosicao(int posicao) {
		this.posicao = posicao;
	}

	@Override
	public String toString() {
		return this.codigo + " - " + this.simbolo + " - " + this.descricao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((simbolo == null) ? 0 : simbolo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Token other = (Token) obj;
		if (simbolo == null) {
			if (other.simbolo != null)
				return false;
		} else if (!simbolo.equals(other.simbolo))
			return false;
		return true;
	}

}
