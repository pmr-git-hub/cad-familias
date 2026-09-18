package br.gov.pmr.cad_familias.domain.gestacao;

import br.gov.pmr.cad_familias.domain.servico.VinculoPessoaServico;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "gestacao")
@Getter
@Setter
public class Gestacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "vinculo_id", nullable = false, unique = true)
    private VinculoPessoaServico vinculo;

    @Column(name = "data_ultima_menstruacao")
    private LocalDate dataUltimaMenstruacao;

    @Column(name = "data_prevista_parto")
    private LocalDate dataPrevistaParto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusGestacao status = StatusGestacao.EM_ACOMPANHAMENTO;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Column(name = "criado_por", nullable = false, updatable = false)
    private Long criadoPor;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @Column(name = "atualizado_por")
    private Long atualizadoPor;
}
