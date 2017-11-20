package br.lms.core.domain;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Classe que representa o analisador léxico
 * 
 * @author Fernando Fogliato
 */
public class Lexico {

	private ArrayList<Token> palavrasReservadas;
	private ArrayList<Token> operadores;
	private ArrayList<Token> simbolosEspeciais;
	private ArrayList<Token> tokensEncontrados;
	private ArrayList<String> errosEncontrados;
	private ArrayList<String> letras;
	private ArrayList<String> digitos;
	private ArrayList<String> caracteresEspeciais;
	private String buffer;
	private String entradaPrograma;
	private int posicaoAnalisePrograma;
	private int posicaoInicioBuffer;
	private int estado;
	private int posicaoTokenAtual;
	private int qtdEspacoEmBranco;

	public ArrayList<Token> getListaTokensEncontrados() {
		return tokensEncontrados;
	}

	public ArrayList<String> getListaMensagensErro() {
		return errosEncontrados;
	}

	public Lexico() {
		this.palavrasReservadas = new ArrayList<Token>();
		this.operadores = new ArrayList<Token>();
		this.simbolosEspeciais = new ArrayList<Token>();
		this.tokensEncontrados = new ArrayList<Token>();
		this.errosEncontrados = new ArrayList<String>();

		this.buffer = "";
		this.estado = 0;
		inicializarPosicaoTokenAtual();

		carregarPalavrasReservadas();
		carregarOperadores();
		carregarSimbolosEspeciais();

		letras = new ArrayList<>(Arrays.asList(new String[] { "a", "b", "c", "d", "e", "f", "g",
				"h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w",
				"x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
				"N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" }));

		digitos = new ArrayList<String>(Arrays.asList(new String[] { "0", "1", "2", "3", "4", "5",
				"6", "7", "8", "9" }));

		caracteresEspeciais = new ArrayList<String>(Arrays.asList(new String[] { "\r", "\t", "\n",
				" " }));
	}

	private void carregarPalavrasReservadas() {
		palavrasReservadas.add(new Token(19, "ID", "Palavra Reservada"));
		palavrasReservadas.add(new Token(20, "INTEIRO", "Palavra Reservada"));
		palavrasReservadas.add(new Token(21, "LIT", "Palavra Reservada"));
		palavrasReservadas.add(new Token(22, "PROGRAM", "Palavra Reservada"));
		palavrasReservadas.add(new Token(23, "CONST", "Palavra Reservada"));
		palavrasReservadas.add(new Token(24, "VAR", "Palavra Reservada"));
		palavrasReservadas.add(new Token(25, "PROCEDURE", "Palavra Reservada"));
		palavrasReservadas.add(new Token(26, "BEGIN", "Palavra Reservada"));
		palavrasReservadas.add(new Token(27, "END", "Palavra Reservada"));
		palavrasReservadas.add(new Token(28, "INTEGER", "Palavra Reservada"));
		palavrasReservadas.add(new Token(29, "OF", "Palavra Reservada"));
		palavrasReservadas.add(new Token(30, "CALL", "Palavra Reservada"));
		palavrasReservadas.add(new Token(31, "IF", "Palavra Reservada"));
		palavrasReservadas.add(new Token(32, "THEN", "Palavra Reservada"));
		palavrasReservadas.add(new Token(33, "ELSE", "Palavra Reservada"));
		palavrasReservadas.add(new Token(34, "WHILE", "Palavra Reservada"));
		palavrasReservadas.add(new Token(35, "DO", "Palavra Reservada"));
		palavrasReservadas.add(new Token(36, "REPEAT", "Palavra Reservada"));
		palavrasReservadas.add(new Token(37, "UNTIL", "Palavra Reservada"));
		palavrasReservadas.add(new Token(38, "READLN", "Palavra Reservada"));
		palavrasReservadas.add(new Token(39, "WRITELN", "Palavra Reservada"));
		palavrasReservadas.add(new Token(40, "OR", "Operador lógico - OU"));
		palavrasReservadas.add(new Token(41, "AND", "Operador lógico - E"));
		palavrasReservadas.add(new Token(42, "NOT", "Operador de negação"));
		palavrasReservadas.add(new Token(43, "FOR", "Palavra Reservada"));
		palavrasReservadas.add(new Token(44, "TO", "Palavra Reservada"));
		palavrasReservadas.add(new Token(45, "CASE", "Palavra Reservada"));
	}

