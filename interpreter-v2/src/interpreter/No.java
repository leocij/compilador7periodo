package interpreter;

import java.util.ArrayList;
import java.util.List;

public class No {
	private No pai;
	private List<No> filhos = new ArrayList<>();
	private String tipo;
	private Token token;

	public No(String tipo) {
		this.tipo = tipo;
	}

	public No(Token token) {
		this.token = token;
		this.tipo = "NO_TOKEN";
	}

	public void addFilho(No filho) {
		filho.setPai(this);
		filhos.add(filho);
	}

	public No getPai() {
		return pai;
	}

	public void setPai(No pai) {
		this.pai = pai;
	}

	public List<No> getFilhos() {
		return filhos;
	}

	public void setFilhos(List<No> filhos) {
		this.filhos = filhos;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	@Override
	public String toString() {
		if(token == null){
			return tipo;
		}else{
			return token.getImagem();
		}
	}
}
