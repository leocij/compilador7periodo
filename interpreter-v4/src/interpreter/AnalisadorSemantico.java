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
//			System.out.print(" " + simbolos.get(i).getEscopo());
//			System.out.print(" " + simbolos.get(i).getTipo());
//			System.out.print(" " + simbolos.get(i).getValor());
//			System.out.println(" " + simbolos.get(i).getFlag());
//		}

	}

	public void addErro(String erro) {
		erros.add("Erro semântico: " + erro + ". ");
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
		} else if (no.getTipo().equals("NO_LEITURA")) {
			return leitura(no);
		} else if (no.getTipo().equals("NO_ESCRITA")) {
			return escrita(no);
		} else if (no.getTipo().equals("NO_COND")) {
			return cond(no);
		} else if (no.getTipo().equals("NO_SENAO")) {
			return senao(no);
		} else if (no.getTipo().equals("NO_LACO")) {
			return laco(no);
		} else if (no.getTipo().equals("NO_EXP_LOG")) {
			return expLog(no);
		} else if (no.getTipo().equals("NO_EXP_REL")) {
			return expRel(no);
		} else if (no.getTipo().equals("NO_OP_LOG")) {
			return opLog(no);
		} else if (no.getTipo().equals("NO_OP_REL")) {
			return opRel(no);
		} else if (no.getTipo().equals("NO_ATRIB")) {
			return atrib(no);
		} else if (no.getTipo().equals("NO_EXP_ARIT")) {
			return expArit(no);
		} else if (no.getTipo().equals("NO_OP_ARIT")) {
			return opArit(no);
		} else if (no.getTipo().equals("NO_OPERAN")) {
			return operan(no);
		} else {
			addErro("Erro: NO desconhecido = " + no.getTipo());
			return null;
		}
	}

	// <atrib> ::= '=' id <exp_arit>
	@SuppressWarnings("unchecked")
	private Object atrib(No no) {

		// { = a1 { + a2 { * 10 a3 } } }

		Token id = no.getFilhos().get(1).getToken();

		String tipoId = simbolos.get(id.getIndice()).getTipo();

		if (tipoId == null) {
			addErro("Erro de Atribuição: ID \"" + id.getImagem() + "\" não declarado na Linha = \"" + id.getLinha()
					+ "\" e Coluna = \"" + id.getColuna() + "\"");
		} else {
			List<Token> operandos = (List<Token>) analisar(no.getFilhos().get(2));

			for (Token operando : operandos) {

				String tipo = simbolos.get(operando.getIndice()).getTipo();
				
				if (tipo == null) {
					addErro("Erro de Atribuição: ID \"" + operando.getImagem() + "\" não declarado na Linha = \""
							+ operando.getLinha() + "\" e Coluna = \"" + operando.getColuna() + "\"");
				}else if (!tipo.equals(tipoId) && !tipoId.equals("texto")) {
					addErro("Erro de Atribuição: A variável \"" + simbolos.get(id.getIndice()).getImagem()
							+ "\" tem Tipo \"" + tipoId + "\" que é incompatível com \"" + operando.getImagem()
							+ "\" que é do tipo \"" + tipo + "\" na Linha = \"" + operando.getLinha()
							+ "\" e Coluna = \"" + operando.getColuna() + "\"");
				}
			}
		}

		return null;
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

	// <operan> ::= id | cli | clr | cls
	private Object operan(No no) {
		return no.getFilhos().get(0).getToken();
	}

	// <op_arit> ::= '+' | '-' | '*' | '/' | '.'
	private Object opArit(No no) {
		return no.getFilhos().get(0).getToken().getImagem();
	}

	// <op_rel> ::= '>' | '<' | '>=' | '<=' | '==' | '!='
	private Object opRel(No no) {
		return no.getFilhos().get(0).getToken().getImagem();
	}

	// <op_log> ::= '&&' | '||'
	private Object opLog(No no) {
		return no.getFilhos().get(0).getToken().getImagem();
	}

	// <exp_rel> ::= <op_rel> <operan> <operan> | <op_log> '{' <exp_rel> '}' '{'
	// <exp_rel> '}'
	private Object expRel(No no) {
		if (no.getFilhos().get(0).getTipo().equals("NO_OP_REL")) {

			Token op1 = (Token) analisar(no.getFilhos().get(1));
			Token op2 = (Token) analisar(no.getFilhos().get(2));

			String op1Tipo = simbolos.get(op1.getIndice()).getTipo();
			String op2Tipo = simbolos.get(op2.getIndice()).getTipo();

			if (op1Tipo == null) {	
				addErro("O Tipo do ID "+op1+" não foi declarado = " + op1Tipo);
			} else if(op2Tipo == null){
				addErro("O Tipo do ID "+op2+" não foi declarado = " + op2Tipo);
			}else if (!op1Tipo.equals(op2Tipo)) {
				addErro("O Tipo "+op1Tipo+" é incompatível com o Tipo " + op2Tipo);
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

	// <laco> ::= 'enquanto' <exp_log> <list_cmd>
	private Object laco(No no) {
		analisar(no.getFilhos().get(1));
		analisar(no.getFilhos().get(2));
		return null;
	}

	// <senao> ::= '{' <list_cmd> '}' |
	private Object senao(No no) {
		if (!no.getFilhos().isEmpty()) {
			analisar(no.getFilhos().get(1));
		}
		return null;
	}

	// <cond> ::= 'se' <exp_log> '{' <list_cmd> '}' <senao>
	private Object cond(No no) {
		analisar(no.getFilhos().get(1));
		analisar(no.getFilhos().get(3));
		analisar(no.getFilhos().get(5));
		return null;
	}

	// <escrita> ::= 'mostra' <exp_arit>
	private Object escrita(No no) {
		analisar(no.getFilhos().get(1));
		return null;
	}

	// <leitura> ::= 'le' id
	private Object leitura(No no) {
		Token id = no.getFilhos().get(1).getToken();

		if (simbolos.get(id.getIndice()).getTipo() == null) {
			addErro("Erro: ID não declarado = " + id.getImagem());
		} else {
			analisar(no.getFilhos().get(1));
		}

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

	// <cmd> ::= '{' <cmd_inter> '}'
	private Object cmd(No no) {
		analisar(no.getFilhos().get(1));
		return null;
	}

	// <cmd_inter> ::= <decl> | <atrib> | <laco> | <cond> | <escrita> |
	// <leitura>
	private Object cmdInter(No no) {
		analisar(no.getFilhos().get(0));
		return null;
	}

	// <decl> ::= <tipo> <list_id>
	@SuppressWarnings("unchecked")
	private Object decl(No no) {
		String tipo = (String) analisar(no.getFilhos().get(0));
		List<Token> listId = (List<Token>) analisar(no.getFilhos().get(1));

		for (Token id : listId) {
			if (simbolos.get(id.getIndice()).getTipo() != null) {
				addErro("Erro de Declaração: ID redeclarado = " + id);
			} else {
				simbolos.get(id.getIndice()).setTipo(tipo);
			}
		}

		return null;
	}

	// <tipo> ::= 'int' | 'real' | 'texto' | 'logico'
	private Object tipo(No no) {
		return no.getFilhos().get(0).getToken().getImagem();
	}

	// <list_id> ::= id <list_id2>
	@SuppressWarnings("unchecked")
	private Object listId(No no) {
		Token id = no.getFilhos().get(0).getToken();
		List<Token> listId2 = (List<Token>) analisar(no.getFilhos().get(1));
		listId2.add(0, id);

		return listId2;
	}

	// <list_id2> ::= <list_id> |
	private Object listId2(No no) {
		if (no.getFilhos().isEmpty()) {
			return new ArrayList<Token>();
		} else {
			return analisar(no.getFilhos().get(0));
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

	public boolean temErros() {
		return !erros.isEmpty();
	}

	public void mostraErros() {
		for (String erro : erros) {
			System.out.println(erro);
		}
	}
}
