package br.com.pmr.cad_familias.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import br.com.pmr.cad_familias.util.CustomLocalDateDeserializer;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "pessoa")
public class Pessoa implements Serializable{

	
	private static final long serialVersionUID = -1490668826956617287L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 300)
	private String nome;
	
	@Column(length = 11)
	private String cpf;
	
	@Column(name = "rg_numero")
	private String numeroRg;
	
	@Column(name = "rg_orgao_expeditor")
	private String orgaoExpeditorRg;

	@Column(name = "rg_data_expedicao")
	@JsonDeserialize(using = CustomLocalDateDeserializer.class)
	private LocalDate dataExpedicaoRg;

	@Column(nullable = false, name = "data_nascimento")
	@JsonDeserialize(using = CustomLocalDateDeserializer.class)
	private LocalDate dataNascimento;


	@Column(name = "pessoa_referencia")
	private boolean referencia;
	
	@Embedded
	private Endereco endereco;
	
	@Column
	private String telefone;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "sexo")
	private Sexo sexo; 
	
	@Column
	private String parentesco;
	
	@Column
	private Long rendaMensal;
	
	@Column
	private int idade;
	
	@ManyToOne
    @JoinColumn(name = "familia_id")
    @JsonBackReference
    private Familia familia;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNumeroRg() {
		return numeroRg;
	}

	public void setNumeroRg(String numeroRg) {
		this.numeroRg = numeroRg;
	}

	public String getOrgaoExpeditorRg() {
		return orgaoExpeditorRg;
	}

	public void setOrgaoExpeditorRg(String orgaoExpeditorRg) {
		this.orgaoExpeditorRg = orgaoExpeditorRg;
	}

	public LocalDate getDataExpedicaoRg() {
		return dataExpedicaoRg;
	}

	public void setDataExpedicaoRg(LocalDate dataExpedicaoRg) {
		this.dataExpedicaoRg = dataExpedicaoRg;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public boolean isReferencia() {
		return referencia;
	}

	public void setReferencia(boolean referencia) {
		this.referencia = referencia;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public String getParentesco() {
		return parentesco;
	}

	public void setParentesco(String parentesco) {
		this.parentesco = parentesco;
	}

	public Long getRendaMensal() {
		return rendaMensal;
	}

	public void setRendaMensal(Long rendaMensal) {
		this.rendaMensal = rendaMensal;
	}

	public int getIdade() {
		return idade;
	}

	public void setIdade(int idade) {
		this.idade = idade;
	}

	public Familia getFamilia() {
		return familia;
	}

	public void setFamilia(Familia familia) {
		this.familia = familia;
	}

	@Override
	public int hashCode() {
		return Objects.hash(familia, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pessoa other = (Pessoa) obj;
		return Objects.equals(id, other.id);
	}

	


}
