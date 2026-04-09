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
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pessoa")
@Setter
@Getter
public class Pessoa implements Serializable {
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
