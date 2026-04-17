package br.gov.pmr.cad_familias.domain.servico;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "servicos")
@Getter
@Setter
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "equipamento_id", nullable = false)
    private Long equipamentoId;

    @Column(nullable = false, length = 300)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "publico_alvo", length = 300)
    private String publicoAlvo;

    @Column(name = "faixa_etaria_min")
    private Integer faixaEtariaMin;

    @Column(name = "faixa_etaria_max")
    private Integer faixaEtariaMax;

    @Column(name = "dia_semana", length = 100)
    private String diaSemana;

    @Column(length = 100)
    private String horario;

    @Column(nullable = false)
    private boolean ativo = true;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "criado_por", nullable = false, updatable = false)
    private Long criadoPor;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @Column(name = "atualizado_por")
    private Long atualizadoPor;
}
