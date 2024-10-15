package br.com.viniciusmassari.gestao_vagas.modules.company.useCases;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.annotation.Description;

import br.com.viniciusmassari.gestao_vagas.modules.company.entities.CompanyEntity;
import br.com.viniciusmassari.gestao_vagas.modules.company.entities.JobEntity;
import br.com.viniciusmassari.gestao_vagas.modules.company.repositories.JobRepository;

@RunWith(MockitoJUnitRunner.class)
public class ListAllJobsByCompanyTest {
    @InjectMocks
    ListAllJobsByCompanyUseCase listAllJobsByCompanyUseCase;

    @Mock
    JobRepository jobRepository;

    @Description("Should return a job list")
    @Test
    public void should_return_a_job_list() {
        UUID companyId = UUID.randomUUID();
        CompanyEntity company = CompanyEntity.builder().id(companyId).email("company@email.com").name("company_test")
                .build();
        JobEntity job = JobEntity.builder().id(UUID.randomUUID()).companyId(companyId).companyEntity(company)
                .description("description test").title("level test").build();
        JobEntity job2 = JobEntity.builder().id(UUID.randomUUID()).companyId(companyId).companyEntity(company)
                .description("description test").title("level test").build();
        List<JobEntity> jobList = new ArrayList<JobEntity>();
        jobList.add(job);
        jobList.add(job2);

        when(jobRepository.findByCompanyId(companyId)).thenReturn(jobList);

        assertDoesNotThrow(() -> {
            var response = listAllJobsByCompanyUseCase.execute(companyId);
            assertEquals("return a list with 2 items", response.size(), 2);
        });

    }

    @Description("Should return an empty job list")
    @Test
    public void should_return_an_empty_job_list() {
        UUID companyId = UUID.randomUUID();

        List<JobEntity> jobList = new ArrayList<JobEntity>();

        when(jobRepository.findByCompanyId(companyId)).thenReturn(jobList);

        assertDoesNotThrow(() -> {
            var response = listAllJobsByCompanyUseCase.execute(companyId);
            assertEquals("return a list with 0 items", response.size(), 0);
        });

    }
}
