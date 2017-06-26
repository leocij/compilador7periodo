package interpreter;

public class Principal {
	public static void main(String[] args) throws Exception {

		if (args.length == 0) {
			System.out.println("Entre com o nome do arquivo!");
			System.exit(0);
		}

		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(args[0]);

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

		// analisadorSintatico.mostraArvore();

		AnalisadorSemantico analisadorSemantico = new AnalisadorSemantico(analisadorSintatico.getRaiz(),
				analisadorLexico.getSimbolos());
		analisadorSemantico.analisar();

		if (analisadorSemantico.temErros()) {
			analisadorSemantico.mostraErros();
			System.exit(0);
		}

		// Interpretador interpretador = new
		// Interpretador(analisadorSintatico.getRaiz(),
		// analisadorSemantico.getSimbolos());
		// interpretador.executar();
		//
		// if(interpretador.temErros()){
		// interpretador.mostraErros();
		// System.exit(0);
		// }

		Gerador gerador = new Gerador(analisadorSintatico.getRaiz(), analisadorSemantico.getSimbolos());
		gerador.gerar();

		if (gerador.temErros()) {
			gerador.mostraErros();
			System.exit(0);
		}

		//
		// System.out.print("\n\n\n========== FIM ==========\n\n\n");
		// System.out.println("Correu tudo como esperado!!! OBRIGADO PELA
		// ATENÇÃO :)");
	}
}
