package br.gov.pmr.cad_familias.service.auth;

import br.gov.pmr.cad_familias.domain.audit.AcaoAudit;
import br.gov.pmr.cad_familias.domain.tecnico.Tecnico;
import br.gov.pmr.cad_familias.domain.usuario.Usuario;
import br.gov.pmr.cad_familias.dto.usuario.AtualizarUsuarioDTO;
import br.gov.pmr.cad_familias.dto.usuario.CriarUsuarioDTO;
import br.gov.pmr.cad_familias.dto.usuario.UsuarioDTO;
import br.gov.pmr.cad_familias.excecao.TecnicoNaoEncontradoException;
import br.gov.pmr.cad_familias.excecao.UsuarioNaoEncontradoException;
import br.gov.pmr.cad_familias.mapper.usuario.UsuarioMapper;
import br.gov.pmr.cad_familias.repository.tecnico.TecnicoRepository;
import br.gov.pmr.cad_familias.repository.usuario.UsuarioRepository;
import br.gov.pmr.cad_familias.service.audit.AuditService;
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
	private final AuditService auditService;

	public UsuarioService(UsuarioRepository usuarioRepository,
						  TecnicoRepository tecnicoRepository,
						  PasswordEncoder encoder,
						  AuditService auditService) {
		this.usuarioRepository = usuarioRepository;
		this.tecnicoRepository = tecnicoRepository;
		this.encoder = encoder;
		this.auditService = auditService;
	}

	public List<UsuarioDTO> listarUsuarios() {
		return UsuarioMapper.listaUsuariosToVO(usuarioRepository.findAll());
	}

	@Transactional
	public UsuarioDTO criarUsuario(CriarUsuarioDTO dto) {
		Tecnico tecnico = tecnicoRepository.findById(dto.getTecnicoId())
				.orElseThrow(TecnicoNaoEncontradoException::new);

		Usuario usuario = UsuarioMapper.criarUsuarioDTOToUsuario(dto, tecnico);
		usuario.setPassword(encoder.encode(dto.getPassword()));

		// ✅ Salva
		Usuario usuarioSalvo = usuarioRepository.save(usuario);

		// ✅ Converte
		UsuarioDTO resultado = UsuarioMapper.usuarioToUsuarioVO(usuarioSalvo);

		// ✅ Auditoria (INSERT) - usuário criado por ele mesmo (auto-registro) ou admin
		// Aqui usamos o próprio usuário criado como "criador" (pode ajustar se tiver admin)
		auditService.registrar(
				"usuario",
				usuarioSalvo.getId(),
				AcaoAudit.INSERT,
				null,
				resultado,
				usuarioSalvo
		);

		return resultado;
	}

	@Transactional
	public UsuarioDTO atualizarUsuario(Long id, AtualizarUsuarioDTO dto) {
		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(UsuarioNaoEncontradoException::new);

		// ✅ Estado ANTES
		UsuarioDTO estadoAnterior = UsuarioMapper.usuarioToUsuarioVO(usuario);

		Tecnico tecnico = tecnicoRepository.findById(dto.getTecnicoId())
				.orElseThrow(TecnicoNaoEncontradoException::new);

		usuario.setUsername(dto.getUsername());
		usuario.setPerfil(dto.getPerfil());
		usuario.setTecnico(tecnico);
		usuario.setAtivo(dto.getAtivo());

		if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
			usuario.setPassword(encoder.encode(dto.getPassword()));
		}

		// ✅ Salva
		Usuario usuarioSalvo = usuarioRepository.save(usuario);

		// ✅ Estado DEPOIS
		UsuarioDTO estadoNovo = UsuarioMapper.usuarioToUsuarioVO(usuarioSalvo);

		// ✅ Auditoria
		auditService.registrar(
				"usuario",
				usuarioSalvo.getId(),
				AcaoAudit.UPDATE,
				estadoAnterior,
				estadoNovo,
				usuarioSalvo // usuário atualiza a si mesmo
		);

		return estadoNovo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return usuarioRepository.findByUsernameIgnoreCase(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
	}
}
