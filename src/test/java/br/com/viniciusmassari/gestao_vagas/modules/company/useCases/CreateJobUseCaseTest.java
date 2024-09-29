package br.com.viniciusmassari.gestao_vagas.modules.company.useCases;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.annotation.Description;

import br.com.viniciusmassari.gestao_vagas.exceptions.CompanyNotFoundException;
import br.com.viniciusmassari.gestao_vagas.modules.company.dto.CreateJobResponseDTO;
import br.com.viniciusmassari.gestao_vagas.modules.company.entities.CompanyEntity;
import br.com.viniciusmassari.gestao_vagas.modules.company.entities.JobEntity;
import br.com.viniciusmassari.gestao_vagas.modules.company.repositories.CompanyRepository;
import br.com.viniciusmassari.gestao_vagas.modules.company.repositories.JobRepository;

@RunWith(MockitoJUnitRunner.class)
public class CreateJobUseCaseTest {

    @InjectMocks
    private CreateJobUseCase createJobUseCase;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Description("Should not create a job if company does not exists")
    @Test
    public void should_not_create_job_if_company_does_not_exists() {
        JobEntity job = JobEntity.builder().id(UUID.randomUUID()).build();
        when(companyRepository.findById(job.getCompanyId())).thenReturn(Optional.empty());

        try {
            createJobUseCase.execute(job);
        } catch (Exception e) {
            assertInstanceOf(CompanyNotFoundException.class, e);
        }
    }

    @Description("Should create a job")
    @Test
    public void should_create_job() {
        CompanyEntity company = CompanyEntity.builder().id(UUID.randomUUID()).build();
        JobEntity job = JobEntity.builder().id(UUID.randomUUID()).companyId(company.getId()).benefits("benefits")
                .title("title").description("description").level("level").build();

        when(companyRepository.findById(job.getCompanyId())).thenReturn(Optional.of(company));
        when(jobRepository.save(job)).thenReturn(job);
        assertDoesNotThrow(() -> {
            var response = createJobUseCase.execute(job);
            assertInstanceOf(CreateJobResponseDTO.class, response);
        });
    }
}
