package br.gov.pmr.cad_familias.dto.programa;

import br.gov.pmr.cad_familias.domain.programa.StatusVinculo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class VinculoDesligamentoRequest {

    @NotNull(message = "Data de saída é obrigatória")
    private LocalDate dataSaida;

    @NotBlank(message = "Motivo da saída é obrigatório")
    private String motivoSaida;

    private StatusVinculo status;

    public VinculoDesligamentoRequest() {}

    public LocalDate getDataSaida() { return dataSaida; }
    public void setDataSaida(LocalDate dataSaida) { this.dataSaida = dataSaida; }

    public String getMotivoSaida() { return motivoSaida; }
    public void setMotivoSaida(String motivoSaida) { this.motivoSaida = motivoSaida; }

    public StatusVinculo getStatus() { return status; }
    public void setStatus(StatusVinculo status) { this.status = status; }
}
