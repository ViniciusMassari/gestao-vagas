package br.com.viniciusmassari.gestao_vagas.modules.company.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.viniciusmassari.gestao_vagas.exceptions.CompanyNotFoundException;
import br.com.viniciusmassari.gestao_vagas.modules.company.dto.CreateJobResponseDTO;
import br.com.viniciusmassari.gestao_vagas.modules.company.entities.JobEntity;
import br.com.viniciusmassari.gestao_vagas.modules.company.repositories.CompanyRepository;
import br.com.viniciusmassari.gestao_vagas.modules.company.repositories.JobRepository;

@Service
public class CreateJobUseCase {
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyRepository companyRepository;

    public CreateJobResponseDTO execute(JobEntity jobEntity) {

        var company = this.companyRepository.findById(jobEntity.getCompanyId()).orElseThrow(() -> {
            throw new CompanyNotFoundException();
        });

        jobEntity.setCompanyEntity(company);

        var job = this.jobRepository.save(jobEntity);
        return CreateJobResponseDTO.createDTO(job);
    }
}
