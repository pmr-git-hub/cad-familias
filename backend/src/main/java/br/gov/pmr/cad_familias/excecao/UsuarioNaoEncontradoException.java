package br.gov.pmr.cad_familias.excecao;

public class UsuarioNaoEncontradoException extends RuntimeException {
    public UsuarioNaoEncontradoException(String message) {
        super(message);
    }

  public UsuarioNaoEncontradoException() {
  }
}
