package interpreter;

public class Token {

	private String imagem;
	private String classe;
	private int indice;
	private int linha;
	private int coluna;
	public Token() {
		super();
	}
	public Token(String imagem, String classe, int indice, int linha, int coluna) {
		super();
		this.imagem = imagem;
		this.classe = classe;
		this.indice = indice;
		this.linha = linha;
		this.coluna = coluna;
	}
	public String getImagem() {
		return imagem;
	}
	public void setImagem(String imagem) {
		this.imagem = imagem;
	}
	public String getClasse() {
		return classe;
	}
	public void setClasse(String classe) {
		this.classe = classe;
	}
	public int getIndice() {
		return indice;
	}
	public void setIndice(int indice) {
		this.indice = indice;
	}
	public int getLinha() {
		return linha;
	}
	public void setLinha(int linha) {
		this.linha = linha;
	}
	public int getColuna() {
		return coluna;
	}
	public void setColuna(int coluna) {
		this.coluna = coluna;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + indice;
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
		if (indice != other.indice)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Token [imagem=" + imagem + ", classe=" + classe + ", indice=" + indice + ", linha=" + linha
				+ ", coluna=" + coluna + "]";
	}
}
