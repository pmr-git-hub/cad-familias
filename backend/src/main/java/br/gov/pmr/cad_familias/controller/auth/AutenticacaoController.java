package br.gov.pmr.cad_familias.controller.auth;

import br.gov.pmr.cad_familias.VO.usuario.CriarUsuarioDTO;
import br.gov.pmr.cad_familias.VO.usuario.UsuarioVO;
import br.gov.pmr.cad_familias.domain.usuario.DadosLogin;
import br.gov.pmr.cad_familias.domain.usuario.LoginResponseDTO;
import br.gov.pmr.cad_familias.domain.usuario.Usuario;
import br.gov.pmr.cad_familias.excecao.UsuarioOuSenhaInvalidoException;
import br.gov.pmr.cad_familias.mapper.usuario.UsuarioMapper;
import br.gov.pmr.cad_familias.repository.usuario.UsuarioRepository;
import br.gov.pmr.cad_familias.service.TokenService;
import br.gov.pmr.cad_familias.service.usuario.UsuarioService;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import static br.gov.pmr.cad_familias.util.Constantes.AUTH_TOKEN;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

	private final AuthenticationManager authenticationManager;
	private final TokenService tokenService;
	private final UsuarioService usuarioService;
	private final UsuarioRepository usuarioRepository;

	public AutenticacaoController(AuthenticationManager authenticationManager,
								  TokenService tokenService,
								  UsuarioService usuarioService,
								  UsuarioRepository usuarioRepository) {
		this.authenticationManager = authenticationManager;
		this.tokenService = tokenService;
		this.usuarioService = usuarioService;
		this.usuarioRepository = usuarioRepository;
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> efetuarLogin(
			@Valid @RequestBody DadosLogin dados) {
		try {
			var authToken = new UsernamePasswordAuthenticationToken(dados.nomeUsuario(), dados.senha());
			var authentication = authenticationManager.authenticate(authToken);

			Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

			String tokenAcesso = tokenService.gerarToken(usuarioLogado);
			String refreshToken = tokenService.gerarRefreshToken(usuarioLogado);

			return ResponseEntity.ok(new LoginResponseDTO(
					usuarioLogado.getId(),
					usuarioLogado.getUsername(),
					tokenAcesso,
					refreshToken
			));
		} catch (AuthenticationException ex) {
			throw new UsuarioOuSenhaInvalidoException();
		}
	}

	@PostMapping(
			value = "/criarUsuario",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<UsuarioVO> criarUsuario(@Valid @RequestBody CriarUsuarioDTO dto) {
		return ResponseEntity.status(201).body(usuarioService.criarUsuario(dto));
	}

	@GetMapping("/logout")
	public ResponseEntity<String> logout() {
		// JWT é stateless — logout é responsabilidade do cliente (descartar o token)
		return ResponseEntity.ok("Logout realizado com sucesso.");
	}

	@GetMapping("/isLogado")
	public ResponseEntity<UsuarioVO> verificarUsuarioLogado(HttpServletRequest request) {
		String token = request.getHeader(AUTH_TOKEN);
		String username = tokenService.validarToken(token);
		Usuario usuario = usuarioRepository.findByUsernameIgnoreCase(username).orElseThrow();
		return ResponseEntity.ok(UsuarioMapper.usuarioToUsuarioVO(usuario));
	}

	@PostMapping(
			value = "/atualizar-token",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<LoginResponseDTO> atualizarToken(@RequestBody LoginResponseDTO dto) {
		try {
			if (dto.refreshToken() == null || tokenService.isTokenExpirado(dto.refreshToken())) {
				return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
			}

			Long id = Long.valueOf(tokenService.validarToken(dto.refreshToken()));
			Usuario usuario = usuarioRepository.findById(id).orElseThrow();

			String novoToken = tokenService.gerarToken(usuario);
			String novoRefreshToken = tokenService.gerarRefreshToken(usuario);

			return ResponseEntity.ok(new LoginResponseDTO(
					usuario.getId(),
					usuario.getUsername(),
					novoToken,
					novoRefreshToken
			));
		} catch (JWTVerificationException e) {
			return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
		}
	}
}
