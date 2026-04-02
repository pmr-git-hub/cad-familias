package br.gov.pmr.cad_familias.VO.usuario;

import br.gov.pmr.cad_familias.VO.tecnico.TecnicoResumoVO;
import br.gov.pmr.cad_familias.domain.usuario.Perfil;

import java.io.Serializable;
import java.time.LocalDateTime;

public class UsuarioVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String nome;
    private Perfil perfil;
    private boolean ativo;
    private LocalDateTime ultimoAcesso;
    private TecnicoResumoVO tecnico;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Perfil getPerfil() { return perfil; }
    public void setPerfil(Perfil perfil) { this.perfil = perfil; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public LocalDateTime getUltimoAcesso() { return ultimoAcesso; }
    public void setUltimoAcesso(LocalDateTime ultimoAcesso) { this.ultimoAcesso = ultimoAcesso; }

    public TecnicoResumoVO getTecnico() { return tecnico; }
    public void setTecnico(TecnicoResumoVO tecnico) { this.tecnico = tecnico; }
}
