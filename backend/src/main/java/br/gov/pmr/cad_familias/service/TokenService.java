package br.gov.pmr.cad_familias.service;

import br.gov.pmr.cad_familias.domain.usuario.Usuario;
import br.gov.pmr.cad_familias.repository.usuario.UsuarioRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final UsuarioRepository usuarioRepository;

    public TokenService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public String gerarToken(Usuario usuario){
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            return JWT.create()
                    .withIssuer("Cad famlias")
                    .withSubject(usuario.getUsername())
                    .withExpiresAt(expiracao(480))
                    .sign(algorithm);
        } catch (JWTCreationException e){
            e.printStackTrace();//TODO TRATAR COM EXCEÇÃO ADEQUADA
        }
        return null;
    }

    public String gerarRefreshToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            return JWT.create()
                    .withIssuer("Cad famlias")
                    .withSubject(usuario.getId().toString())
                    .withExpiresAt(expiracao(10080))
                    .sign(algorithm);
        } catch (JWTCreationException e){
            e.printStackTrace();//TODO TRATAR COM EXCEÇÃO ADEQUADA
        }
        return null;
    }

    private Instant expiracao(Integer minutos) {
        return LocalDateTime.now().plusMinutes(minutos).toInstant(ZoneOffset.of("-03:00"));
    }

    public String validarToken(String token) throws JWTVerificationException{
        DecodedJWT decodedJWT;
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("Cad famlias")
                .build();
        decodedJWT = verifier.verify(token);
        return decodedJWT.getSubject();
    }

    public boolean isTokenExpirado(String token) throws JWTVerificationException {
        DecodedJWT decodedJWT = JWT.decode(token);
        if (decodedJWT.getExpiresAt() != null && decodedJWT.getExpiresAt().before(new java.util.Date())) {
            return true;
        }else {
            return false;
        }

    }
}
