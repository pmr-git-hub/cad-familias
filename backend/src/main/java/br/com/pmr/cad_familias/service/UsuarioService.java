package br.com.pmr.cad_familias.service;

import java.util.List;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.pmr.cad_familias.domain.Usuario;
import br.com.pmr.cad_familias.repository.UsuarioRepository;

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
