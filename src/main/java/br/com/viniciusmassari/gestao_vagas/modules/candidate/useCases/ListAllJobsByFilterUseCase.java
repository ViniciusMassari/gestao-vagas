package br.com.viniciusmassari.gestao_vagas.modules.candidate.useCases;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.viniciusmassari.gestao_vagas.modules.candidate.dto.ListAllJobsByFilterResponseDTO;
import br.com.viniciusmassari.gestao_vagas.modules.company.repositories.JobRepository;

@Service
public class ListAllJobsByFilterUseCase {
    @Autowired
    private JobRepository jobRepository;

    public List<ListAllJobsByFilterResponseDTO> execute(String filter) {
        var jobs = this.jobRepository.findByDescriptionContainingIgnoreCase(filter);
        var listOfJobs = jobs.stream().map(job -> {
            return ListAllJobsByFilterResponseDTO.builder().benefits(job.getBenefits())
                    .description(job.getDescription()).id(job.getId()).level(job.getLevel()).title(job.getTitle())
                    .build();
        }).collect(Collectors.toList());

        return listOfJobs;
    }
}
