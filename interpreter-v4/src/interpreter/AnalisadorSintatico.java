package interpreter;

import java.util.ArrayList;
import java.util.List;

public class AnalisadorSintatico {

	private List<Token> tokens;
	private List<String> erros = new ArrayList<String>();
	private int ptoken;
	private Token token;
	private No raiz;

	public AnalisadorSintatico(List<Token> tokens) {
		this.tokens = tokens;
	}

	private void letoken() {
		token = tokens.get(ptoken++);
	}

	public void addErro(String erro) {
		erros.add("Erro sintático: " + erro);
	}

	public void analisar() {
		ptoken = 0;
		letoken();
		raiz = listCmd();

		if (!token.getClasse().equals("$")) {
			addErro("Esperado EOF --> " + token);
		}

		if (erros.isEmpty()) {
			// System.out.println("\nSUCESSO!");
		} else {
			System.out.println("\nERRO(S): ");
			for (String erro : erros) {
				System.out.println(erro);
			}
		}
	}

	// <list_cmd> ::= <cmd> <list_cmd> |
	private No listCmd() {
		No no = new No("NO_LIST_CMD");
		if (token.getImagem().equals("{")) {
			no.addFilho(cmd());
			no.addFilho(listCmd());
		} // else vazio
		return no;
	}

	// <cmd> ::= '{' <cmd_inter> '}'
	private No cmd() {
		No no = new No("NO_CMD");
		if (token.getImagem().equals("{")) {
			no.addFilho(new No(token));
			letoken();
			no.addFilho(cmdInter());
			if (token.getImagem().equals("}")) {
				no.addFilho(new No(token));
				letoken();
			} else {
				addErro("Esperado '}'--> " + token);
			}
		} else {
			addErro("Esperado '{'--> " + token);
		}
		return no;
	}

	// <cmd_inter> ::= <decl> | <atrib> | <laco> | <cond> | <escrita> |
	// <leitura>
	private No cmdInter() {
		No no = new No("NO_CMD_INTER");
		if (token.getImagem().equals("int") || token.getImagem().equals("real") || token.getImagem().equals("texto")
				|| token.getImagem().equals("logico")) {
			no.addFilho(decl());
		} else if (token.getImagem().equals("=")) {
			no.addFilho(atrib());
		} else if (token.getImagem().equals("enquanto")) {
			no.addFilho(laco());
		} else if (token.getImagem().equals("se")) {
			no.addFilho(cond());
		} else if (token.getImagem().equals("mostra")) {
			no.addFilho(escrita());
		} else if (token.getImagem().equals("le")) {
			no.addFilho(leitura());
		} else {
			addErro("Esperado 'int' ou 'real' ou 'texto' ou 'logico' ou '=' ou 'enquanto' ou 'se' ou 'mostra' ou 'le' --> "
					+ token);
		}
		return no;
	}

	// <decl> ::= <tipo> <list_id>
	private No decl() {
		No no = new No("NO_DECL");
		no.addFilho(tipo());
		no.addFilho(listId());
		return no;
	}

	// <tipo> ::= 'int' | 'real' | 'texto' | 'logico'
	private No tipo() {
		No no = new No("NO_TIPO");
		if (token.getImagem().equals("int")) {
			no.addFilho(new No(token));
			letoken();
		} else if (token.getImagem().equals("real")) {
			no.addFilho(new No(token));
			letoken();
		} else if (token.getImagem().equals("texto")) {
			no.addFilho(new No(token));
			letoken();
		} else if (token.getImagem().equals("logico")) {
			no.addFilho(new No(token));
			letoken();
		} else {
			addErro("Esperado 'int' ou 'real' ou 'texto' ou 'logico' --> " + token);
		}
		return no;
	}

	// <list_id> ::= id <list_id2>
	private No listId() {
		// AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		No no = new No("NO_LIST_ID");
		if (token.getClasse().equals("ID")) {
			no.addFilho(new No(token));
			letoken();
			no.addFilho(listId2());
		} else {
			addErro("Esperado 'id' --> " + token);
		}
		return no;
	}

	// <list_id2> ::= <list_id> |
	private No listId2() {
		No no = new No("NO_LIST_ID_2");
		if (token.getClasse().equals("ID")) {
			no.addFilho(listId());
		} // else vazio
		return no;
	}

