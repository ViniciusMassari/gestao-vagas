package br.com.viniciusmassari.gestao_vagas.modules.candidate.useCases;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.viniciusmassari.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.dto.AuthCandidateRequestDTO;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.dto.AuthCandidateResponseDTO;

@Service
public class AuthCandidateUseCase {

        @Value("${security.token.secret.candidate}")
        private String secretKey;

        @Autowired
        PasswordEncoder passwordEncoder;

        @Autowired
        private CandidateRepository candidateRepository;

        public AuthCandidateResponseDTO execute(AuthCandidateRequestDTO authCandidateRequestDTO)
                        throws AuthenticationException {
                var candidate = this.candidateRepository.findByUsername(authCandidateRequestDTO.username())
                                .orElseThrow(() -> {
                                        throw new UsernameNotFoundException("Username ou password incorretos");
                                });

                boolean isSamePassword = this.passwordEncoder.matches(authCandidateRequestDTO.password(),
                                candidate.getPassword());

                if (!isSamePassword) {
                        throw new AuthenticationException();
                }

                Instant expiresIn = Instant.now()
                                .plus(Duration.ofHours(2));
                Algorithm algorithm = Algorithm.HMAC256(this.secretKey);
                String token = JWT.create()
                                .withIssuer("javagas")
                                .withSubject(candidate.getId()
                                                .toString())
                                .withExpiresAt(expiresIn)
                                .withClaim("roles", Arrays.asList("CANDIDATE"))
                                .sign(algorithm);

                AuthCandidateResponseDTO authCandidateResponseDTO = AuthCandidateResponseDTO.builder()
                                .access_token(token).expires_in(
                                                expiresIn.toEpochMilli())
                                .build();

                return authCandidateResponseDTO;

        }

};
