package br.com.viniciusmassari.gestao_vagas.modules.candidate.useCases;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Description;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.auth0.jwt.algorithms.Algorithm;

import br.com.viniciusmassari.gestao_vagas.exceptions.UserNotFoundException;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.dto.AuthCandidateRequestDTO;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.dto.AuthCandidateResponseDTO;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.entity.CandidateEntity;
import br.com.viniciusmassari.gestao_vagas.utils.TokenUtil;
import br.com.viniciusmassari.gestao_vagas.utils.UserType;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class AuthCandidateUseCaseTest {

    @Value("${security.token.secret.candidate}")
    private String securityToken;

    @InjectMocks
    private AuthCandidateUseCase authCandidateUseCase;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private TokenUtil tokenUtil;

    @Mock
    Algorithm algorithmMock;

    @Description("Should not authenticate an user if user does not exist")
    @Test
    public void should_not_authenticate_user_if_user_does_not_exist() {
        AuthCandidateRequestDTO authCandidateRequestDTO = new AuthCandidateRequestDTO("username", "123456");
        when(candidateRepository.findByUsername(authCandidateRequestDTO.username())).thenReturn(Optional.empty());
        try {
            authCandidateUseCase.execute(authCandidateRequestDTO);
        } catch (Exception e) {
            assertInstanceOf(UserNotFoundException.class, e);
        }
    }

    @Test
    public void assertNot() {
        assertNotNull(this.securityToken);
    }

    @Test
    public void should_auth_candidate() {
        String rawPassword = "123456";
        CandidateEntity candidateEntity = CandidateEntity.builder().id(UUID.randomUUID()).username("username")
                .password(UUID.randomUUID().toString()).build();
        AuthCandidateRequestDTO authCandidateRequestDTO = new AuthCandidateRequestDTO(candidateEntity.getUsername(),
                rawPassword);
        when(candidateRepository.findByUsername(candidateEntity.getUsername()))
                .thenReturn(Optional.of(candidateEntity));
        when(passwordEncoder.matches(rawPassword, candidateEntity.getPassword())).thenReturn(true);

        when(tokenUtil.createToken(candidateEntity.getId().toString(), UserType.CANDIDATE, "mysecret"))
                .thenReturn("token");

        assertDoesNotThrow(() -> {
            var response = authCandidateUseCase.execute(authCandidateRequestDTO);
            assertInstanceOf(AuthCandidateResponseDTO.class, response);
        });
    }

}
