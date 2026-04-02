package br.gov.pmr.cad_familias.service.usuario;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.gov.pmr.cad_familias.domain.usuario.Usuario;
import br.gov.pmr.cad_familias.repository.usuario.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService{
	
	private UsuarioRepository usuarioRepository;

	private final PasswordEncoder encoder;

	public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder encoder) {
		this.usuarioRepository = usuarioRepository;
        this.encoder = encoder;
    }

	public List<Usuario> listarUsuarios(){
		return this.usuarioRepository.findAll();
	}

//	public Usuario autenticacao(Usuario usuario) {
//
//		return usuario;
//	}

	public Usuario criarUsuario(Usuario usuario) {
		usuario.setPassword(encoder.encode(usuario.getPassword()));
		return usuarioRepository.save(usuario);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return usuarioRepository.findByUsernameIgnoreCase(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não foi encontrado"));
	}


}
