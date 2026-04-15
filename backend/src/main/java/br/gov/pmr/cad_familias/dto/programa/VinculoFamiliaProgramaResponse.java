package br.gov.pmr.cad_familias.dto.programa;

import br.gov.pmr.cad_familias.domain.programa.StatusVinculo;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class VinculoFamiliaProgramaResponse {

    private Long id;
    private Long familiaId;
    private Long programaId;
    private String programaNome;
    private LocalDate dataEntrada;
    private LocalDate dataSaida;
    private StatusVinculo status;
    private String motivoSaida;
    private LocalDateTime criadoEm;
    private Long criadoPor;
    private LocalDateTime atualizadoEm;
    private Long atualizadoPor;

    public VinculoFamiliaProgramaResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getFamiliaId() { return familiaId; }
    public void setFamiliaId(Long familiaId) { this.familiaId = familiaId; }

    public Long getProgramaId() { return programaId; }
    public void setProgramaId(Long programaId) { this.programaId = programaId; }

    public String getProgramaNome() { return programaNome; }
    public void setProgramaNome(String programaNome) { this.programaNome = programaNome; }

    public LocalDate getDataEntrada() { return dataEntrada; }
    public void setDataEntrada(LocalDate dataEntrada) { this.dataEntrada = dataEntrada; }

    public LocalDate getDataSaida() { return dataSaida; }
    public void setDataSaida(LocalDate dataSaida) { this.dataSaida = dataSaida; }

    public StatusVinculo getStatus() { return status; }
    public void setStatus(StatusVinculo status) { this.status = status; }

    public String getMotivoSaida() { return motivoSaida; }
    public void setMotivoSaida(String motivoSaida) { this.motivoSaida = motivoSaida; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

    public Long getCriadoPor() { return criadoPor; }
    public void setCriadoPor(Long criadoPor) { this.criadoPor = criadoPor; }

    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }

    public Long getAtualizadoPor() { return atualizadoPor; }
    public void setAtualizadoPor(Long atualizadoPor) { this.atualizadoPor = atualizadoPor; }
}
