package br.gov.pmr.cad_familias.dto.tecnico;

import br.gov.pmr.cad_familias.domain.tecnico.Especialidade;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class TecnicoDTO implements Serializable {

    private Long id;
    private String nome;
    private String cpf;
    private String registroProfissional;
    private Especialidade especialidade;
    private Long equipamentoId;
    private String nomeEquipamento;
    private boolean ativo;
}
