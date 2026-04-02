package br.gov.pmr.cad_familias.domain.audit;

import br.gov.pmr.cad_familias.domain.usuario.Usuario;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTabela() { return tabela; }
    public void setTabela(String tabela) { this.tabela = tabela; }

    public Long getRegistroId() { return registroId; }
    public void setRegistroId(Long registroId) { this.registroId = registroId; }

    public AcaoAudit getAcao() { return acao; }
    public void setAcao(AcaoAudit acao) { this.acao = acao; }

    public String getDadosAntes() { return dadosAntes; }
    public void setDadosAntes(String dadosAntes) { this.dadosAntes = dadosAntes; }

    public String getDadosDepois() { return dadosDepois; }
    public void setDadosDepois(String dadosDepois) { this.dadosDepois = dadosDepois; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public LocalDateTime getFeitoEm() { return feitoEm; }
    public void setFeitoEm(LocalDateTime feitoEm) { this.feitoEm = feitoEm; }
}
