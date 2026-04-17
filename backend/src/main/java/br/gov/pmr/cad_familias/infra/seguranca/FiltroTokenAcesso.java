package br.gov.pmr.cad_familias.infra.seguranca;

import br.gov.pmr.cad_familias.domain.usuario.Usuario;
import br.gov.pmr.cad_familias.excecao.UsuarioOuSenhaInvalidoException;
import br.gov.pmr.cad_familias.repository.usuario.UsuarioRepository;
import br.gov.pmr.cad_familias.service.auth.TokenService;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static br.gov.pmr.cad_familias.util.Constantes.*;

@Component
public class FiltroTokenAcesso extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    public FiltroTokenAcesso(TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/api/auth/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String token = recuperarToken(request);

            if (token == null) {
                responderNaoAutorizado(response, "Token não fornecido");
                return;
            }

            String username = tokenService.validarToken(token);

            if (username == null) {
                responderNaoAutorizado(response, "Token inválido");
                return;
            }

            Usuario usuario = usuarioRepository.findByUsernameIgnoreCase(username)
                    .orElseThrow(() -> new UsuarioOuSenhaInvalidoException("Usuário não encontrado."));

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JWTVerificationException e) {
            responderNaoAutorizado(response, e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        String authToken = request.getHeader(AUTH_TOKEN);
        if (authToken != null && !tokenService.isTokenExpirado(authToken)) {
            return authToken;
        }
        return null;
    }

    private void responderNaoAutorizado(HttpServletResponse response, String mensagem) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(
                String.format("{\"error\": \"Token inválido ou expirado\", \"message\": \"%s\", \"status\": 401}", mensagem)
        );
    }
}
