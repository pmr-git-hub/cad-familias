package br.gov.pmr.cad_familias.excecao;

public class EquipamentoNaoEncontradoException extends RuntimeException{

    public EquipamentoNaoEncontradoException(String equipamento) {
        super("Equipamento " + equipamento + " não encontrado");
    }

    public EquipamentoNaoEncontradoException() {
        super("Equipamento não encontrado");
    }
}
