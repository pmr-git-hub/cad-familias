package br.gov.pmr.cad_familias.domain.familia;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Objects;

import br.gov.pmr.cad_familias.util.CustomLocalDateDeserializer;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.persistence.*;

@Entity
@Table(name = "pessoa")
public class Pessoa implements Serializable {

	private static final long serialVersionUID = -1490668826956617287L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "familia_id", nullable = false)
	@JsonBackReference
	private Familia familia;

	@Column(nullable = false, length = 300)
	private String nome;

	@Column(length = 14)
	private String cpf;

	@Column
	private String nis;

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

	@Column(name = "is_referencia", nullable = false)
	private boolean isReferencia;

	@Enumerated(EnumType.STRING)
	@Column
	private Parentesco parentesco;

	@Column
	private String telefone;

	@Enumerated(EnumType.STRING)
	@Column
	private Sexo sexo;

	@Column(name = "renda_mensal")
	private Long rendaMensal;

	@Embedded
	private Endereco endereco;

	@Column(name = "criado_em", nullable = false, updatable = false)
	private LocalDateTime criadoEm;

	@Column(name = "criado_por", nullable = false, updatable = false)
	private Long criadoPor;

	@Column(name = "atualizado_em")
	private LocalDateTime atualizadoEm;

	@Column(name = "atualizado_por")
	private Long atualizadoPor;

	@PrePersist
	public void prePersist() {
		this.criadoEm = LocalDateTime.now();
	}

	@PreUpdate
	public void preUpdate() {
		this.atualizadoEm = LocalDateTime.now();
	}

	public int getIdade() {
		if (this.dataNascimento == null) return 0;
		return Period.between(this.dataNascimento, LocalDate.now()).getYears();
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public Familia getFamilia() { return familia; }
	public void setFamilia(Familia familia) { this.familia = familia; }

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public String getCpf() { return cpf; }
	public void setCpf(String cpf) { this.cpf = cpf; }

	public String getNis() { return nis; }
	public void setNis(String nis) { this.nis = nis; }

	public String getNumeroRg() { return numeroRg; }
	public void setNumeroRg(String numeroRg) { this.numeroRg = numeroRg; }

	public String getOrgaoExpeditorRg() { return orgaoExpeditorRg; }
	public void setOrgaoExpeditorRg(String orgaoExpeditorRg) { this.orgaoExpeditorRg = orgaoExpeditorRg; }

	public LocalDate getDataExpedicaoRg() { return dataExpedicaoRg; }
	public void setDataExpedicaoRg(LocalDate dataExpedicaoRg) { this.dataExpedicaoRg = dataExpedicaoRg; }

	public LocalDate getDataNascimento() { return dataNascimento; }
	public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

	public boolean isReferencia() { return isReferencia; }
	public void setReferencia(boolean isReferencia) { this.isReferencia = isReferencia; }

	public Parentesco getParentesco() { return parentesco; }
	public void setParentesco(Parentesco parentesco) { this.parentesco = parentesco; }

	public String getTelefone() { return telefone; }
	public void setTelefone(String telefone) { this.telefone = telefone; }

	public Sexo getSexo() { return sexo; }
	public void setSexo(Sexo sexo) { this.sexo = sexo; }

	public Long getRendaMensal() { return rendaMensal; }
	public void setRendaMensal(Long rendaMensal) { this.rendaMensal = rendaMensal; }

	public Endereco getEndereco() { return endereco; }
	public void setEndereco(Endereco endereco) { this.endereco = endereco; }

	public LocalDateTime getCriadoEm() { return criadoEm; }
	public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

	public Long getCriadoPor() { return criadoPor; }
	public void setCriadoPor(Long criadoPor) { this.criadoPor = criadoPor; }

	public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
	public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }

	public Long getAtualizadoPor() { return atualizadoPor; }
	public void setAtualizadoPor(Long atualizadoPor) { this.atualizadoPor = atualizadoPor; }

	@Override
	public int hashCode() { return Objects.hash(id); }

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Pessoa other = (Pessoa) obj;
		return Objects.equals(id, other.id);
	}
}