	// <leitura> ::= 'le' id
	private No leitura() {
		No no = new No("NO_LEITURA");
		if (token.getImagem().equals("le")) {
			no.addFilho(new No(token));
			letoken();
			if (token.getClasse().equals("ID")) {
				no.addFilho(new No(token));
				letoken();
			} else {
				addErro("Esperado 'id' --> " + token);
			}
		}
		return no;
	}

	// <escrita> ::= 'mostra' <exp_arit>
	private No escrita() {
		No no = new No("NO_ESCRITA");
		if (token.getImagem().equals("mostra")) {
			no.addFilho(new No(token));
			letoken();
			no.addFilho(expArit());
		}
		return no;
	}

	// <cond> ::= 'se' <exp_log> '{' <list_cmd> '}' <senao>
	private No cond() {
		No no = new No("NO_COND");
		if (token.getImagem().equals("se")) {
			no.addFilho(new No(token));
			letoken();
			no.addFilho(expLog());
			if (token.getImagem().equals("{")) {
				no.addFilho(new No(token));
				letoken();
				no.addFilho(listCmd());
				if (token.getImagem().equals("}")) {
					no.addFilho(new No(token));
					letoken();
					no.addFilho(senao());
				} else {
					addErro("Esperado '}' --> " + token);
				}
			} else {
				addErro("Esperado '{' --> " + token);
			}
		} else {
			addErro("Esperado 'se' --> " + token);
		}
		return no;
	}

	// <senao> ::= '{' <list_cmd> '}' |
	private No senao() {
		No no = new No("NO_SENAO");
		if (token.getImagem().equals("{")) {
			no.addFilho(new No(token));
			letoken();
			no.addFilho(listCmd());
			if (token.getImagem().equals("}")) {
				no.addFilho(new No(token));
				letoken();
			}
		} // else vazio
		return no;
	}

	// <laco> ::= 'enquanto' <exp_log> <list_cmd>
	private No laco() {
		No no = new No("NO_LACO");
		if (token.getImagem().equals("enquanto")) {
			no.addFilho(new No(token));
			letoken();
			no.addFilho(expLog());
			no.addFilho(listCmd());
		} else {
			addErro("Esperado 'enquanto' --> " + token);
		}
		return no;
	}

	// <exp_log> ::= '{' <exp_rel> '}'
	private No expLog() {
		No no = new No("NO_EXP_LOG");
		if (token.getImagem().equals("{")) {
			no.addFilho(new No(token));
			letoken();
			no.addFilho(expRel());
			if (token.getImagem().equals("}")) {
				no.addFilho(new No(token));
				letoken();
			} else {
				addErro("Esperado '}' --> " + token);
			}
		} else {
			addErro("Esperado '{' --> " + token);
		}
		return no;
	}

	// <exp_rel> ::= <op_rel> <operan> <operan> | <op_log> '{' <exp_rel> '}' '{'
	// <exp_rel> '}'
	private No expRel() {
		No no = new No("NO_EXP_REL");
		if (token.getImagem().equals(">") || token.getImagem().equals("<") || token.getImagem().equals(">=")
				|| token.getImagem().equals("<=") || token.getImagem().equals("==") || token.getImagem().equals("!=")) {
			no.addFilho(opRel());
			no.addFilho(operan());
			no.addFilho(operan());
		} else if (token.getImagem().equals("&&") || token.getImagem().equals("||")) {
			no.addFilho(opLog());
			if (token.getImagem().equals("{")) {
				no.addFilho(new No(token));
				letoken();
				no.addFilho(expRel());
				if (token.getImagem().equals("}")) {
					no.addFilho(new No(token));
					letoken();
					if (token.getImagem().equals("{")) {
						no.addFilho(new No(token));
						letoken();
						no.addFilho(expRel());
						if (token.getImagem().equals("}")) {
							no.addFilho(new No(token));
							letoken();
						} else {
							addErro("Esperado '}' --> " + token);
						}
					} else {
						addErro("Esperado '{' --> " + token);
					}
				} else {
					addErro("Esperado '}' --> " + token);
				}
			} else {
				addErro("Esperado '{' --> " + token);
			}
		} else {
			addErro("Esperado Operador Lógico ou Relacional --> " + token);
		}
		return no;
	}

