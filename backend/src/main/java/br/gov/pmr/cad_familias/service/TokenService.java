package br.gov.pmr.cad_familias.service;

import br.gov.pmr.cad_familias.domain.usuario.Usuario;
import br.gov.pmr.cad_familias.excecao.TokenInvalidoException;
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

    private static final String ISSUER = "Cad Familias";

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String gerarToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(usuario.getUsername())
                    .withExpiresAt(expiracao(480))
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new TokenInvalidoException("Erro ao gerar token de acesso.");
        }
    }

    public String gerarRefreshToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(usuario.getId().toString())
                    .withExpiresAt(expiracao(10080))
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new TokenInvalidoException("Erro ao gerar refresh token.");
        }
    }

    public String validarToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getSubject();
    }

    public boolean isTokenExpirado(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getExpiresAt() != null
                && decodedJWT.getExpiresAt().before(new java.util.Date());
    }

    private Instant expiracao(Integer minutos) {
        return LocalDateTime.now().plusMinutes(minutos).toInstant(ZoneOffset.of("-03:00"));
    }
}
