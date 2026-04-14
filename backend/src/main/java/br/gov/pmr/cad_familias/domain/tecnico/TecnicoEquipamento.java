package br.gov.pmr.cad_familias.domain.tecnico;

import br.gov.pmr.cad_familias.domain.equipamento.Equipamento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tecnico_equipamento",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_tecnico_equipamento_ativo",
                columnNames = {"tecnico_id", "equipamento_id", "ativo"}
        ))
@Getter
@Setter
public class TecnicoEquipamento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tecnico_id", nullable = false)
    private Tecnico tecnico;

    @ManyToOne
    @JoinColumn(name = "equipamento_id", nullable = false)
    private Equipamento equipamento;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "criado_por", nullable = false, updatable = false)
    private Long criadoPor;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }
}
