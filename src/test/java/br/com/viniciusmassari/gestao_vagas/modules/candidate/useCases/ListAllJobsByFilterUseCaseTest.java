package br.com.viniciusmassari.gestao_vagas.modules.candidate.useCases;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.annotation.Description;

import br.com.viniciusmassari.gestao_vagas.modules.company.entities.JobEntity;
import br.com.viniciusmassari.gestao_vagas.modules.company.repositories.JobRepository;

@RunWith(MockitoJUnitRunner.class)
public class ListAllJobsByFilterUseCaseTest {

    @InjectMocks
    ListAllJobsByFilterUseCase listAllJobsByFilterUseCase;

    @Mock
    private JobRepository jobRepository;

    @Description("Should return a list with 2 jobs with 'Java' filter")
    @Test
    public void should_return_jobs() {
        String filter = "java";
        JobEntity firstJob = JobEntity.builder().description("Desenvolvedor Java").build();
        JobEntity secondJob = JobEntity.builder().description("Desenvolvedor Java").build();
        List<JobEntity> jobEntityList = new ArrayList<>();
        jobEntityList.add(firstJob);
        jobEntityList.add(secondJob);

        when(jobRepository.findByDescriptionContainingIgnoreCase(filter)).thenReturn(jobEntityList);

        assertDoesNotThrow(() -> {
            listAllJobsByFilterUseCase.execute(filter);
        });
    }

    @Description("Should return an list empty list with java filter")
    @Test
    public void should_return_empty_job_list() {
        String filter = "java";

        List<JobEntity> jobEntityList = new ArrayList<>();

        when(jobRepository.findByDescriptionContainingIgnoreCase(filter)).thenReturn(jobEntityList);

        assertDoesNotThrow(() -> {
            listAllJobsByFilterUseCase.execute(filter);
        });
    }

}
