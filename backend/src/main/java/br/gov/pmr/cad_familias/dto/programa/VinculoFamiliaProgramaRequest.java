package br.gov.pmr.cad_familias.dto.programa;

import br.gov.pmr.cad_familias.domain.programa.StatusVinculo;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class VinculoFamiliaProgramaRequest {

    @NotNull(message = "ID da família é obrigatório")
    private Long familiaId;

    @NotNull(message = "ID do programa é obrigatório")
    private Long programaId;

    private LocalDate dataEntrada;

    private StatusVinculo status;

    public VinculoFamiliaProgramaRequest() {}

    public Long getFamiliaId() { return familiaId; }
    public void setFamiliaId(Long familiaId) { this.familiaId = familiaId; }

    public Long getProgramaId() { return programaId; }
    public void setProgramaId(Long programaId) { this.programaId = programaId; }

    public LocalDate getDataEntrada() { return dataEntrada; }
    public void setDataEntrada(LocalDate dataEntrada) { this.dataEntrada = dataEntrada; }

    public StatusVinculo getStatus() { return status; }
    public void setStatus(StatusVinculo status) { this.status = status; }
}
