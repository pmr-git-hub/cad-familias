package br.gov.pmr.cad_familias.dto.tecnico;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
public class TecnicoEquipamentoDTO implements Serializable {

    private Long id;
    private Long equipamentoId;
    private String nomeEquipamento;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private boolean ativo;
}
