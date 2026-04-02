// TecnicoNaoEncontradoException.java
package br.gov.pmr.cad_familias.excecao;

public class TecnicoNaoEncontradoException extends RuntimeException {
    public TecnicoNaoEncontradoException() {
        super("Técnico não encontrado.");
    }
}
