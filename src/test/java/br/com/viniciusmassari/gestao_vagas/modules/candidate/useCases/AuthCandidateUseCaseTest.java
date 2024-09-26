package br.com.viniciusmassari.gestao_vagas.modules.candidate.useCases;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;

import java.util.Optional;
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

}
