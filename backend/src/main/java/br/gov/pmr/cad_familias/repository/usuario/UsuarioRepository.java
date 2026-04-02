// UsuarioRepository.java
package br.gov.pmr.cad_familias.repository.usuario;

import br.gov.pmr.cad_familias.domain.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	Optional<Usuario> findByUsernameIgnoreCase(String username);
	boolean existsByUsername(String username);
}
