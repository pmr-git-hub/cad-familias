package br.gov.pmr.cad_familias.controller.auth;

import br.gov.pmr.cad_familias.domain.usuario.DadosLogin;
import br.gov.pmr.cad_familias.domain.usuario.LoginResponseDTO;
import br.gov.pmr.cad_familias.domain.usuario.Usuario;
import br.gov.pmr.cad_familias.excecao.UsuarioOuSenhaInvalidoException;
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

	public AutenticacaoController(AuthenticationManager authenticationManager, TokenService tokenService, UsuarioService usuarioService, UsuarioRepository usuarioRepository) {
		this.authenticationManager = authenticationManager;
		this.tokenService = tokenService;
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
    }

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> efetuarLogin(@Valid @RequestBody DadosLogin dados, HttpServletResponse response){
		try{

			var autenticationToken = new UsernamePasswordAuthenticationToken(dados.nomeUsuario(), dados.senha());
			var authentication = authenticationManager.authenticate(autenticationToken);

			Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
			usuarioLogado.setPassword(null);

			String tokenAcesso = tokenService.gerarToken(usuarioLogado);
			String refreshToken = tokenService.gerarRefreshToken((Usuario) authentication.getPrincipal());

			LoginResponseDTO loginResponseDTO = new LoginResponseDTO(
					usuarioLogado.getId(),
					usuarioLogado.getUsername(),
					tokenAcesso,
					refreshToken
			);


			return ResponseEntity.ok(loginResponseDTO);
		} catch (AuthenticationException ex) {
			throw new UsuarioOuSenhaInvalidoException();
		}
	}
	@PostMapping(value = "/criarUsuario", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Usuario> criarUsuario(@RequestBody Usuario usuario) {
		return ResponseEntity.ok().body(this.usuarioService.criarUsuario(usuario));
	}

	@GetMapping("/logout")
	public ResponseEntity logout(HttpServletRequest request, HttpServletResponse response){
		return ResponseEntity.ok("Logout realizado com sucesso");
	}
	@GetMapping(value = "/isLogado")
	public ResponseEntity<Usuario> verificarUsuarioLogado(HttpServletRequest request){
		String token = request.getHeader(AUTH_TOKEN);
		String username = tokenService.validarToken(token);
		Usuario usuario = usuarioRepository.findByUsernameIgnoreCase(username).orElseThrow();
		usuario.setPassword(null);
		return ResponseEntity.ok().body(usuario);
	}

	@PostMapping(value = "/atualizar-token", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Usuario> atualizarToken(@Valid @RequestBody Usuario usuarioLogado){
		String refreshToken = usuarioLogado.getRefreshToken();
		String authToken = "";
		String novoRefreshToken = "";
		try {
			if(refreshToken != null && !tokenService.isTokenExpirado(refreshToken)){
				Long id = Long.valueOf(tokenService.validarToken(refreshToken));
				usuarioLogado = usuarioRepository.findById(id).orElseThrow();
				authToken = tokenService.gerarToken(usuarioLogado);
				novoRefreshToken = tokenService.gerarRefreshToken(usuarioLogado);
				usuarioLogado.setToken(authToken);
				usuarioLogado.setRefreshToken(novoRefreshToken);
			} else {
				return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(null);
			}
			return ResponseEntity.ok(usuarioLogado);

		} catch (JWTVerificationException e){
			return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(null);
		}
	}
}


