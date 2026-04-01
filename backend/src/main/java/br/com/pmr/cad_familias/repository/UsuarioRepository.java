package br.com.pmr.cad_familias.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pmr.cad_familias.domain.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	public Optional<Usuario> findByUsernameIgnoreCase(String username);
}
