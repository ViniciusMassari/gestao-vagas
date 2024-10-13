package br.com.viniciusmassari.gestao_vagas.modules.candidate.useCases;

import java.util.Arrays;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.viniciusmassari.gestao_vagas.exceptions.UserNotFoundException;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.dto.AuthCandidateRequestDTO;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.dto.AuthCandidateResponseDTO;
import br.com.viniciusmassari.gestao_vagas.utils.TokenUtil;
import br.com.viniciusmassari.gestao_vagas.utils.UserType;

@Service
public class AuthCandidateUseCase {

        @Value("${security.token.secret.candidate}")
        private String secretKey;

        @Autowired
        PasswordEncoder passwordEncoder;

        @Autowired
        private CandidateRepository candidateRepository;

        @Autowired
        private TokenUtil tokenUtil;

        public AuthCandidateResponseDTO execute(AuthCandidateRequestDTO authCandidateRequestDTO)
                        throws AuthenticationException {
                var candidate = this.candidateRepository.findByUsername(authCandidateRequestDTO.username())
                                .orElseThrow(() -> {
                                        throw new UserNotFoundException();
                                });

                boolean isSamePassword = this.passwordEncoder.matches(authCandidateRequestDTO.password(),
                                candidate.getPassword());

                if (!isSamePassword) {
                        throw new AuthenticationException();
                }
                var roles = Arrays.asList(UserType.CANDIDATE.toString());

                String token = tokenUtil.createToken(candidate.getId().toString(), roles, secretKey);

                AuthCandidateResponseDTO authCandidateResponseDTO = AuthCandidateResponseDTO.builder()
                                .access_token(token)
                                .roles(roles)
                                .build();

                return authCandidateResponseDTO;

        }

};
