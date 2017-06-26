package interpreter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Gerador {

	private No raiz;
	private List<Simbolo> simbolos;
	private List<String> erros = new ArrayList<String>();
	private PrintWriter printWriter;
	private File arquivo;

	public Gerador(No raiz, List<Simbolo> simbolos) {
		this.setRaiz(raiz);
		this.setSimbolos(simbolos);
	}

	public void addErro(String erro) {
		erros.add("Erro de geração: " + erro + ". ");
	}

	public void gerar() throws Exception {

		try {
			arquivo = new File("saida.c");
			printWriter = new PrintWriter(arquivo);

			inicial();

			gerar(raiz);

			rodape();

			printWriter.flush();
			printWriter.close();

		} catch (Exception ex) {
			throw ex;
		}
	}

	private void inicial() throws FileNotFoundException {
		printWriter.write("#include <stdio.h>\n\n");
		printWriter.write("int main(){\n");
	}

	private void rodape() {
		printWriter.write("\n  return 0;\n");
		printWriter.write("}\n");

	}

	private Object gerar(No no) {
		if (no.getTipo().equals("NO_TOKEN")) {
			return null;
		} else if (no.getTipo().equals("NO_LIST_CMD")) {
			return listCmd(no);
		} else if (no.getTipo().equals("NO_CMD")) {
			return cmd(no);
		} else if (no.getTipo().equals("NO_CMD_INTER")) {
			return cmdInter(no);
		} else if (no.getTipo().equals("NO_DECL")) {
			return decl(no);
		} else if (no.getTipo().equals("NO_TIPO")) {
			return tipo(no);
		} else if (no.getTipo().equals("NO_LIST_ID")) {
			return listId(no);
		} else if (no.getTipo().equals("NO_LIST_ID_2")) {
			return listId2(no);
		} else if (no.getTipo().equals("NO_ESCRITA")) {
			return escrita(no);
		} else if (no.getTipo().equals("NO_EXP_ARIT")) {
			return expArit(no);
		} else if (no.getTipo().equals("NO_OPERAN")) {
			return operan(no);
		} else if (no.getTipo().equals("NO_LEITURA")) {
			return leitura(no);
		} else if (no.getTipo().equals("NO_ATRIB")) {
			return atrib(no);
		} else if (no.getTipo().equals("NO_COND")) {
			return cond(no);
		} else if (no.getTipo().equals("NO_EXP_LOG")) {
			return expLog(no);
		} else if (no.getTipo().equals("NO_EXP_REL")) {
			return expRel(no);
		} else if (no.getTipo().equals("NO_OP_REL")) {
			return opRel(no);
		} else if (no.getTipo().equals("NO_SENAO")) {
			return senao(no);
		} else if (no.getTipo().equals("NO_LACO")) {
			return laco(no);
		} else if (no.getTipo().equals("NO_OP_ARIT")) {
			return opArit(no);
		} else {
			addErro("Erro: NO desconhecido = " + no.getTipo());
			return null;
		}
	}

	// <op_arit> ::= '+' | '-' | '*' | '/' | '.'
	private Object opArit(No no) {
		printWriter.print(" " + no.getFilhos().get(0).getToken().getImagem() + " ");
		return null;
	}

	// <laco> ::= 'enquanto' <exp_log> <list_cmd>
	private Object laco(No no) {
		printWriter.write("  while(");
		gerar(no.getFilhos().get(1));
		printWriter.write("){\n");
		gerar(no.getFilhos().get(2));
		printWriter.write("}\n");
		return null;
	}

	// <senao> ::= '{' <list_cmd> '}' |
	private Object senao(No no) {
		if (!no.getFilhos().isEmpty()) {
			printWriter.print("else{\n");
			gerar(no.getFilhos().get(1));
			printWriter.print("}");
		}
		return null;
	}

	// <op_rel> ::= '>' | '<' | '>=' | '<=' | '==' | '!='
	private Object opRel(No no) {
		printWriter.write(" " + no.getFilhos().get(0).getToken().getImagem() + " ");
		return null;
	}

	// <exp_rel> ::= <op_rel> <operan> <operan> | <op_log> '{' <exp_rel> '}' '{'
	// <exp_rel> '}'
	private Object expRel(No no) {
		if (no.getFilhos().get(0).getTipo().equals("NO_OP_REL")) {

			Token operando1 = (Token) gerar(no.getFilhos().get(1));

			if (operando1.getClasse().equals("ID")) {
				printWriter.write(operando1.getImagem());
			} else if (operando1.getClasse().equals("CLI")) {
				printWriter.write(operando1.getImagem());
			} else if (operando1.getClasse().equals("CLR")) {
				printWriter.write(operando1.getImagem());
			} else if (operando1.getClasse().equals("CLS")) {
				printWriter.write("\"" + operando1.getImagem() + "\"");
			}

			gerar(no.getFilhos().get(0));

			Token operando2 = (Token) gerar(no.getFilhos().get(2));

			if (operando2.getClasse().equals("ID")) {
				printWriter.write(operando2.getImagem());
			} else if (operando2.getClasse().equals("CLI")) {
				printWriter.write(operando2.getImagem());
			} else if (operando2.getClasse().equals("CLR")) {
				printWriter.write(operando2.getImagem());
			} else if (operando2.getClasse().equals("CLS")) {
				printWriter.write("\"" + operando2.getImagem() + "\"");
			}

		} else if (no.getFilhos().get(0).getTipo().equals("NO_OP_LOG")) {
			gerar(no.getFilhos().get(2));
			gerar(no.getFilhos().get(5));
		}

		return null;
	}

	// <exp_log> ::= '{' <exp_rel> '}'
	private Object expLog(No no) {
		gerar(no.getFilhos().get(1));
		return null;
	}

	// <cond> ::= 'se' <exp_log> '{' <list_cmd> '}' <senao>
	private Object cond(No no) {

		printWriter.print("  if(");
		gerar(no.getFilhos().get(1));
		printWriter.print("){\n");
		gerar(no.getFilhos().get(3));
		printWriter.print("}");
		gerar(no.getFilhos().get(5));
		return null;
	}

	// <atrib> ::= '=' id <exp_arit>
	// @SuppressWarnings("unchecked")
	private Object atrib(No no) {

		// { = a1 { + a2 { * 10 a3 } } }

		Token id = no.getFilhos().get(1).getToken();

		printWriter.write("  " + id.getImagem() + " = ");

		gerar(no.getFilhos().get(2));

		printWriter.write(";\n");

		// List<Token> operandos = (List<Token>) gerar(no.getFilhos().get(2));
		//
		// for (Token operando : operandos) {
		// printWriter.write(operando.getImagem());
		// }

		return null;
	}

	// <leitura> ::= 'le' id
	private Object leitura(No no) {
		Token id = no.getFilhos().get(1).getToken();

		if (simbolos.get(id.getIndice()).getTipo().equals("int")) {
			printWriter.write("  scanf(\"%d\", &" + id.getImagem());
		} else if (simbolos.get(id.getIndice()).getTipo().equals("real")) {
			printWriter.write("  scanf(\"%lf\", &" + id.getImagem());
		} else if (simbolos.get(id.getIndice()).getTipo().equals("texto")) {
			printWriter.write("  scanf(\"%s\", " + id.getImagem());
		}

		printWriter.write(");\n");

		return null;
	}

	// <operan> ::= id | cli | clr | cls
	private Object operan(No no) {
		return no.getFilhos().get(0).getToken();
	}

	// <exp_arit> ::= <operan> | '{' <op_arit> <exp_arit> <exp_arit> '}'
	@SuppressWarnings("unchecked")
	private Object expArit(No no) {

		if (no.getFilhos().size() == 1) {

			Token operando = (Token) gerar(no.getFilhos().get(0));

			// System.out.println("Passei aqui --> " +
			// no.getPai().getToken().getImagem());

			if (operando.getClasse().equals("ID")) {
				printWriter.write(operando.getImagem());

				// if
				// (simbolos.get(operando.getIndice()).getTipo().equals("int"))
				// {
				// printWriter.write("\"%d\", " + operando.getImagem());
				// } else if
				// (simbolos.get(operando.getIndice()).getTipo().equals("real"))
				// {
				// printWriter.write("\"%lf\", " + operando.getImagem());
				// } else if
				// (simbolos.get(operando.getIndice()).getTipo().equals("texto"))
				// {
				// printWriter.write("\"%s\", " + operando.getImagem());
				// }
			} else if (operando.getClasse().equals("CLI")) {
				printWriter.write(operando.getImagem());
			} else if (operando.getClasse().equals("CLR")) {

			} else if (operando.getClasse().equals("CLS")) {
				printWriter.write("\"" + operando.getImagem() + "\"");
			}

			List<Token> operandos = new ArrayList<>();
			operandos.add(operando);
			return operandos;
		} else if (no.getFilhos().size() == 5) {
			List<Token> expArits1 = (List<Token>) gerar(no.getFilhos().get(2));
			gerar(no.getFilhos().get(1));
			List<Token> expArits2 = (List<Token>) gerar(no.getFilhos().get(3));

			expArits1.addAll(expArits2);
			return expArits1;
		}

		return null;
	}

	// <escrita> ::= 'mostra' <exp_arit>
	private Object escrita(No no) {
		// System.out.println("Passei aqui --> " +
		// no.getFilhos().get(1).getFilhos().get(0).getFilhos().get(0).getToken());

		Token id = (Token) no.getFilhos().get(1).getFilhos().get(0).getFilhos().get(0).getToken();
		if (id.getClasse().equals("ID")) {
			printWriter.write("  printf(\"%d\", ");
		} else {
			printWriter.write("  printf(");
		}

		gerar(no.getFilhos().get(1));
		printWriter.write(");\n");
		return null;
	}

	// <list_id2> ::= <list_id> |
	private Object listId2(No no) {
		if (no.getFilhos().isEmpty()) {
			return new ArrayList<Token>();
		} else {
			return gerar(no.getFilhos().get(0));
		}
	}

	// <list_id> ::= id <list_id2>
	@SuppressWarnings("unchecked")
	private Object listId(No no) {
		Token id = no.getFilhos().get(0).getToken();
		List<Token> listId2 = (List<Token>) gerar(no.getFilhos().get(1));
		listId2.add(0, id);

		return listId2;
	}

	// <tipo> ::= 'int' | 'real' | 'texto' | 'logico'
	private Object tipo(No no) {
		return no.getFilhos().get(0).getToken().getImagem();
	}

	// <decl> ::= <tipo> <list_id>
	@SuppressWarnings("unchecked")
	private Object decl(No no) {
		String tipo = (String) gerar(no.getFilhos().get(0));

		if (tipo.equals("int")) {
			printWriter.write("  int ");
		} else if (tipo.equals("real")) {
			printWriter.write("  double ");
		} else if (tipo.equals("texto")) {
			printWriter.write("  char ");
		}

		List<Token> listId = (List<Token>) gerar(no.getFilhos().get(1));
		int cont = 0;
		for (Token id : listId) {
			if (cont > 0) {
				printWriter.write(", ");
			}

			if (simbolos.get(id.getIndice()).getTipo().equals("int")) {
				printWriter.write(id.getImagem() + " = 0");
			} else if (simbolos.get(id.getIndice()).getTipo().equals("real")) {
				printWriter.write(id.getImagem() + " = 0.0");
			} else if (simbolos.get(id.getIndice()).getTipo().equals("texto")) {
				printWriter.write(id.getImagem() + "[100]");
			}

			cont++;
		}

		printWriter.write(";\n");
		return null;
	}

	// <cmd_inter> ::= <decl> | <atrib> | <laco> | <cond> | <escrita> |
	// <leitura>
	private Object cmdInter(No no) {
		gerar(no.getFilhos().get(0));
		return null;
	}

	// <cmd> ::= '{' <cmd_inter> '}'
	private Object cmd(No no) {
		gerar(no.getFilhos().get(1));
		// printWriter.write(";\n");
		return null;
	}

	// <list_cmd> ::= <cmd> <list_cmd> | &
	private Object listCmd(No no) {
		if (!no.getFilhos().isEmpty()) {
			gerar(no.getFilhos().get(0));
			gerar(no.getFilhos().get(1));
		}
		return null;
	}

	// FIM DAS REGRAS

	public boolean temErros() {
		return !erros.isEmpty();
	}

	public void mostraErros() {
		for (String erro : erros) {
			System.out.println(erro);
		}
	}

	public No getRaiz() {
		return raiz;
	}

	public void setRaiz(No raiz) {
		this.raiz = raiz;
	}

	public List<Simbolo> getSimbolos() {
		return simbolos;
	}

	public void setSimbolos(List<Simbolo> simbolos) {
		this.simbolos = simbolos;
	}

	public List<String> getErros() {
		return erros;
	}

	public void setErros(List<String> erros) {
		this.erros = erros;
	}

}
