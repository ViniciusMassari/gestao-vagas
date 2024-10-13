package br.com.viniciusmassari.gestao_vagas.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

@Service
public class TokenUtil {
    public String createToken(String id, List<String> roles, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            Instant expiresIn = Instant.now().plus(Duration.ofHours(2));
            String token = JWT.create().withIssuer("javagas").withSubject(id)
                    .withClaim("roles", roles)
                    .withExpiresAt(expiresIn).sign(algorithm);

            return token;
        } catch (JWTCreationException | IllegalArgumentException e) {
            throw new JWTCreationException("JWT could not be created", e);
        }

    }
}
