// TokenInvalidoException.java
package br.gov.pmr.cad_familias.excecao;

public class TokenInvalidoException extends RuntimeException {
    public TokenInvalidoException(String mensagem) {
        super(mensagem);
    }
}
