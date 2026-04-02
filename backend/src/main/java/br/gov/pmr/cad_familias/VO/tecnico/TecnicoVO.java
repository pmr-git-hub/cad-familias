package br.gov.pmr.cad_familias.VO.tecnico;

import br.gov.pmr.cad_familias.domain.tecnico.Especialidade;

import java.io.Serializable;

public class TecnicoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;
    private String cpf;
    private String registroProfissional;
    private Especialidade especialidade;
    private Long equipamentoId;
    private String nomeEquipamento;
    private boolean ativo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getRegistroProfissional() { return registroProfissional; }
    public void setRegistroProfissional(String registroProfissional) { this.registroProfissional = registroProfissional; }

    public Especialidade getEspecialidade() { return especialidade; }
    public void setEspecialidade(Especialidade especialidade) { this.especialidade = especialidade; }

    public Long getEquipamentoId() { return equipamentoId; }
    public void setEquipamentoId(Long equipamentoId) { this.equipamentoId = equipamentoId; }

    public String getNomeEquipamento() { return nomeEquipamento; }
    public void setNomeEquipamento(String nomeEquipamento) { this.nomeEquipamento = nomeEquipamento; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}
