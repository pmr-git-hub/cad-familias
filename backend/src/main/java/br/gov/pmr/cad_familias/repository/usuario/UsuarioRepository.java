package br.gov.pmr.cad_familias.repository.usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import br.gov.pmr.cad_familias.domain.usuario.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	public Optional<Usuario> findByUsernameIgnoreCase(String username);
}
 