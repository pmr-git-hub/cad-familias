package br.gov.pmr.cad_familias.dto.programa;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ProgramaSocialResponse {

    private Long id;
    private String nome;
    private String criterios;
    private String orgaoGestor;
    private boolean ativo;
    private LocalDateTime criadoEm;
    private Long criadoPor;
    private LocalDateTime atualizadoEm;
    private Long atualizadoPor;

    public ProgramaSocialResponse() {}

}
