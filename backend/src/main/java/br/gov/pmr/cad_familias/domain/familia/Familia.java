package br.gov.pmr.cad_familias.domain.familia;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "familia")
@Setter
@Getter
public class Familia implements Serializable {

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

}
