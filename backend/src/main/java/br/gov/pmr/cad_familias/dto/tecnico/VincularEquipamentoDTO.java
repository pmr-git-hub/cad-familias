package br.gov.pmr.cad_familias.dto.tecnico;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
public class VincularEquipamentoDTO implements Serializable {

    @NotNull
    private Long equipamentoId;

    @NotNull
    private LocalDate dataInicio;
}
