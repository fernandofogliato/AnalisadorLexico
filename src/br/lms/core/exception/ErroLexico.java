package br.lms.core.exception;

public class ErroLexico extends ErroAnalise {
	
	private static final long serialVersionUID = -3782219368096028797L;

	public ErroLexico(String mensagem, int posicao) {
        super(mensagem, posicao);
    }

    public ErroLexico(String mensagem) {
        super(mensagem);
    }
}
