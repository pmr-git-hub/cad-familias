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
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;
    @Value("${cors.allow-headers}")
    private String allowedHeaders;
    @Value("${cors.allow-methods}")
    private String allowedMethods;

    public FiltroTokenAcesso(TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        response.setHeader("Access-Control-Allow-Origin", allowedOrigins);
        if (OPTION_METHOD.equalsIgnoreCase(request.getMethod())) {
            response.setHeader("Access-Control-Allow-Methods", allowedMethods);
            response.setHeader("Access-Control-Allow-Headers", allowedHeaders);
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        String requestURI = request.getRequestURI();
        if (requestURI.equals(AUTH_LOGOUT) || requestURI.equals(AUTH_LOGIN) || requestURI.equals(AUTH_ATUALIZAR_TOKEN)) {
            filterChain.doFilter(request, response);
            return;
        }

        try{
            String token = recuperarTokenRequisicao(request);
            if(token != null){
                String username = tokenService.validarToken(token);
                if(username == null){
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Retorna 401
                    return;
                }
                Usuario usuario = usuarioRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new UsuarioOuSenhaInvalidoException("Usuário ou senha inválidos."));
                request.setAttribute("usuarioId", usuario.getId());
                Authentication authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Retorna 401
                return;
            }
        } catch (JWTVerificationException e){
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(
                    String.format("{\"error\": \"%s\", \"message\": \"%s\", \"status\": %d}",
                            "Token inválido ou expirado",
                            e.getMessage(),
                            HttpServletResponse.SC_UNAUTHORIZED)
            );
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Retorna 401
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String recuperarTokenRequisicao(HttpServletRequest request) throws JWTVerificationException {
        String authToken = request.getHeader(AUTH_TOKEN);
        if (authToken != null) {
            try {
                tokenService.validarToken(authToken); // Valida assinatura, issuer, etc.
                if (!tokenService.isTokenExpirado(authToken)) {
                    return authToken;
                }
            } catch (JWTVerificationException e) {
                return null;
            }
        }
        return null;
    }
}
