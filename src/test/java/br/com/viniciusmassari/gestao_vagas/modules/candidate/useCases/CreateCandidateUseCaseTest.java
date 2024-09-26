package br.com.viniciusmassari.gestao_vagas.modules.candidate.useCases;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.viniciusmassari.gestao_vagas.exceptions.UserFoundException;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.dto.CreateCandidateResponseDTO;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.entity.CandidateEntity;

@RunWith(MockitoJUnitRunner.class)
public class CreateCandidateUseCaseTest {

    @InjectMocks
    private CreateCandidateUseCase createCandidateUseCase;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @DisplayName("Should not create a new user if username already exists")
    @Test
    public void should_not_create_an_user() {
        String username = "username example";
        CandidateEntity candidateEntity = CandidateEntity.builder().username(username).build();
        when(candidateRepository.findByUsernameOrEmail(username, null)).thenReturn(Optional.of(candidateEntity));
        try {
            createCandidateUseCase.execute(candidateEntity);
        } catch (Exception e) {
            assertInstanceOf(UserFoundException.class, e);
        }
    }

    @DisplayName("Should not create a new user if email already exists")
    @Test
    public void should_not_create_an_user_if_email() {
        String email = "email@example.com";
        CandidateEntity candidateEntity = CandidateEntity.builder().email(email).build();
        when(candidateRepository.findByUsernameOrEmail(null, email)).thenReturn(Optional.of(candidateEntity));
        try {
            createCandidateUseCase.execute(candidateEntity);
        } catch (Exception e) {
            assertInstanceOf(UserFoundException.class, e);
        }
    }

    @DisplayName("Should create a new user")
    @Test
    public void should_create_an_user() {
        String email = "email@example.com";
        String username = "username example";
        String name = "name example";
        String password = "123456";
        UUID userId = UUID.randomUUID();

        CandidateEntity candidateEntity = CandidateEntity.builder().id(userId).name(name).username(username)
                .email(email)
                .password(password).build();
        when(candidateRepository.findByUsernameOrEmail(candidateEntity.getUsername(), candidateEntity.getEmail()))
                .thenReturn(Optional.empty());

        when(candidateRepository.save(candidateEntity)).thenReturn(candidateEntity);

        assertDoesNotThrow(() -> {
            CreateCandidateResponseDTO candidate = createCandidateUseCase.execute(candidateEntity);
            assertInstanceOf(CreateCandidateResponseDTO.class, candidate);
        });
    }

}