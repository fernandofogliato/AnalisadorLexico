package br.lms.core.exception;

public class ErroAnalise extends Exception {
	
	private static final long serialVersionUID = 1L;
	private int posicao;

    public ErroAnalise(String mensagem, int posicao) {
        super(mensagem);
        this.posicao = posicao;
    }

    public ErroAnalise(String mensagem) {
        super(mensagem);
        this.posicao = -1;
    }

    public int getPosicao() {
        return posicao;
    }

    public String toString() {
        return super.toString() + ", @ " + posicao;
    }
}
