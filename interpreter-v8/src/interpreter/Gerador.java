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

	public void gerar() throws FileNotFoundException {
		arquivo = new File("saida.c");
		printWriter = new PrintWriter(arquivo);
		printWriter.write("\n");
		printWriter.write("#include <stdio.h>\n");
		printWriter.write("#include <stdlib.h>\n");
		printWriter.write("#include <string.h>\n\n");
		printWriter.write("int main(){\n");
		printWriter.write("\n");
		gerar(raiz);
		printWriter.write("  return 0;\n");
		printWriter.write("}\n");
		printWriter.flush();
		printWriter.close();
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
		} else if (no.getTipo().equals("NO_ATRIB")) {
			return atrib(no);
		} else if (no.getTipo().equals("NO_EXP_ARIT")) {
			return expArit(no);
		} else if (no.getTipo().equals("NO_OP_ARIT")) {
			return opArit(no);
		} else if (no.getTipo().equals("NO_OPERAN")) {
			return operan(no);
		} else if (no.getTipo().equals("NO_LACO")) {
			return laco(no);
		} else if (no.getTipo().equals("NO_EXP_LOG")) {
			return expLog(no);
		} else if (no.getTipo().equals("NO_EXP_REL")) {
			return expRel(no);
		} else if (no.getTipo().equals("NO_COND")) {
			return cond(no);
		} else if (no.getTipo().equals("NO_OP_REL")) {
			return opRel(no);
		} else if (no.getTipo().equals("NO_SENAO")) {
			return senao(no);
		} else if (no.getTipo().equals("NO_LEITURA")) {
			return leitura(no);
		} else if (no.getTipo().equals("NO_ESCRITA")) {
			return escrita(no);
		} else {
			addErro("NO desconhecido = " + no.getTipo());
			return null;
		}
	}

	// <escrita> ::= 'mostra' <exp_arit>
	@SuppressWarnings("unchecked")
	private Object escrita(No no) {

		List<Token> operandos = (List<Token>) gerar(no.getFilhos().get(1));

		gerar(no.getFilhos().get(1));

		for (Token operando : operandos) {
			// printWriter.write(" printf(");
			if (operando.getClasse().equals("ID")) {
				if (simbolos.get(operando.getIndice()).getTipo().equals("int")) {
					printWriter.write("  printf(\"%d\", " + operando.getImagem() + ");\n");
				} else if (simbolos.get(operando.getIndice()).getTipo().equals("real")) {
					printWriter.write("  printf(\"%lf\", " + operando.getImagem() + ");\n");
				} else if (simbolos.get(operando.getIndice()).getTipo().equals("texto")) {
					printWriter.write("  printf(\"%s\", " + operando.getImagem() + ");\n");
				}

			} else if (operando.getClasse().equals("CLI")) {
				printWriter.write("  printf(\"%d\", " + operando.getImagem() + ");\n");
			} else if (operando.getClasse().equals("CLR")) {
				printWriter.write("  printf(\"%lf\", " + operando.getImagem() + ");\n");
			} else if (operando.getClasse().equals("CLS")) {
				printWriter.write("  printf(\"" + operando.getImagem() + "\");\n");
			}
		}
		printWriter.write("\n");

		// Token id = (Token) no.getFilhos().get(1).getToken();
		// String tipo = (String) simbolos.get(id.getIndice()).getTipo();
		//
		// if (tipo.equals("int")) {
		// List<Token> operandos = (List<Token>) gerar(no.getFilhos().get(2));
		// printWriter.write(" " + id.getImagem() + " =");
		// for (Token operando : operandos) {
		// printWriter.write(" " + operando.getImagem());
		// }
		// printWriter.write(";\n");
		// } else if (tipo.equals("real")) {
		// List<Token> operandos = (List<Token>) gerar(no.getFilhos().get(2));
		// printWriter.write(" " + id.getImagem() + " =");
		// for (Token operando : operandos) {
		// printWriter.write(" " + operando.getImagem());
		// }
		// printWriter.write(";\n");
		// } else if (tipo.equals("texto")) {
		// printWriter.write(" " + id.getImagem() + "[0] = \'\\0\';\n");
		// List<Token> operandos = (List<Token>) gerar(no.getFilhos().get(2));
		//
		// int iaux = 0;
		// for (Token operando : operandos) {
		// if (operando.getClasse().equals("ID")) {
		//
		// iaux++;
		// if (simbolos.get(operando.getIndice()).getTipo().equals("int")) {
		// printWriter.write(" char aux" + iaux + "[1000];\n");
		// printWriter.write(" sprintf(aux" + iaux + ", \"%d\", " +
		// operando.getImagem() + ");\n");
		// printWriter.write(" strcat(" + id.getImagem() + ", aux" + iaux +
		// ");\n");
		// } else if
		// (simbolos.get(operando.getIndice()).getTipo().equals("real")) {
		// printWriter.write(" char aux" + iaux + "[1000];\n");
		// printWriter.write(" sprintf(aux" + iaux + ", \"%lf\", " +
		// operando.getImagem() + ");\n");
		// printWriter.write(" strcat(" + id.getImagem() + ", aux" + iaux +
		// ");\n");
		// } else if
		// (simbolos.get(operando.getIndice()).getTipo().equals("texto"))
		// {
		// printWriter.write(" char aux" + iaux + "[1000];\n");
		// printWriter.write(" sprintf(aux" + iaux + ", \"%s\", " +
		// operando.getImagem() + ");\n");
		// printWriter.write(" strcat(" + id.getImagem() + ", aux" + iaux +
		// ");\n");
		// }
		//
		// } else if (operando.getClasse().equals("CLI")) {
		//
		// printWriter.write(" strcat(" + id.getImagem() + ", " +
		// operando.getImagem() + ");\n");
		// } else if (operando.getClasse().equals("CLR")) {
		// printWriter.write(" strcat(" + id.getImagem() + ", " +
		// operando.getImagem() + ");\n");
		// } else if (operando.getClasse().equals("CLS")) {
		// printWriter.write(" strcat(" + id.getImagem() + ", \"" +
		// operando.getImagem() + "\");\n");
		// }
		// }
		// printWriter.write("\n");
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
			printWriter.write("  fgets(" + id.getImagem() + ", sizeof(" + id.getImagem() + "), stdin");
			// printWriter.write(" scanf(\"%s\", " + id.getImagem());
		}

		printWriter.write(");\n");

		return null;
	}

	// <senao> ::= '{' <list_cmd> '}' | &
	private Object senao(No no) {
		if (!no.getFilhos().isEmpty()) {
			gerar(no.getFilhos().get(1));
		}
		return null;
	}

	// <cond> ::= 'se' <exp_log> '{' <list_cmd> '}' <senao>
	@SuppressWarnings("unchecked")
	private Object cond(No no) {
		printWriter.write("  if(");
		List<Token> operandos = (List<Token>) gerar(no.getFilhos().get(1));

		for (Token operando : operandos) {
			printWriter.write(" " + operando.getImagem());
		}
		printWriter.write(" ){\n  ");
		gerar(no.getFilhos().get(3));
		printWriter.write("  }else{\n  ");
		gerar(no.getFilhos().get(5));
		printWriter.write("  }\n\n");
		return null;
	}

	// <op_rel> ::= '>' | '<' | '>=' | '<=' | '==' | '!='
	private Object opRel(No no) {
		return no.getFilhos().get(0).getToken();
	}

	// <exp_rel> ::= <op_rel> <operan> <operan> | <op_log> '{' <exp_rel> '}' '{'
	// <exp_rel> '}'
	private Object expRel(No no) {

		if (no.getFilhos().size() == 3) {
			Token operan1 = (Token) gerar(no.getFilhos().get(1));
			Token operRel = (Token) gerar(no.getFilhos().get(0));
			Token operan2 = (Token) gerar(no.getFilhos().get(2));

			List<Token> listOperans = new ArrayList<>();

			listOperans.add(operan1);
			listOperans.add(operRel);
			listOperans.add(operan2);
			return listOperans;
		} else if (no.getFilhos().size() == 7) {

		}
		return null;
	}

	// <exp_log> ::= '{' <exp_rel> '}'
	@SuppressWarnings("unchecked")
	private Object expLog(No no) {
		List<Token> listExpLog = (List<Token>) gerar(no.getFilhos().get(1));
		return listExpLog;
	}

	// <laco> ::= 'enquanto' <exp_log> <list_cmd>
	@SuppressWarnings("unchecked")
	private Object laco(No no) {
		printWriter.write("  while(");
		List<Token> operandos = (List<Token>) gerar(no.getFilhos().get(1));

		for (Token operando : operandos) {
			printWriter.write(" " + operando.getImagem());
		}
		printWriter.write(" ){\n  ");
		gerar(no.getFilhos().get(2));
		printWriter.write("  }\n\n");
		return null;
	}

	// <operan> ::= id | cli | clr | cls
	private Object operan(No no) {
		Token id = (Token) no.getFilhos().get(0).getToken();
		if (id.getClasse().equals("ID")) {
			return no.getFilhos().get(0).getToken();
		} else if (id.getClasse().equals("CLI")) {
			return no.getFilhos().get(0).getToken();
		} else if (id.getClasse().equals("CLR")) {
			return no.getFilhos().get(0).getToken();
		} else if (id.getClasse().equals("CLS")) {
			return no.getFilhos().get(0).getToken();
		}
		return null;
	}

	// <op_arit> ::= '+' | '-' | '*' | '/' | '.'
	private Object opArit(No no) {
		Token sinal = (Token) no.getFilhos().get(0).getToken();
		List<Token> sinais = new ArrayList<>();
		sinais.add(sinal);
		return sinais;
	}

	// <exp_arit> ::= <operan> | '{' <op_arit> <exp_arit> <exp_arit> '}'
	@SuppressWarnings("unchecked")
	private Object expArit(No no) {
		if (no.getFilhos().size() == 1) {
			Token operando = (Token) gerar(no.getFilhos().get(0));

			List<Token> operandos = new ArrayList<>();
			operandos.add(operando);
			return operandos;
		} else if (no.getFilhos().size() == 5) {

			List<Token> expArits1 = (List<Token>) gerar(no.getFilhos().get(2));
			List<Token> sinal = (List<Token>) gerar(no.getFilhos().get(1));
			List<Token> expArits2 = (List<Token>) gerar(no.getFilhos().get(3));

			expArits1.addAll(sinal);
			expArits1.addAll(expArits2);

			return expArits1;
		}
		return null;
	}

	// <atrib> ::= '=' id <exp_arit>
	@SuppressWarnings("unchecked")
	private Object atrib(No no) {
		Token id = (Token) no.getFilhos().get(1).getToken();
		String tipo = (String) simbolos.get(id.getIndice()).getTipo();

		if (tipo.equals("int")) {
			List<Token> operandos = (List<Token>) gerar(no.getFilhos().get(2));
			printWriter.write("  " + id.getImagem() + " =");
			for (Token operando : operandos) {
				printWriter.write(" " + operando.getImagem());
			}
			printWriter.write(";\n");
		} else if (tipo.equals("real")) {
			List<Token> operandos = (List<Token>) gerar(no.getFilhos().get(2));
			printWriter.write("  " + id.getImagem() + " =");
			for (Token operando : operandos) {
				printWriter.write(" " + operando.getImagem());
			}
			printWriter.write(";\n");
		} else if (tipo.equals("texto")) {
			printWriter.write("  " + id.getImagem() + "[0] = \'\\0\';\n");
			List<Token> operandos = (List<Token>) gerar(no.getFilhos().get(2));

			int iaux = 0;
			for (Token operando : operandos) {
				if (operando.getClasse().equals("ID")) {

					iaux++;
					if (simbolos.get(operando.getIndice()).getTipo().equals("int")) {
						printWriter.write("  char aux" + iaux + "[1000];\n");
						printWriter.write("  sprintf(aux" + iaux + ", \"%d\", " + operando.getImagem() + ");\n");
						printWriter.write("  strcat(" + id.getImagem() + ", aux" + iaux + ");\n");
					} else if (simbolos.get(operando.getIndice()).getTipo().equals("real")) {
						printWriter.write("  char aux" + iaux + "[1000];\n");
						printWriter.write("  sprintf(aux" + iaux + ", \"%lf\", " + operando.getImagem() + ");\n");
						printWriter.write("  strcat(" + id.getImagem() + ", aux" + iaux + ");\n");
					} else if (simbolos.get(operando.getIndice()).getTipo().equals("texto")) {
						printWriter.write("  char aux" + iaux + "[1000];\n");
						printWriter.write("  sprintf(aux" + iaux + ", \"%s\", " + operando.getImagem() + ");\n");
						printWriter.write("  strcat(" + id.getImagem() + ", aux" + iaux + ");\n");
					}

				} else if (operando.getClasse().equals("CLI")) {

					printWriter.write("  strcat(" + id.getImagem() + ", " + operando.getImagem() + ");\n");
				} else if (operando.getClasse().equals("CLR")) {
					printWriter.write("  strcat(" + id.getImagem() + ", " + operando.getImagem() + ");\n");
				} else if (operando.getClasse().equals("CLS")) {
					printWriter.write("  strcat(" + id.getImagem() + ", \"" + operando.getImagem() + "\");\n");
				}
			}
			printWriter.write("\n");
		}
		return null;
	}

	// <list_id2> ::= <list_id> | &
	private Object listId2(No no) {
		if (!no.getFilhos().isEmpty()) {
			printWriter.write(",");
			gerar(no.getFilhos().get(0));
		}
		return null;
	}

	// <list_id> ::= id <list_id2>
	private Object listId(No no) {
		Token id = (Token) no.getFilhos().get(0).getToken();
		String tipo = (String) simbolos.get(id.getIndice()).getTipo();
		if (tipo.equals("int")) {
			printWriter.write(" " + id.getImagem() + " = 0");
		} else if (tipo.equals("real")) {
			printWriter.write(" " + id.getImagem() + " = 0.0");
		} else if (tipo.equals("texto")) {
			printWriter.write(" " + id.getImagem() + "[1000]");
		}
		gerar(no.getFilhos().get(1));
		return null;
	}

	// <tipo> ::= 'int' | 'real' | 'texto' | 'logico'
	private Object tipo(No no) {
		String tipo = (String) no.getFilhos().get(0).getToken().getImagem();
		if (tipo.equals("int")) {
			printWriter.write("  int");
		} else if (tipo.equals("real")) {
			printWriter.write("  double");
		} else if (tipo.equals("texto")) {
			printWriter.write("  char");
		}

		return null;
	}

	// <decl> ::= <tipo> <list_id>
	private Object decl(No no) {
		gerar(no.getFilhos().get(0));
		gerar(no.getFilhos().get(1));
		printWriter.write(";\n");
		printWriter.write("\n");
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

	public void addErro(String erro) {
		erros.add("Erro de geração: " + erro + ". ");
	}

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

	public PrintWriter getPrintWriter() {
		return printWriter;
	}

	public void setPrintWriter(PrintWriter printWriter) {
		this.printWriter = printWriter;
	}

	public File getArquivo() {
		return arquivo;
	}

	public void setArquivo(File arquivo) {
		this.arquivo = arquivo;
	}

}
