package interpreter;

import java.util.ArrayList;
import java.util.List;

public class AnalisadorSemantico {

	private No raiz;
	private List<Simbolo> simbolos;
	private List<String> erros = new ArrayList<String>();

	public AnalisadorSemantico(No raiz, List<Simbolo> simbolos) {
		this.setRaiz(raiz);
		this.setSimbolos(simbolos);
	}

	public void analisar() {
		analisar(raiz);

//		System.out.println("\nTabela de Simbolos depois de analisar:");
//		for (int i = 0; i < simbolos.size(); i++) {
//			System.out.print(" " + i);
//			System.out.print(" " + simbolos.get(i).getImagem());
//			System.out.print(" " + simbolos.get(i).getTipo());
//			System.out.println(" " + simbolos.get(i).getValor());
//		}
	}

	private Object analisar(No no) {
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
		} else if (no.getTipo().equals("NO_OP_ARIT")) {
			return opArit(no);
		} else if (no.getTipo().equals("NO_EXP_ARIT")) {
			return expArit(no);
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
		} else if (no.getTipo().equals("NO_OP_LOG")) {
			return opLog(no);
		} else if (no.getTipo().equals("NO_SENAO")) {
			return senao(no);
		} else if (no.getTipo().equals("NO_LACO")) {
			return laco(no);
		} else {
			addErro("NO desconhecido = " + no.getTipo());
			return null;
		}
	}

	// <laco> ::= 'enquanto' <exp_log> <list_cmd>
	private Object laco(No no) {
		analisar(no.getFilhos().get(1));
		analisar(no.getFilhos().get(2));
		return null;
	}

	// <senao> ::= '{' <list_cmd> '}' | &
	private Object senao(No no) {
		if (!no.getFilhos().isEmpty()) {
			analisar(no.getFilhos().get(1));
		}
		return null;
	}

	// <op_log> ::= '&&' | '||'
	private Object opLog(No no) {
		return no.getFilhos().get(0).getToken().getImagem();
	}

	// <op_rel> ::= '>' | '<' | '>=' | '<=' | '==' | '!='
	private Object opRel(No no) {
		return no.getFilhos().get(0).getToken().getImagem();
	}

	// <exp_rel> ::= <op_rel> <operan> <operan> | <op_log> '{' <exp_rel> '}' '{'
	// <exp_rel> '}'
	private Object expRel(No no) {
		if (no.getFilhos().get(0).getTipo().equals("NO_OP_REL")) {

			// System.out.println("Operando 1 --> " +
			// no.getFilhos().get(1).getFilhos().get(0).getToken());
			//
			// System.out.println("Operando 2 --> " +
			// no.getFilhos().get(2).getFilhos().get(0).getToken());

			Token op1 = (Token) no.getFilhos().get(1).getFilhos().get(0).getToken();
			Token op2 = (Token) no.getFilhos().get(2).getFilhos().get(0).getToken();

			if (op1.getClasse().equals("ID")) {
				String op1Tipo = simbolos.get(op1.getIndice()).getTipo();
				if (op1Tipo == null) {
					addErro("ID não declarado " + op1);
				} else if (op2.getClasse().equals("ID")) {
					String op2Tipo = simbolos.get(op2.getIndice()).getTipo();
					if (op2Tipo == null) {
						addErro("ID não declarado " + op2);
					} else if (!op1Tipo.equals(op2Tipo)) {
						addErro("O ID \"" + op1.getImagem() + "\" possui tipo \"" + op1Tipo
								+ "\" que é incompatível com o ID \"" + op2.getImagem() + "\" que possui o tipo \""
								+ op2Tipo + "\"");
					}
				} else if (op2.getClasse().equals("CLI")) {
					if (!op1Tipo.equals("int")) {
						addErro("O ID \"" + op1.getImagem() + "\" possui tipo \"" + op1Tipo
								+ "\" que é incompatível com o ID \"" + op2.getImagem()
								+ "\" que possui o tipo \"int\"");
					}
				} else if (op2.getClasse().equals("CLR")) {
					if (!op1Tipo.equals("real")) {
						addErro("O ID \"" + op1.getImagem() + "\" possui tipo \"" + op1Tipo
								+ "\" que é incompatível com o ID \"" + op2.getImagem()
								+ "\" que possui o tipo \"real\"");
					}
				} else if (op2.getClasse().equals("CLS")) {
					if (!op1Tipo.equals("texto")) {
						addErro("O ID \"" + op1.getImagem() + "\" possui tipo \"" + op1Tipo
								+ "\" que é incompatível com o ID \"" + op2.getImagem()
								+ "\" que possui o tipo \"texto\"");
					}
				}
			} else if (op1.getClasse().equals("CLI")) {
				if (op2.getClasse().equals("ID")) {
					String op2Tipo = simbolos.get(op2.getIndice()).getTipo();
					if (op2Tipo == null) {
						addErro("ID não declarado " + op2);
					} else if (!op2Tipo.equals("int")) {
						addErro("O ID \"" + op2.getImagem() + "\" possui tipo \"" + op2Tipo
								+ "\" que é incompatível com o ID \"" + op1.getImagem()
								+ "\" que possui o tipo \"int\"");
					}
				} else if (op2.getClasse().equals("CLR")) {
					addErro("O ID \"" + op1.getImagem() + "\" possui tipo \"int\" que é incompatível com o ID \""
							+ op2.getImagem() + "\" que possui o tipo \"real\"");
				} else if (op2.getClasse().equals("CLS")) {
					addErro("O ID \"" + op1.getImagem() + "\" possui tipo \"int\" que é incompatível com o ID \""
							+ op2.getImagem() + "\" que possui o tipo \"texto\"");
				}
			} else if (op1.getClasse().equals("CLR")) {
				if (op2.getClasse().equals("ID")) {
					String op2Tipo = simbolos.get(op2.getIndice()).getTipo();
					if (op2Tipo == null) {
						addErro("ID não declarado " + op2);
					} else if (!op2Tipo.equals("real")) {
						addErro("O ID \"" + op2.getImagem() + "\" possui tipo \"" + op2Tipo
								+ "\" que é incompatível com o ID \"" + op1.getImagem()
								+ "\" que possui o tipo \"real\"");
					}
				} else if (op2.getClasse().equals("CLI")) {
					addErro("O ID \"" + op1.getImagem() + "\" possui tipo \"real\" que é incompatível com o ID \""
							+ op2.getImagem() + "\" que possui o tipo \"int\"");
				} else if (op2.getClasse().equals("CLS")) {
					addErro("O ID \"" + op1.getImagem() + "\" possui tipo \"real\" que é incompatível com o ID \""
							+ op2.getImagem() + "\" que possui o tipo \"texto\"");
				}
			} else if (op1.getClasse().equals("CLS")) {
				if (op2.getClasse().equals("ID")) {
					String op2Tipo = simbolos.get(op2.getIndice()).getTipo();
					if (op2Tipo == null) {
						addErro("ID não declarado " + op2);
					} else if (!op2Tipo.equals("texto")) {
						addErro("O ID \"" + op2.getImagem() + "\" possui tipo \"" + op2Tipo
								+ "\" que é incompatível com o ID \"" + op1.getImagem()
								+ "\" que possui o tipo \"texto\"");
					}
				} else if (op2.getClasse().equals("CLI")) {
					addErro("O ID \"" + op1.getImagem() + "\" possui tipo \"texto\" que é incompatível com o ID \""
							+ op2.getImagem() + "\" que possui o tipo \"int\"");
				} else if (op2.getClasse().equals("CLR")) {
					addErro("O ID \"" + op1.getImagem() + "\" possui tipo \"texto\" que é incompatível com o ID \""
							+ op2.getImagem() + "\" que possui o tipo \"real\"");
				}
			}
		} else if (no.getFilhos().get(0).getTipo().equals("NO_OP_LOG")) {
			analisar(no.getFilhos().get(2));
			analisar(no.getFilhos().get(5));
		}

		return null;
	}

	// <exp_log> ::= '{' <exp_rel> '}'
	private Object expLog(No no) {
		analisar(no.getFilhos().get(1));
		return null;
	}

	// <cond> ::= 'se' <exp_log> '{' <list_cmd> '}' <senao>
	private Object cond(No no) {
		analisar(no.getFilhos().get(1));
		analisar(no.getFilhos().get(3));
		analisar(no.getFilhos().get(5));
		return null;
	}

	// <atrib> ::= '=' id <exp_arit>
	@SuppressWarnings("unchecked")
	private Object atrib(No no) {

		// { = a1 { + a2 { * 10 a3 } } }

		Token id = no.getFilhos().get(1).getToken();

		String tipoId = simbolos.get(id.getIndice()).getTipo();

		// System.out.println("Passei tipo id " + tipoId);

		if (tipoId == null) {
			addErro("O ID \"" + id.getImagem() + "\" não foi declarado na Linha = \"" + id.getLinha()
					+ "\" e Coluna = \"" + id.getColuna() + "\"");
		} else {
			List<Token> operandos = (List<Token>) analisar(no.getFilhos().get(2));

			for (Token operando : operandos) {
				if (operando.getClasse().equals("CLS")) {
					// System.out.println("Passei" + operando);

					if (!tipoId.equals("texto")) {
						addErro("A variável \"" + simbolos.get(id.getIndice()).getImagem() + "\" tem Tipo \"" + tipoId
								+ "\" que é incompatível com \"" + operando.getImagem()
								+ "\" que é do tipo texto, na Linha = \"" + operando.getLinha() + "\" e Coluna = \""
								+ operando.getColuna() + "\"");
					}
				} else if (operando.getClasse().equals("CLI")) {
					// System.out.println("Passei" + operando);

					if (!tipoId.equals("int")) {
						addErro("A variável \"" + simbolos.get(id.getIndice()).getImagem() + "\" tem Tipo \"" + tipoId
								+ "\" que é incompatível com \"" + operando.getImagem()
								+ "\" que é do tipo int, na Linha = \"" + operando.getLinha() + "\" e Coluna = \""
								+ operando.getColuna() + "\"");
					}
				} else if (operando.getClasse().equals("CLR")) {
					// System.out.println("Passei" + operando);

					if (!tipoId.equals("real")) {
						addErro("A variável \"" + simbolos.get(id.getIndice()).getImagem() + "\" tem Tipo \"" + tipoId
								+ "\" que é incompatível com \"" + operando.getImagem()
								+ "\" que é do tipo real, na Linha = \"" + operando.getLinha() + "\" e Coluna = \""
								+ operando.getColuna() + "\"");
					}

				} else if (operando.getClasse().equals("ID")) {
					String tipo = simbolos.get(operando.getIndice()).getTipo();

					if (tipo == null) {
						addErro("O ID \"" + operando.getImagem() + "\" não foi declarado na Linha = \""
								+ operando.getLinha() + "\" e Coluna = \"" + operando.getColuna() + "\"");
					} else if (!tipo.equals(tipoId) && !tipoId.equals("texto")) {
						addErro("A variável \"" + simbolos.get(id.getIndice()).getImagem() + "\" tem Tipo \"" + tipoId
								+ "\" que é incompatível com \"" + operando.getImagem() + "\" que é do tipo \"" + tipo
								+ "\" na Linha = \"" + operando.getLinha() + "\" e Coluna = \"" + operando.getColuna()
								+ "\"");
					}
				}
			}
		}

		return null;
	}

	// <leitura> ::= 'le' id
	private Object leitura(No no) {
		Token id = no.getFilhos().get(1).getToken();

		if (simbolos.get(id.getIndice()).getTipo() == null) {
			addErro("ID não declarado = " + id.getImagem());
		} else {
			analisar(no.getFilhos().get(1));
		}

		return null;
	}

	// <op_arit> ::= '+' | '-' | '*' | '/' | '.'
	private Object opArit(No no) {
		return no.getFilhos().get(0).getToken().getImagem();
	}

	// <operan> ::= id | cli | clr | cls
	private Object operan(No no) {
		return no.getFilhos().get(0).getToken();
	}

	// <exp_arit> ::= <operan> | '{' <op_arit> <exp_arit> <exp_arit> '}'
	@SuppressWarnings("unchecked")
	private Object expArit(No no) {

		if (no.getFilhos().size() == 1) {
			Token operando = (Token) analisar(no.getFilhos().get(0));

			List<Token> operandos = new ArrayList<>();
			operandos.add(operando);
			return operandos;
		} else if (no.getFilhos().size() == 5) {
			List<Token> expArits1 = (List<Token>) analisar(no.getFilhos().get(2));
			List<Token> expArits2 = (List<Token>) analisar(no.getFilhos().get(3));

			expArits1.addAll(expArits2);
			return expArits1;
		}

		return null;
	}

	// <escrita> ::= 'mostra' <exp_arit>
	private Object escrita(No no) {
		analisar(no.getFilhos().get(1));
		return null;
	}

	// <list_id2> ::= <list_id> |
	private Object listId2(No no) {
		if (no.getFilhos().isEmpty()) {
			return new ArrayList<Token>();
		} else {
			return analisar(no.getFilhos().get(0));
		}
	}

	// <list_id> ::= id <list_id2>
	@SuppressWarnings("unchecked")
	private Object listId(No no) {
		Token id = no.getFilhos().get(0).getToken();
		List<Token> listId2 = (List<Token>) analisar(no.getFilhos().get(1));
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
		String tipo = (String) analisar(no.getFilhos().get(0));
		List<Token> listId = (List<Token>) analisar(no.getFilhos().get(1));

		for (Token id : listId) {
			if (simbolos.get(id.getIndice()).getTipo() != null) {
				addErro("ID redeclarado = " + id);
			} else {
				simbolos.get(id.getIndice()).setTipo(tipo);
			}
		}

		return null;
	}

	// <cmd_inter> ::= <decl> | <atrib> | <laco> | <cond> | <escrita> |
	// <leitura>
	private Object cmdInter(No no) {
		analisar(no.getFilhos().get(0));
		return null;
	}

	// <cmd> ::= '{' <cmd_inter> '}'
	private Object cmd(No no) {
		analisar(no.getFilhos().get(1));
		return null;
	}

	// <list_cmd> ::= <cmd> <list_cmd> | &
	private Object listCmd(No no) {
		if (!no.getFilhos().isEmpty()) {
			analisar(no.getFilhos().get(0));
			analisar(no.getFilhos().get(1));
		}
		return null;
	}

	// FIM DAS REGRAS

	public void addErro(String erro) {
		erros.add("Erro semântico: " + erro + ". ");
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

}
