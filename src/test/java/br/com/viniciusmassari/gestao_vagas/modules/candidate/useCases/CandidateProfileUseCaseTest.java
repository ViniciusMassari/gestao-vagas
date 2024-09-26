package br.com.viniciusmassari.gestao_vagas.modules.candidate.useCases;

import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;

import br.com.viniciusmassari.gestao_vagas.exceptions.UserNotFoundException;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.dto.CandidateProfileResponseDTO;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.entity.CandidateEntity;

@RunWith(MockitoJUnitRunner.class)
public class CandidateProfileUseCaseTest {

    @InjectMocks
    private CandidateProfileUseCase candidateProfileUseCase;

    @Mock
    private CandidateRepository candidateRepository;

    @DisplayName("Should not be able to return an user profile if user does not exist")
    @Test
    public void should_not_be_able_to_return_an_user_profile() {
        try {
            UUID randomUserId = UUID.randomUUID();
            this.candidateProfileUseCase.execute(randomUserId);
        } catch (Exception e) {
            assertInstanceOf(UserNotFoundException.class, e);
        }

    }

    @DisplayName("Should be able to return an user profile")
    @Test
    public void should_be_able_to_return_an_user_profile() {

        UUID userId = UUID.randomUUID();
        CandidateEntity candidateEntity = CandidateEntity.builder().id(userId).name("user name").build();

        when(this.candidateRepository.findById(candidateEntity.getId())).thenReturn(Optional.of(candidateEntity));
        assertDoesNotThrow(() -> {
            CandidateProfileResponseDTO candidateProfileResponseDTO;
            candidateProfileResponseDTO = this.candidateProfileUseCase.execute(candidateEntity.getId());
            assertInstanceOf(CandidateProfileResponseDTO.class, candidateProfileResponseDTO);
        });

    }
}
