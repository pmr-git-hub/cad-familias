package br.com.pmr.cad_familias.excecao;

public class UsuarioOuSenhaInvalidoException extends RuntimeException {
    public UsuarioOuSenhaInvalidoException(){
        super("Usuário ou senha inválidos.");
    }
    public UsuarioOuSenhaInvalidoException(String message) {
        super(message);
    }

}
