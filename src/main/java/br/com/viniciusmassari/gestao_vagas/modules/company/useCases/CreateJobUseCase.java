package br.com.viniciusmassari.gestao_vagas.modules.company.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.viniciusmassari.gestao_vagas.modules.company.dto.CreateJobResponseDTO;
import br.com.viniciusmassari.gestao_vagas.modules.company.entities.JobEntity;
import br.com.viniciusmassari.gestao_vagas.modules.company.repositories.CompanyRepository;
import br.com.viniciusmassari.gestao_vagas.modules.company.repositories.JobRepository;

@Service
public class CreateJobUseCase {
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    CompanyRepository companyRepository;

    public CreateJobResponseDTO execute(JobEntity jobEntity) {

        var company = companyRepository.findById(jobEntity.getCompanyId());
        company.ifPresent(entity -> {
            jobEntity.setCompanyEntity(entity);
        });

        var job = this.jobRepository.save(jobEntity);
        return CreateJobResponseDTO.builder().benefits(job.getBenefits()).description(job.getDescription())
                .id(job.getId()).level(job.getLevel()).title(job.getTitle()).build();
    }
}