	private void carregarOperadores() {
		operadores.add(new Token(2, "+", "Operador - Adição"));
		operadores.add(new Token(3, "-", "Operador - Subtração"));
		operadores.add(new Token(4, "*", "Operador - Multiplicação"));
		operadores.add(new Token(5, "/", "Operador - Divisão"));
		operadores.add(new Token(6, "=", "Operador Relacional - Igual"));
		operadores.add(new Token(7, ">", "Operador Relacional - Maior"));
		operadores.add(new Token(8, ">=", "Operador Relacional - Maior ou Igual"));
		operadores.add(new Token(9, "<", "Operador Relacional - Menor"));
		operadores.add(new Token(10, "<=", "Operador Relacional - Menor ou Igual"));
		operadores.add(new Token(11, "<>", "Operador Relacional - Diferente"));
	}

	private void carregarSimbolosEspeciais() {
		simbolosEspeciais.add(new Token(12, ":=", "Símbolo - Recebe"));
		simbolosEspeciais.add(new Token(13, ":", "Símbolo - Dois Pontos"));		
		simbolosEspeciais.add(new Token(14, ";", "Símbolo - Ponto e Vírgula"));
		simbolosEspeciais.add(new Token(15, ",", "Símbolo - Vírgula"));
		simbolosEspeciais.add(new Token(16, ".", "Símbolo - Ponto"));
		simbolosEspeciais.add(new Token(17, "(", "Símbolo - Abre Parênteses"));
		simbolosEspeciais.add(new Token(18, ")", "Símbolo - Fecha Parênteses"));
	}

	public void limparBuffer() {
		this.buffer = "";
		posicaoInicioBuffer = 0;
	}

	public void addBuffer(String texto) {
		if (this.buffer.equals("")) {
			posicaoInicioBuffer = posicaoAnalisePrograma - qtdEspacoEmBranco;
		}
		this.buffer += texto;
	}

	private void estadoInicial(String caracterAtual) {
		// Se for uma letra
		if (letras.contains(caracterAtual)) {
			addBuffer(caracterAtual);
			estado = 1;

		// Se for um dígito
		} else if (digitos.contains(caracterAtual)) {
			addBuffer(caracterAtual);
			estado = 2;

		// Se for símbolo especial
		} else if (isSimboloEspecial(caracterAtual)) {
			if (caracterAtual.equals("(")) {
				addBuffer(caracterAtual);
				estado = 9;
			} else if (caracterAtual.equals(".")) {
				addBuffer(caracterAtual);
				estado = 4;
			} else if (caracterAtual.equals(":")) {
				addBuffer(caracterAtual);
				estado = 5;
			} else {
				addBuffer(caracterAtual);
				estado = 3;
			}

		} else if (isOperador(caracterAtual)) {
			if (caracterAtual.equals("<")) {
				addBuffer(caracterAtual);
				estado = 6;
			} else if (caracterAtual.equals(">")) {
				addBuffer(caracterAtual);
				estado = 7;
			} else {
				addBuffer(caracterAtual);
				estado = 14;
			}

		// Se for "aspas duplas"
		} else if (caracterAtual.equals("\"")) {
			addBuffer(caracterAtual);
			estado = 8;

		// Se não for nenhum deles, ignora
		} else {
			estado = 0;
			limparBuffer();

			if (!caracteresEspeciais.contains(caracterAtual)) {
				errosEncontrados.add("Token '" + caracterAtual + "' inválido! posição: " + posicaoAnalisePrograma);
			}
		}
	}

	/**
	 * Identificador ou palavra reservada
	 */
	private void estado01(String caracterAtual) {
		// Se for uma letra ou um dígito
		if (letras.contains(caracterAtual) || digitos.contains(caracterAtual)) {
			addBuffer(caracterAtual);
			estado = 1;

		// Verifica se é um identificador-palavra reservada
		} else {
			estado = 0;
			addIdentificadorPalavraReservada(buffer);
			entradaPrograma = adicionarEspacoEmBranco(entradaPrograma, posicaoAnalisePrograma);
			limparBuffer();
		}
	}

