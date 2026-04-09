package br.gov.pmr.cad_familias.domain.usuario;

import br.gov.pmr.cad_familias.domain.tecnico.Tecnico;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuario")
@Setter
@Getter
public class Usuario implements UserDetails, Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "tecnico_id", nullable = false, unique = true)
	private Tecnico tecnico;

	@Column(name = "nome_usuario", nullable = false, unique = true)
	private String username;

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

}
