package br.gov.pmr.cad_familias.domain.gestacao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "gestacao_acompanhamento")
@Getter
@Setter
public class GestacaoAcompanhamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "gestacao_id", nullable = false)
    private Gestacao gestacao;

    @Column(name = "data_registro", nullable = false)
    private LocalDate dataRegistro;

    @Column(name = "semanas_gestacao")
    private Integer semanasGestacao;

    @Column(name = "numero_consultas")
    private Integer numeroConsultas;

    @Column(name = "alto_risco", nullable = false)
    private Boolean altoRisco = false;

    @Column(name = "motivo_alto_risco", length = 500)
    private String motivoAltoRisco;

    @Column(name = "peso_kg", precision = 5, scale = 2)
    private BigDecimal pesoKg;

    @Column(name = "pressao_arterial", length = 20)
    private String pressaoArterial;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Column(name = "criado_por", nullable = false, updatable = false)
    private Long criadoPor;
}
