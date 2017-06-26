package interpreter;

import java.io.IOException;

public class Principal {
	public static void main(String[] args) throws IOException {

		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar();

		if (analisadorLexico.temErros()) {
			analisadorLexico.mostraErros();
			System.exit(0);
		}

		AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico(analisadorLexico.getTokens());
		analisadorSintatico.analisar();

		if (analisadorSintatico.temErros()) {
			analisadorSintatico.mostraErros();
			System.exit(0);
		}

		//analisadorSintatico.mostraArvore();

		AnalisadorSemantico analisadorSemantico = new AnalisadorSemantico(analisadorSintatico.getRaiz(),
				analisadorLexico.getSimbolos());
		analisadorSemantico.analisar();

		if (analisadorSemantico.temErros()) {
			analisadorSemantico.mostraErros();
			System.exit(0);
		}

		Interpretador interpretador = new Interpretador(analisadorSintatico.getRaiz(), analisadorSemantico.getSimbolos());
		interpretador.executar();
		
		if(interpretador.temErros()){
			interpretador.mostraErros();
			System.exit(0);
		}

		System.out.print("\n\n\n========== FIM ==========\n\n\n");
		System.out.println("Correu tudo como esperado!!! OBRIGADO PELA ATENÇÃO :)");
	}
}
