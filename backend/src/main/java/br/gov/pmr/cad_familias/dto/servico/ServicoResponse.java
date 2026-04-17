package br.gov.pmr.cad_familias.dto.servico;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ServicoResponse {

    private Long id;
    private Long equipamentoId;
    private String nome;
    private String descricao;
    private String publicoAlvo;
    private Integer faixaEtariaMin;
    private Integer faixaEtariaMax;
    private String diaSemana;
    private String horario;
    private boolean ativo;
    private LocalDateTime criadoEm;
    private Long criadoPor;
    private LocalDateTime atualizadoEm;
    private Long atualizadoPor;
}
