package br.gov.pmr.cad_familias.VO.familia;

import java.io.Serializable;
import java.time.LocalDate;

import br.gov.pmr.cad_familias.domain.familia.Parentesco;
import br.gov.pmr.cad_familias.domain.familia.Sexo;

public class PessoaVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;
    private String cpf;
    private String telefone;
    private Sexo sexo;
    private Parentesco parentesco;
    private Long rendaMensal;
    private LocalDate dataNascimento;
    private String numeroRg;
    private String orgaoExpeditorRg;
    private LocalDate dataExpedicaoRg;
    private boolean referencia;
    private EnderecoVO endereco;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public Sexo getSexo() { return sexo; }
    public void setSexo(Sexo sexo) { this.sexo = sexo; }

    public Parentesco getParentesco() { return parentesco; }
    public void setParentesco(Parentesco parentesco) { this.parentesco = parentesco; }

    public Long getRendaMensal() { return rendaMensal; }
    public void setRendaMensal(Long rendaMensal) { this.rendaMensal = rendaMensal; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getNumeroRg() { return numeroRg; }
    public void setNumeroRg(String numeroRg) { this.numeroRg = numeroRg; }

    public String getOrgaoExpeditorRg() { return orgaoExpeditorRg; }
    public void setOrgaoExpeditorRg(String orgaoExpeditorRg) { this.orgaoExpeditorRg = orgaoExpeditorRg; }

    public LocalDate getDataExpedicaoRg() { return dataExpedicaoRg; }
    public void setDataExpedicaoRg(LocalDate dataExpedicaoRg) { this.dataExpedicaoRg = dataExpedicaoRg; }

    public boolean isReferencia() { return referencia; }
    public void setReferencia(boolean referencia) { this.referencia = referencia; }

    public EnderecoVO getEndereco() { return endereco; }
    public void setEndereco(EnderecoVO endereco) { this.endereco = endereco; }
}
