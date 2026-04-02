package br.gov.pmr.cad_familias.domain.usuario;

import br.gov.pmr.cad_familias.domain.tecnico.Tecnico;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario implements UserDetails, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "tecnico_id", nullable = false, unique = true)
	private Tecnico tecnico;

	@Column(name = "nome_usuario", nullable = false, unique = true)
	private String username;

	@Column
	private String nome;

	@Column(name = "senha", nullable = false)
	private String password;

	@Column(nullable = false)
	private boolean ativo = true;

	@Column(name = "ultimo_acesso")
	private LocalDateTime ultimoAcesso;

	@Column(name = "criado_em", nullable = false, updatable = false)
	private LocalDateTime criadoEm;

	@Column(name = "atualizado_em")
	private LocalDateTime atualizadoEm;

	@Column
	@Enumerated(EnumType.STRING)
	private Perfil perfil;

	@Transient
	private String token;

	@Transient
	private String refreshToken;

	@PrePersist
	public void prePersist() {
		this.criadoEm = LocalDateTime.now();
	}

	@PreUpdate
	public void preUpdate() {
		this.atualizadoEm = LocalDateTime.now();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (this.perfil == null) return List.of();
		return List.of(new SimpleGrantedAuthority(this.perfil.name()));
	}

	@Override
	public boolean isAccountNonExpired() { return true; }

	@Override
	public boolean isAccountNonLocked() { return true; }

	@Override
	public boolean isCredentialsNonExpired() { return true; }

	@Override
	public boolean isEnabled() { return ativo; }

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public Tecnico getTecnico() { return tecnico; }
	public void setTecnico(Tecnico tecnico) { this.tecnico = tecnico; }

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }

	public boolean isAtivo() { return ativo; }
	public void setAtivo(boolean ativo) { this.ativo = ativo; }

	public LocalDateTime getUltimoAcesso() { return ultimoAcesso; }
	public void setUltimoAcesso(LocalDateTime ultimoAcesso) { this.ultimoAcesso = ultimoAcesso; }

	public LocalDateTime getCriadoEm() { return criadoEm; }
	public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

	public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
	public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }

	public Perfil getPerfil() { return perfil; }
	public void setPerfil(Perfil perfil) { this.perfil = perfil; }

	public String getToken() { return token; }
	public void setToken(String token) { this.token = token; }

	public String getRefreshToken() { return refreshToken; }
	public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}
