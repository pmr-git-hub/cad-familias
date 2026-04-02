package br.gov.pmr.cad_familias.service.usuario;

import br.gov.pmr.cad_familias.VO.usuario.CriarUsuarioDTO;
import br.gov.pmr.cad_familias.VO.usuario.UsuarioVO;
import br.gov.pmr.cad_familias.domain.tecnico.Tecnico;
import br.gov.pmr.cad_familias.domain.usuario.Usuario;
import br.gov.pmr.cad_familias.excecao.TecnicoNaoEncontradoException;
import br.gov.pmr.cad_familias.mapper.usuario.UsuarioMapper;
import br.gov.pmr.cad_familias.repository.tecnico.TecnicoRepository;
import br.gov.pmr.cad_familias.repository.usuario.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService implements UserDetailsService {

	private final UsuarioRepository usuarioRepository;
	private final TecnicoRepository tecnicoRepository;
	private final PasswordEncoder encoder;

	public UsuarioService(UsuarioRepository usuarioRepository,
						  TecnicoRepository tecnicoRepository,
						  PasswordEncoder encoder) {
		this.usuarioRepository = usuarioRepository;
		this.tecnicoRepository = tecnicoRepository;
		this.encoder = encoder;
	}

	public List<UsuarioVO> listarUsuarios() {
		return UsuarioMapper.listaUsuariosToVO(usuarioRepository.findAll());
	}

	@Transactional
	public UsuarioVO criarUsuario(CriarUsuarioDTO dto) {
		Tecnico tecnico = tecnicoRepository.findById(dto.getTecnicoId())
				.orElseThrow(TecnicoNaoEncontradoException::new);

		Usuario usuario = UsuarioMapper.criarUsuarioDTOToUsuario(dto, tecnico);
		usuario.setPassword(encoder.encode(dto.getPassword()));

		return UsuarioMapper.usuarioToUsuarioVO(usuarioRepository.save(usuario));
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return usuarioRepository.findByUsernameIgnoreCase(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
	}
}
