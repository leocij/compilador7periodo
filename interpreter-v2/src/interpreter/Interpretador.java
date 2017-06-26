package interpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Interpretador {

	private No raiz;
	private List<Simbolo> simbolos;
	private List<String> erros = new ArrayList<String>();
	private Scanner sc;

	public Interpretador(No raiz, List<Simbolo> simbolos) {
		this.setRaiz(raiz);
		this.setSimbolos(simbolos);
	}

	public void addErro(String erro) {
		erros.add("Erro de interpretação: " + erro + ". ");
	}

	public void executar() {
		try {
			executar(raiz);

			// System.out.println("\nTabela de Simbolos depois de
			// interpretar:");
			// for (int i = 0; i < simbolos.size(); i++) {
			// System.out.print(" " + i);
			// System.out.print(" " + simbolos.get(i).getImagem());
			// System.out.print(" " + simbolos.get(i).getEscopo());
			// System.out.print(" " + simbolos.get(i).getTipo());
			// System.out.print(" " + simbolos.get(i).getValor());
			// System.out.println(" " + simbolos.get(i).getFlag());
			// }

		} catch (Exception ex) {
			throw ex;
		}

	}

	private Object executar(No no) {
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
		} else if (no.getTipo().equals("NO_OPERAN")) {
			return operan(no);
		} else if (no.getTipo().equals("NO_OP_ARIT")) {
			return opArit(no);
		} else if (no.getTipo().equals("NO_ESCRITA")) {
			return escrita(no);
		} else if (no.getTipo().equals("NO_LACO")) {
			return laco(no);
		} else if (no.getTipo().equals("NO_EXP_LOG")) {
			return expLog(no);
		} else if (no.getTipo().equals("NO_EXP_REL")) {
			return expRel(no);
		} else if (no.getTipo().equals("NO_OP_REL")) {
			return opRel(no);
		} else if (no.getTipo().equals("NO_OP_LOG")) {
			return opLog(no);
		} else if (no.getTipo().equals("NO_COND")) {
			return cond(no);
		} else if (no.getTipo().equals("NO_SENAO")) {
			return senao(no);
		} else if (no.getTipo().equals("NO_LEITURA")) {
			return leitura(no);
		} else {
			addErro("Erro: NO desconhecido = " + no.getTipo());
			return null;
		}
	}

	// <leitura> ::= 'le' id
	private Object leitura(No no) {
		sc = new Scanner(System.in);

		Token id = no.getFilhos().get(1).getToken();

		String tipo = simbolos.get(id.getIndice()).getTipo();

		try{
		if (tipo.equals("int")) {
			Object entrada = (Object) sc.nextInt();
			sc.nextLine();
			setValor(id, entrada);
		} else if (tipo.equals("real")) {
			Object entrada = (Object) sc.nextDouble();
			sc.nextLine();
			setValor(id, entrada);
		} else if (tipo.equals("texto")) {
			Object entrada = (Object) sc.nextLine();
			sc.nextLine();
			setValor(id, entrada);
		}
		}catch(Exception ex){
			System.out.println("Atenção! A entrada deve ser do tipo: " + tipo);
			System.out.println("Programa finalizado pelo sistema :)");
			//throw ex;
			System.exit(0);
		}

		// sc.close();
		return null;
	}

	// <senao> ::= '{' <list_cmd> '}' |
	private Object senao(No no) {
		executar(no.getFilhos().get(1));
		return null;
	}

	// <cond> ::= 'se' <exp_log> '{' <list_cmd> '}' <senao>
	private Object cond(No no) {
		boolean retorno = (boolean) executar(no.getFilhos().get(1));

		if (retorno == true) {
			executar(no.getFilhos().get(3));
		} else {
			executar(no.getFilhos().get(5));
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

			Object sinal = executar(no.getFilhos().get(0));

			Object operan1 = (Object) executar(no.getFilhos().get(1));
			Object operan2 = (Object) executar(no.getFilhos().get(2));

			if (sinal.equals(">")) {
				if (operan1 instanceof Integer && operan2 instanceof Integer) {
					if (((Integer) operan1).intValue() > ((Integer) operan2).intValue()) {
						return true;
					} else {
						return false;
					}
				}
			} else if (sinal.equals("<")) {
				if (operan1 instanceof Integer && operan2 instanceof Integer) {
					if (((Integer) operan1).intValue() < ((Integer) operan2).intValue()) {
						return true;
					} else {
						return false;
					}
				}
			} else if (sinal.equals(">=")) {
				if (operan1 instanceof Integer && operan2 instanceof Integer) {
					if (((Integer) operan1).intValue() >= ((Integer) operan2).intValue()) {
						return true;
					} else {
						return false;
					}
				}
			} else if (sinal.equals("<=")) {
				if (operan1 instanceof Integer && operan2 instanceof Integer) {
					if (((Integer) operan1).intValue() <= ((Integer) operan2).intValue()) {
						return true;
					} else {
						return false;
					}
				}
			} else if (sinal.equals("==")) {
				if (operan1 instanceof Integer && operan2 instanceof Integer) {
					if (((Integer) operan1).intValue() == ((Integer) operan2).intValue()) {
						return true;
					} else {
						return false;
					}
				}
			} else if (sinal.equals("!=")) {
				if (operan1 instanceof Integer && operan2 instanceof Integer) {
					if (((Integer) operan1).intValue() != ((Integer) operan2).intValue()) {
						return true;
					} else {
						return false;
					}
				}
			}

		} else if (no.getFilhos().get(0).getTipo().equals("NO_OP_LOG")) {
			executar(no.getFilhos().get(2));
			executar(no.getFilhos().get(5));
		}

		return null;
	}

	// <exp_log> ::= '{' <exp_rel> '}'
	private Object expLog(No no) {
		return executar(no.getFilhos().get(1));
	}

	// <laco> ::= 'enquanto' <exp_log> <list_cmd>
	private Object laco(No no) {
		boolean retorno = (boolean) executar(no.getFilhos().get(1));

		while (retorno == true) {
			executar(no.getFilhos().get(2));
			retorno = (boolean) executar(no.getFilhos().get(1));
		}

		return null;
	}

	// <escrita> ::= 'mostra' <exp_arit>
	private Object escrita(No no) {

		// System.out.print("\n\n\n========== SAIDA ==========\n\n\n");
		System.out.print(executar(no.getFilhos().get(1)));
		// System.out.print("\n\n\n========== SAIDA ==========\n\n\n");

		return null;
	}

	// <op_arit> ::= '+' | '-' | '*' | '/' | '.'
	private Object opArit(No no) {
		return no.getFilhos().get(0).getToken().getImagem();
	}

	// <operan> ::= id | cli | clr | cls
	private Object operan(No no) {
		Token token = (Token) no.getFilhos().get(0).getToken();
		// System.out.println(token);
		if (token.getClasse().equals("ID")) {
			return getValor(token);
		} else if (token.getClasse().equals("CLI")) {
			return Integer.parseInt(token.getImagem());
		} else if (token.getClasse().equals("CLR")) {
			return Double.parseDouble(token.getImagem());
		} else if (token.getClasse().equals("CLS")) {
			return token.getImagem();
		}

		return null;
	}

	// <exp_arit> ::= <operan> | '{' <op_arit> <exp_arit> <exp_arit> '}'
	private Object expArit(No no) {

		if (no.getFilhos().size() == 1) {
			return executar(no.getFilhos().get(0));
		} else if (no.getFilhos().size() == 5) {

			Object operador = executar(no.getFilhos().get(1));

			Object valor1 = (Object) executar(no.getFilhos().get(2));
			Object valor2 = (Object) executar(no.getFilhos().get(3));

			if (operador.equals("+")) {
				if (valor1 instanceof Integer && valor2 instanceof Integer) {
					return ((Integer) valor1).intValue() + ((Integer) valor2).intValue();
				} else if (valor1 instanceof Double && valor2 instanceof Double) {
					return ((Double) valor1).doubleValue() + ((Double) valor2).doubleValue();
				}
			} else if (operador.equals("-")) {
				if (valor1 instanceof Integer && valor2 instanceof Integer) {
					return ((Integer) valor1).intValue() - ((Integer) valor2).intValue();
				} else if (valor1 instanceof Double && valor2 instanceof Double) {
					return ((Double) valor1).doubleValue() - ((Double) valor2).doubleValue();
				}
			} else if (operador.equals("*")) {
				if (valor1 instanceof Integer && valor2 instanceof Integer) {
					return ((Integer) valor1).intValue() * ((Integer) valor2).intValue();
				} else if (valor1 instanceof Double && valor2 instanceof Double) {
					return ((Double) valor1).doubleValue() * ((Double) valor2).doubleValue();
				}
			} else if (operador.equals("/")) {
				if (valor1 instanceof Integer && valor2 instanceof Integer) {
					return ((Integer) valor1).intValue() / ((Integer) valor2).intValue();
				} else if (valor1 instanceof Double && valor2 instanceof Double) {
					return ((Double) valor1).doubleValue() / ((Double) valor2).doubleValue();
				}
			} else if (operador.equals(".")) {
				if (valor1 instanceof String || valor2 instanceof String) {
					valor1 = valor1.toString();
					valor2 = valor2.toString();
					return ((String) valor1) + ((String) valor2);
				}
			}

			return null;
		}

		return null;
	}

	// <atrib> ::= '=' id <exp_arit>
	private Object atrib(No no) {
		// { = a1 { + a2 10 } }

		Token id = no.getFilhos().get(1).getToken();

		Object valorExpArit = executar(no.getFilhos().get(2));

		setValor(id, valorExpArit);

		// System.out.println("Resultado = " + getValor(id));

		return null;
	}

	// <list_id2> ::= <list_id> |
	private Object listId2(No no) {
		if (no.getFilhos().isEmpty()) {
			return new ArrayList<Token>();
		} else {
			return executar(no.getFilhos().get(0));
		}
	}

	// <list_id> ::= id <list_id2>
	@SuppressWarnings("unchecked")
	private Object listId(No no) {
		Token id = no.getFilhos().get(0).getToken();
		List<Token> listId2 = (List<Token>) executar(no.getFilhos().get(1));
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
		List<Token> listId = (List<Token>) executar(no.getFilhos().get(1));

		for (Token id : listId) {

			Object valorPadrao = getValorPadrao(simbolos.get(id.getIndice()).getTipo());
			simbolos.get(id.getIndice()).setValor(valorPadrao);

		}

		return null;
	}

	private Object getValorPadrao(String tipo) {
		if (tipo.equals("int")) {
			return 0;
		} else if (tipo.equals("real")) {
			return 0.0;
		} else if (tipo.equals("texto")) {
			return "";
		}
		return null;
	}

	// <cmd_inter> ::= <decl> | <atrib> | <laco> | <cond> | <escrita> |
	// <leitura>
	private Object cmdInter(No no) {
		executar(no.getFilhos().get(0));
		return null;
	}

	// <cmd> ::= '{' <cmd_inter> '}'
	private Object cmd(No no) {
		executar(no.getFilhos().get(1));
		return null;
	}

	// <list_cmd> ::= <cmd> <list_cmd> | &
	private Object listCmd(No no) {
		if (!no.getFilhos().isEmpty()) {
			executar(no.getFilhos().get(0));
			executar(no.getFilhos().get(1));
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

	private Object getValor(Token token) {
		return simbolos.get(token.getIndice()).getValor();
	}

	private void setValor(Token token, Object valor) {
		simbolos.get(token.getIndice()).setValor(valor);
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
