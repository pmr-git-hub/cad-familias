package br.gov.pmr.cad_familias.excecao;

public class FamiliaNaoEncontradaException extends RuntimeException{

    public FamiliaNaoEncontradaException() {
        super("Família não encontrada");
    }

    public FamiliaNaoEncontradaException(String nomePessoaReferencia) {
        super("Família de " + nomePessoaReferencia + " não encontrada");
    }
}