	/**
	 * Número Inteiro
	 */
	private void estado02(String caracterAtual) {
		// Se for um dígito
		if (digitos.contains(caracterAtual)) {
			addBuffer(caracterAtual);
			estado = 2;
		// Se não for dígito verifica numero inteiro
		} else {
			estado = 0;
			addNumeroInteiro(buffer);
			entradaPrograma = adicionarEspacoEmBranco(entradaPrograma, posicaoAnalisePrograma);
			limparBuffer();
		}
	}

	/**
	 * Simbolo Especial
	 */
	private void estado03(String caracterAtual) {
		estado = 0;
		addSimboloEspecial(buffer);
		entradaPrograma = adicionarEspacoEmBranco(entradaPrograma, posicaoAnalisePrograma);
		limparBuffer();
	}

	/**
	 * . ou ..
	 */
	private void estado04(String caracterAtual) {
		if (caracterAtual.equals(".")) {
			estado = 0;
			tokensEncontrados.add(simbolosEspeciais.get(simbolosEspeciais.indexOf(new Token(0, "..", ""))));
			limparBuffer();
		} else {
			estado = 0;
			tokensEncontrados.add(simbolosEspeciais.get(simbolosEspeciais.indexOf(new Token(0, ".", ""))));
			entradaPrograma = adicionarEspacoEmBranco(entradaPrograma, posicaoAnalisePrograma);
			limparBuffer();
		}
	}

	/**
	 * : ou :=
	 */
	private void estado05(String caracterAtual) {
		if (caracterAtual.equals("=")) {
			tokensEncontrados.add(new Token(12, ":=", "Atribuição", posicaoInicioBuffer));
			estado = 0;
			limparBuffer();
		} else {
			tokensEncontrados.add(new Token(13, ":", "Dois pontos", posicaoInicioBuffer));
			estado = 0;
			entradaPrograma = adicionarEspacoEmBranco(entradaPrograma, posicaoAnalisePrograma);
			limparBuffer();
		}
	}

	/**
	 * >= ou <> ou >
	 */
	private void estado06(String caracterAtual) {
		if (caracterAtual.equals(">")) {
			tokensEncontrados.add(new Token(11, "<>", "Operador Relacional - Diferente", posicaoInicioBuffer));
			estado = 0;
			entradaPrograma = adicionarEspacoEmBranco(entradaPrograma, posicaoAnalisePrograma + 1);
			limparBuffer();
		} else if (caracterAtual.equals("=")) {
			tokensEncontrados.add(new Token(10, "<=", "Operador Relacional - Menor ou Igual", posicaoInicioBuffer));
			estado = 0;
			entradaPrograma = adicionarEspacoEmBranco(entradaPrograma, posicaoAnalisePrograma + 1);
			limparBuffer();
		} else {
			tokensEncontrados.add(new Token(9, "<", "Operador Relacional - Menor", posicaoInicioBuffer));
			estado = 0;
			entradaPrograma = adicionarEspacoEmBranco(entradaPrograma, posicaoAnalisePrograma);
			limparBuffer();
		}
	}

	/**
	 * >= ou >
	 */
	private void estado07(String caracterAtual) {
		if (caracterAtual.equals("=")) {
			tokensEncontrados.add(new Token(8, ">=", "Operador Relacional - Maior ou Igual", posicaoInicioBuffer));
			estado = 0;
			entradaPrograma = adicionarEspacoEmBranco(entradaPrograma, posicaoAnalisePrograma + 1);
			limparBuffer();
		} else {
			tokensEncontrados.add(new Token(7, ">", "Operador Relacional - Maior", posicaoInicioBuffer));
			estado = 0;
			entradaPrograma = adicionarEspacoEmBranco(entradaPrograma, posicaoAnalisePrograma);
			limparBuffer();
		}
	}

	/**
	 * Literal
	 */
	private void estado08(String caracterAtual) {
		if (caracterAtual.equals("\"")) {
			addBuffer(caracterAtual);
			addLiteral(buffer);
			estado = 0;
			entradaPrograma = adicionarEspacoEmBranco(entradaPrograma, posicaoAnalisePrograma + 1);
			limparBuffer();
		} else {

			if (posicaoAnalisePrograma == entradaPrograma.length() - 1) {
				int numeroAspas = 0;

				for (char caracter : buffer.toCharArray()) {
					if (caracter == '"')
						numeroAspas++;
				}

				if (numeroAspas < 2) {
					estado = 10;
					entradaPrograma = adicionarEspacoEmBranco(entradaPrograma, posicaoAnalisePrograma + 1);
				}
			} else {
				estado = 8;
				addBuffer(caracterAtual);
			}
		}
	}

