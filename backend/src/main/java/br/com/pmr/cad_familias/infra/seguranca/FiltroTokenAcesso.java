package br.com.pmr.cad_familias.infra.seguranca;

import br.com.pmr.cad_familias.domain.Usuario;
import br.com.pmr.cad_familias.repository.UsuarioRepository;
import br.com.pmr.cad_familias.service.TokenService;
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

import static br.com.pmr.cad_familias.util.Constantes.*;

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
                //validação do token
                String username = tokenService.validarToken(token);
                if(username == null){
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Retorna 401
                    return;
                }
                Usuario usuario = usuarioRepository.findByUsernameIgnoreCase(username).orElseThrow();//TODO exceção mais clara
                Authentication authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Retorna 401
                return;
            }
        } catch (JWTVerificationException e){
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"Token inválido ou expirado.\"}");//TODO considerar enviar um json
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Retorna 401
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String recuperarTokenRequisicao(HttpServletRequest request) throws JWTVerificationException {

        String authToken = request.getHeader(AUTH_TOKEN);
        if(authToken != null && !tokenService.isTokenExpirado(authToken)){
            return authToken;
        }
        return null;
    }
}
