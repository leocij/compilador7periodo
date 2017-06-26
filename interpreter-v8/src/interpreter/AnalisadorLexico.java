package interpreter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnalisadorLexico {

	private List<Token> tokens = new ArrayList<>();
	private List<String> erros = new ArrayList<String>();
	private List<Simbolo> simbolos = new ArrayList<>();

	public void analisar(String caminho) throws IOException {
		FileReader myReader = new FileReader(caminho);
		BufferedReader myBuffer = new BufferedReader(myReader);
		String linha = myBuffer.readLine();
		Token t = new Token();
		int contLinha = 1;

		while (linha != null) {
			// System.out.println(linha);

			List<String> items = new ArrayList<>();

			boolean flag = false;
			int cont = 0;

			StringBuilder auxiliar = new StringBuilder();

			for (int i = 0; i < linha.length(); i++) {
				if (linha.charAt(i) == '"') {
					flag = !flag;
					cont++;
					i++;
				}
				if (flag)
					auxiliar.append(linha.charAt(i));
				if (cont > 0 && !flag) {
					items.add(auxiliar.toString());

					linha = linha.replace(auxiliar.toString(), " ");

					i = i - auxiliar.length();

					auxiliar = new StringBuilder();
					cont = 0;

				}
			} // FIM DO FOR 1

			String[] lexemas = linha.split(" ");
			boolean flag2 = false;
			int contColuna = 1;
			int k = 0;

			for (int i = 0; i < lexemas.length; i++) {

				t = new Token();
				if (lexemas[i].equals("#")) {
					break;
				} else if (lexemas[i].equals("\"")) {
					flag2 = !flag2;

					if (flag2) {

						t.setImagem(items.get(k));
						t.setClasse("CLS");
						t.setIndice(-1);
						t.setLinha(contLinha);
						t.setColuna(contColuna);
						contColuna += items.get(k).length();
						tokens.add(t);
						k++;

					}
				} else {

					if (palavraReservada(lexemas[i]) == true) {
						// System.out.printf("PalavraReservada: %s\n",
						// lexemas[i]);
						t.setImagem(lexemas[i]);
						t.setClasse("PR");
						t.setIndice(-1);
						t.setLinha(contLinha);
						t.setColuna(contColuna);
						tokens.add(t);
					} else if (delimitador(lexemas[i]) == true) {
						// System.out.printf("Delimitador: %s\n", lexemas[i]);
						t.setImagem(lexemas[i]);
						t.setClasse("DE");
						t.setIndice(-1);
						t.setLinha(contLinha);
						t.setColuna(contColuna);
						tokens.add(t);
					} else if (operador(lexemas[i]) == true) {
						// System.out.printf("Operador: %s\n", lexemas[i]);
						t.setImagem(lexemas[i]);
						t.setClasse("OP");
						t.setIndice(-1);
						t.setLinha(contLinha);
						t.setColuna(contColuna);
						tokens.add(t);
					} else {
						if (lexemas[i].matches("\\d+")) { // Constante Literal
							// Inteira
							// System.out.printf("CLI: %s\n", lexemas[i]);
							t.setImagem(lexemas[i]);
							t.setClasse("CLI");
							t.setIndice(-1);
							t.setLinha(contLinha);
							t.setColuna(contColuna);
							tokens.add(t);
						} else if (lexemas[i].matches("\\d+\\.\\d+")) { // Constante
							// Literal
							// Real
							// System.out.println("CLR: " + lexemas[i]);
							t.setImagem(lexemas[i]);
							t.setClasse("CLR");
							t.setIndice(-1);
							t.setLinha(contLinha);
							t.setColuna(contColuna);
							tokens.add(t);
						} else if (lexemas[i].matches("\\w{1}\\w*\\d*")) { // ID.
							// System.out.println("ID: " + lexemas[i]);
							t.setImagem(lexemas[i]);
							t.setClasse("ID");
							t.setIndice(indiceSimbolo(lexemas[i], simbolos, "ID"));
							t.setLinha(contLinha);
							t.setColuna(contColuna);
							tokens.add(t);
						} // FIM IF 3
					} // FIM IF 2

					contColuna += lexemas[i].length() + 1;
				} // FIM IF 1

			} // FIM DO FOR 2

			contLinha++;
			linha = myBuffer.readLine();
		} // FIM WHILE

		t = new Token();
		t.setImagem("$");
		t.setClasse("$"); // retorna aqui
		t.setIndice(-1);
		t.setLinha(-1);
		t.setColuna(-1);
		tokens.add(t);

		myReader.close();

		// System.out.println("\nTabela de Simbolos");
		// for (int i = 0; i < simbolos.size(); i++) {
		// System.out.print(" " + i);
		// System.out.print(" " + simbolos.get(i).getImagem());
		// System.out.print(" " + simbolos.get(i).getTipo());
		// System.out.println(" " + simbolos.get(i).getValor());
		// }
		//
		// System.out.println("\nTabela de Tokens");
		// for (int i = 0; i < tokens.size(); i++) {
		// System.out.print(" " + i);
		// System.out.print(" " + tokens.get(i).getImagem());
		// System.out.print(" " + tokens.get(i).getClasse());
		// System.out.print(" " + tokens.get(i).getIndice());
		// System.out.print(" " + tokens.get(i).getLinha());
		// System.out.println(" " + tokens.get(i).getColuna());
		// }

	}// FIM DO ANALISAR

	private boolean operador(String string) {
		String[] listaOp = new String[] { "+", "-", "*", "/", ">", "<", ">=", "<=", "!=", "==", "=", "&&", ".", "||" };

		for (int i = 0; i < listaOp.length; i++) {
			if (string.equals(listaOp[i])) {
				return true;
			}
		}
		return false;
	}

	private boolean delimitador(String string) {
		String[] listaDe = new String[] { "{", "}" };

		for (int i = 0; i < listaDe.length; i++) {
			if (string.equals(listaDe[i])) {
				return true;
			}
		}
		return false;
	}

	private boolean palavraReservada(String string) {
		String[] listaPr = new String[] { "int", "real", "texto", "se", "enquanto", "mostra", "le" };

		for (int i = 0; i < listaPr.length; i++) {
			if (string.equals(listaPr[i])) {
				return true;
			}
		}
		return false;
	}

	private int indiceSimbolo(String string, List<Simbolo> simbolos, String tipo) {
		int i = 0;
		for (; i < simbolos.size(); i++) {
			if (string.equals(simbolos.get(i).getImagem())) {
				return i;
			}
		}

		Simbolo s = new Simbolo();
		s.setImagem(string);
		s.setTipo(null);
		s.setValor(null);
		simbolos.add(s);

		return i;
	}

	public boolean temErros() {
		return !erros.isEmpty();
	}

	public void mostraErros() {
		for (String erro : erros) {
			System.out.println(erro);
		}
	}

	public List<Token> getTokens() {
		return tokens;
	}

	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
	}

	public List<String> getErros() {
		return erros;
	}

	public void setErros(List<String> erros) {
		this.erros = erros;
	}

	public List<Simbolo> getSimbolos() {
		return simbolos;
	}

	public void setSimbolos(List<Simbolo> simbolos) {
		this.simbolos = simbolos;
	}

}
