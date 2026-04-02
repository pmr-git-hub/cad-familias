package br.gov.pmr.cad_familias.VO.usuario;

import br.gov.pmr.cad_familias.domain.usuario.Perfil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class CriarUsuarioDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    private String username;

    @NotBlank
    private String nome;

    @NotBlank
    private String password;

    @NotNull
    private Perfil perfil;

    @NotNull
    private Long tecnicoId;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Perfil getPerfil() { return perfil; }
    public void setPerfil(Perfil perfil) { this.perfil = perfil; }

    public Long getTecnicoId() { return tecnicoId; }
    public void setTecnicoId(Long tecnicoId) { this.tecnicoId = tecnicoId; }
}
