package br.gov.pmr.cad_familias.domain.equipamento;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "equipamento")
@Setter
@Getter
public class Equipamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 300)
    @Column(nullable = false, length = 300)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEquipamento tipo;

    @Size(max = 9)
    @Column(length = 9)
    private String cep;

    @Size(max = 255)
    @Column
    private String logradouro;

    @Size(max = 20)
    @Column
    private String numero;

    @Size(max = 255)
    @Column
    private String complemento;

    @Size(max = 255)
    @Column
    private String bairro;

    @Size(max = 255)
    @Column
    private String cidade;

    @Size(max = 2)
    @Column(length = 2)
    private String estado;

    @Size(max = 20)
    @Column
    private String telefone;

    @Size(max = 255)
    @Column
    private String email;

    @Column(nullable = false)
    private boolean ativo = true;

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