	// <op_log> ::= '&&' | '||'
	private No opLog() {
		No no = new No("NO_OP_LOG");
		if (token.getImagem().equals("&&")) {
			no.addFilho(new No(token));
			letoken();
		} else if (token.getImagem().equals("||")) {
			no.addFilho(new No(token));
			letoken();
		} else {
			addErro("Esperado '&&' ou '||' --> " + token);
		}
		return no;
	}

	// <op_rel> ::= '>' | '<' | '>=' | '<=' | '==' | '!='
	private No opRel() {
		No no = new No("NO_OP_REL");
		if (token.getImagem().equals(">")) {
			no.addFilho(new No(token));
			letoken();
		} else if (token.getImagem().equals("<")) {
			no.addFilho(new No(token));
			letoken();
		} else if (token.getImagem().equals(">=")) {
			no.addFilho(new No(token));
			letoken();
		} else if (token.getImagem().equals("<=")) {
			no.addFilho(new No(token));
			letoken();
		} else if (token.getImagem().equals("==")) {
			no.addFilho(new No(token));
			letoken();
		} else if (token.getImagem().equals("!=")) {
			no.addFilho(new No(token));
			letoken();
		} else {
			addErro("Esperado '>' ou '<' ou '>=' ou '<=' ou '==' ou '!=' --> " + token);
		}
		return no;
	}

	// <atrib> ::= '=' id <exp_arit>
	private No atrib() {
		No no = new No("NO_ATRIB");
		if (token.getImagem().equals("=")) {
			no.addFilho(new No(token));
			letoken();
			if (token.getClasse().equals("ID")) {
				no.addFilho(new No(token));
				letoken();
				no.addFilho(expArit());
			} else {
				addErro("Esperado 'id' --> " + token);
			}
		} else {
			addErro("Esperado '=' --> " + token);
		}
		return no;
	}

	// <exp_arit> ::= <operan> | '{' <op_arit> <exp_arit> <exp_arit> '}'
	private No expArit() {
		No no = new No("NO_EXP_ARIT");
		if (token.getClasse().equals("ID") || token.getClasse().equals("CLI") || token.getClasse().equals("CLR")
				|| token.getClasse().equals("CLS")) {
			no.addFilho(operan());
		} else if (token.getImagem().equals("{")) {
			no.addFilho(new No(token));
			letoken();
			no.addFilho(opArit());
			no.addFilho(expArit());
			no.addFilho(expArit());
			if (token.getImagem().equals("}")) {
				no.addFilho(new No(token));
				letoken();
			} else {
				addErro("Esperado '}' --> " + token);
			}
		} else {
			addErro("Esperado 'id' ou 'cli' ou 'clr' ou 'cls' ou '{' --> " + token);
		}
		return no;
	}

	// <op_arit> ::= '+' | '-' | '*' | '/' | '.'
	private No opArit() {
		No no = new No("NO_OP_ARIT");
		if (token.getImagem().equals("+")) {
			no.addFilho(new No(token));
			letoken();
		} else if (token.getImagem().equals("-")) {
			no.addFilho(new No(token));
			letoken();
		} else if (token.getImagem().equals("*")) {
			no.addFilho(new No(token));
			letoken();
		} else if (token.getImagem().equals("/")) {
			no.addFilho(new No(token));
			letoken();
		} else if (token.getImagem().equals(".")) {
			no.addFilho(new No(token));
			letoken();
		} else {
			addErro("Esperado '+' ou '-' ou '*' ou '/' ou '.' --> " + token);
		}
		return no;
	}

	// <operan> ::= id | cli | clr | cls
	private No operan() {
		No no = new No("NO_OPERAN");
		if (token.getClasse().equals("ID")) {
			no.addFilho(new No(token));
			letoken();
		} else if (token.getClasse().equals("CLI")) {
			no.addFilho(new No(token));
			letoken();
		} else if (token.getClasse().equals("CLR")) {
			no.addFilho(new No(token));
			letoken();
		} else if (token.getClasse().equals("CLS")) {
			no.addFilho(new No(token));
			letoken();
		} else {
			addErro("Esperado 'id' ou 'cli' ou 'clr' ou 'cls' --> " + token);
		}
		return no;
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

	public void mostraArvore() {
		mostraNo("", raiz);
	}

	private void mostraNo(String espaco, No no) {
		System.out.println(espaco + no);
		for (No filho : no.getFilhos()) {
			mostraNo(espaco + "..", filho);
		}
	}

	public No getRaiz() {
		return raiz;
	}

}