	/**
	 * Comentário
	 */
	private void estado09(String caracterAtual) {
		if (caracterAtual.equals("*")) {
			addBuffer(caracterAtual);
			estado = 11;
		} else {
			tokensEncontrados.add(new Token(17, "(", "Símbolo - Abre Parênteses", posicaoInicioBuffer));
			estado = 0;
			entradaPrograma = adicionarEspacoEmBranco(entradaPrograma, posicaoAnalisePrograma);
			limparBuffer();
		}
	}

	/**
	 * Erro de literal não finalizado
	 * 
	 * @param caracterAtual
	 */
	private void estado10(String caracterAtual) {
		errosEncontrados.add("Erro! Literal não finalizado: "
				+ buffer.replace("\r", " ").replace("\t", " ").replace("\n", " "));
	}

	/**
	 * Comentário
	 */
	private void estado11(String caracterAtual) {
		if (caracterAtual.equals("*")) {
			addBuffer(caracterAtual);
			estado = 13;
		} else {

			if (posicaoAnalisePrograma == entradaPrograma.length() - 1) {
				int numeroAsterisco = 0;

				for (char caracter : buffer.toCharArray()) {
					if (caracter == '*')
						numeroAsterisco++;
				}

				if (numeroAsterisco < 2) {
					estado = 12;
					entradaPrograma = adicionarEspacoEmBranco(entradaPrograma, posicaoAnalisePrograma + 1);
				}
			} else {
				estado = 11;
				addBuffer(caracterAtual);
			}
		}
	}

	/**
	 * Erro de comentário não finalizado
	 */
	private void estado12(String caracterAtual) {
		errosEncontrados.add("Erro! Comentário nao finalizado: "
				+ buffer.replace("\r", " ").replace("\t", " ").replace("\n", " "));
		limparBuffer();
		estado = 0;
	}

	private void estado13(String caracterAtual) {
		if (caracterAtual.equals(")")) {
			limparBuffer();
			estado = 0;
		} else {
			if (posicaoAnalisePrograma == entradaPrograma.length() - 1) {
				int numeroParenteses = 0;

				for (char caracter : buffer.toCharArray()) {
					if (caracter == ')')
						numeroParenteses++;
				}

				if (numeroParenteses < 1) {
					estado = 12;
					entradaPrograma = adicionarEspacoEmBranco(entradaPrograma, posicaoAnalisePrograma + 1);
				}
			} else {
				estado = 12;				
			}
		}
	}
	
	private void estado14(String caracterAtual) {
		estado = 0;
		addOperador(buffer);
		entradaPrograma = adicionarEspacoEmBranco(entradaPrograma, posicaoAnalisePrograma);
		limparBuffer();
	}

	public void analisarEntrada(String entrada) {
		estado = 0;
		String caracterAtual = "";
		entradaPrograma = entrada;
		posicaoAnalisePrograma = 0;
		qtdEspacoEmBranco = 0;
		inicializarPosicaoTokenAtual();		

		for (int i = 0; i < entradaPrograma.length(); i++) {
			posicaoAnalisePrograma = i;

			// pega o caracter atual
			caracterAtual = String.valueOf(entradaPrograma.charAt(i));

			switch (estado) {
				case 0: /* ESTADO INICIAL */
					estadoInicial(caracterAtual);
					break;

				case 1: /* IDENTIFICADOR / PALAVRA RESERVADA */
					estado01(caracterAtual);
					break;

				case 2: /* NUMERO INTEIRO */
					estado02(caracterAtual);
					break;

				case 3: /* SIMBOLOS ESPECIAIS */
					estado03(caracterAtual);
					break;

				case 4: /* . OU .. */
					estado04(caracterAtual);
					break;

				case 5: /* : OU := */
					estado05(caracterAtual);
					break;

				case 6: /* >= OU <> OU > */
					estado06(caracterAtual);
					break;

				case 7: /* >= OU > */
					estado07(caracterAtual);
					break;

				case 8: /* LITERAL */
					estado08(caracterAtual);
					break;

				case 9: /* COMENTÁRIO */
					estado09(caracterAtual);
					break;

				case 10: /* ERRO DE LITERAL NAO FINALIZADO */
					estado10(caracterAtual);
					break;

				case 11: /* COMENTÁRIO */
					estado11(caracterAtual);
					break;

				case 12: /* ERRO DE COMENTARIO NAO FINALIZADO */
					estado12(caracterAtual);
					break;

				case 13:
					estado13(caracterAtual);
					break;
				
				case 14:
					estado14(caracterAtual);
					break;

				default:
					break;
			}
		}
	}
	
