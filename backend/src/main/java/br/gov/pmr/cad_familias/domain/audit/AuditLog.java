package br.gov.pmr.cad_familias.domain.audit;

import br.gov.pmr.cad_familias.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
@Getter
@Setter
public class AuditLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String tabela;

    @Column(name = "registro_id", nullable = false)
    private Long registroId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AcaoAudit acao;

    @Column(name = "dados_antes", columnDefinition = "JSON")
    private String dadosAntes;

    @Column(name = "dados_depois", columnDefinition = "JSON")
    private String dadosDepois;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "feito_em", nullable = false, updatable = false)
    private LocalDateTime feitoEm;

    @PrePersist
    public void prePersist() {
        this.feitoEm = LocalDateTime.now();
    }

}
