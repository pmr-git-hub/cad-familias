package br.gov.pmr.cad_familias.domain.atendimento;

import br.gov.pmr.cad_familias.domain.familia.Pessoa;
import br.gov.pmr.cad_familias.domain.tecnico.Tecnico;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "atendimento")
@Getter
@Setter
public class Atendimento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prontuario_id", nullable = false)
    private Prontuario prontuario;

    @ManyToOne
    @JoinColumn(name = "tecnico_id", nullable = false)
    private Tecnico tecnico;

    @ManyToOne
    @JoinColumn(name = "pessoa_id")
    private Pessoa pessoa;

    // servico_id será adicionado na Fase 4 (quando criarmos a entidade Servico)

    @Column(nullable = false)
    private LocalDateTime data;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAtendimento tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModalidadeAtendimento modalidade;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

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
}
