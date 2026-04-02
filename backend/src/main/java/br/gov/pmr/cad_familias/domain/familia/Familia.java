package br.gov.pmr.cad_familias.domain.familia;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "familia")
public class Familia implements Serializable {

	private static final long serialVersionUID = 7660693721478356023L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "codigo_cadunico")
	private String codigoCadunico;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SituacaoFamilia situacao;

	@OneToMany(mappedBy = "familia", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JsonManagedReference
	private List<Pessoa> membrosDaFamilia;

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
		this.situacao = this.situacao != null ? this.situacao : SituacaoFamilia.ATIVA;
	}

	@PreUpdate
	public void preUpdate() {
		this.atualizadoEm = LocalDateTime.now();
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getCodigoCadunico() { return codigoCadunico; }
	public void setCodigoCadunico(String codigoCadunico) { this.codigoCadunico = codigoCadunico; }

	public SituacaoFamilia getSituacao() { return situacao; }
	public void setSituacao(SituacaoFamilia situacao) { this.situacao = situacao; }

	public List<Pessoa> getMembrosDaFamilia() { return membrosDaFamilia; }
	public void setMembrosDaFamilia(List<Pessoa> membrosDaFamilia) { this.membrosDaFamilia = membrosDaFamilia; }

	public LocalDateTime getCriadoEm() { return criadoEm; }
	public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

	public Long getCriadoPor() { return criadoPor; }
	public void setCriadoPor(Long criadoPor) { this.criadoPor = criadoPor; }

	public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
	public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }

	public Long getAtualizadoPor() { return atualizadoPor; }
	public void setAtualizadoPor(Long atualizadoPor) { this.atualizadoPor = atualizadoPor; }
}
