package br.com.viniciusmassari.gestao_vagas.modules.candidate.useCases;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.viniciusmassari.gestao_vagas.exceptions.JobNotFoundException;
import br.com.viniciusmassari.gestao_vagas.exceptions.UserNotFoundException;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.entity.CandidateEntity;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.repository.ApplyJobRepository;
import br.com.viniciusmassari.gestao_vagas.modules.company.entities.JobEntity;
import br.com.viniciusmassari.gestao_vagas.modules.company.repositories.JobRepository;

@ExtendWith(MockitoExtension.class)
public class ApplyJobUseCaseTest {

    @InjectMocks
    private ApplyJobUseCase applyJobUseCase;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private ApplyJobRepository applyJobRepository;

    @Test
    @DisplayName("Should not be able to apply job with candidate not found ❌")
    public void should_not_be_able_to_apply_job_with_candidate_not_found() {
        try {
            applyJobUseCase.execute(UUID.randomUUID(), UUID.randomUUID());

        } catch (Exception e) {
            assertInstanceOf(UserNotFoundException.class, e);
        }
    }

    @Test
    @DisplayName("Should not be able to apply job with job not found ❌")
    public void should_not_be_able_to_apply_job_with_job_not_found() {
        UUID randomJobUUID = UUID.randomUUID();
        UUID candidateId = UUID.randomUUID();
        CandidateEntity candidate = CandidateEntity.builder().id(candidateId).build();
        when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(candidate));

        try {
            applyJobUseCase.execute(candidateId, randomJobUUID);

        } catch (Exception e) {
            assertInstanceOf(JobNotFoundException.class, e);
        }
    }

    @Test
    @DisplayName("Should be able to apply to a job ")
    public void should_create_a_new_apply_job() {
        UUID candidateId = UUID.randomUUID();
        CandidateEntity candidate = CandidateEntity.builder().id(candidateId).build();
        when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(candidate));

        UUID jobId = UUID.randomUUID();
        JobEntity job = JobEntity.builder().id(jobId).build();
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));

        assertDoesNotThrow(() -> {
            applyJobUseCase.execute(candidateId, jobId);
        });

    }

}