	private Token efetuarCopiaTokenComNovaPosicao(Token tokenOriginal) {
		return new Token(
				tokenOriginal.getCodigo(), 
				tokenOriginal.getSimbolo(), 
				tokenOriginal.getDescricao(),
				posicaoInicioBuffer);
	}

	public void addIdentificadorPalavraReservada(String palavra) {
		palavra = palavra.replace("\n", "").trim();
		Token token = new Token(0, palavra.toUpperCase(), "");
		
		// Verifica se é uma palavra reservada
		if (isPalavraReservada(palavra)) {
			token = palavrasReservadas.get(palavrasReservadas.indexOf(token));			
			tokensEncontrados.add(efetuarCopiaTokenComNovaPosicao(token));
		} else {
			if (palavra.trim().length() <= 30)
				tokensEncontrados.add(new Token(19, palavra, "Identificador", posicaoInicioBuffer));
			else
				errosEncontrados.add("Identificador '" + palavra
						+ "' inválido! Tamanho excede 30 caracteres.");
		}
	}

	public void addNumeroInteiro(String numero) {
		try {
			numero = numero.replace("\n", "").trim();

			// O numero inteiro não deve, em módulo, ser maior do que 32767
			if (Integer.parseInt(numero) <= 32767)
				tokensEncontrados.add(new Token(20, numero, "Número Inteiro", posicaoInicioBuffer));
			else
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			errosEncontrados.add("Número inteiro '" + numero + "' inválido! Excede o tamanho máximo de 32767.");
		}
	}

	public void addSimboloEspecial(String simbolo) {
		simbolo = simbolo.replace("\n", "").trim();

		if (isSimboloEspecial(simbolo)) {
			Token token = new Token(0, simbolo, "");
			token = simbolosEspeciais.get(simbolosEspeciais.indexOf(token));
			tokensEncontrados.add(efetuarCopiaTokenComNovaPosicao(token));
		}
	}
	
	public void addOperador(String operador) {
		operador = operador.replace("\n", "").trim();

		if (isOperador(operador)) {
			Token token = new Token(0, operador, "");
			token = operadores.get(operadores.indexOf(token));
			tokensEncontrados.add(efetuarCopiaTokenComNovaPosicao(token));
		}
	}	

	private boolean isSimboloEspecial(String simbolo) {
		return simbolosEspeciais.contains(new Token(0, simbolo, ""));
	}

	public boolean isPalavraReservada(String palavra) {
		return palavrasReservadas.contains(new Token(0, palavra.toUpperCase(), ""));
	}

	private boolean isOperador(String operador) {
		return operadores.contains(new Token(0, operador, ""));
	}

	public void addLiteral(String literal) {
		// Sequência de caracteres (letras/símbolos/números) delimitados por aspas,
		// contendo não mais do que 255 caracteres.
		if (literal.length() <= 255) {
			tokensEncontrados.add(new Token(21, literal, "Literal", posicaoInicioBuffer));
		} else {
			errosEncontrados.add("Literal inválido. Excedeu o tamanho máximo de 255 caracteres: '"
					+ literal + "' ");
		}

	}

	public String adicionarEspacoEmBranco(String cadeia, int posicao) {
		qtdEspacoEmBranco++;
		StringBuilder novaCadeia = new StringBuilder(cadeia);
		novaCadeia.insert(posicao, " ");
		return String.valueOf(novaCadeia);
	}
	
	public int getPosicaoTokenAtual() {
		return posicaoTokenAtual;
	}

	public void inicializarPosicaoTokenAtual() {
		this.posicaoTokenAtual = -1;
	}

	public Token getProximoToken() {
		posicaoTokenAtual++;
		if (tokensEncontrados.size() > 0 && posicaoTokenAtual < tokensEncontrados.size()) {
			return tokensEncontrados.get(posicaoTokenAtual);
		} 
		
		return null;
	}

}
