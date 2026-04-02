package br.gov.pmr.cad_familias.VO.tecnico;

import br.gov.pmr.cad_familias.domain.tecnico.Especialidade;

import java.io.Serializable;

public class TecnicoResumoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;
    private Especialidade especialidade;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Especialidade getEspecialidade() { return especialidade; }
    public void setEspecialidade(Especialidade especialidade) { this.especialidade = especialidade; }
}
