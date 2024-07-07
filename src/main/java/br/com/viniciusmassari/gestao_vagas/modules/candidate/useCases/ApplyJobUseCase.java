package br.com.viniciusmassari.gestao_vagas.modules.candidate.useCases;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.viniciusmassari.gestao_vagas.exceptions.JobNotFoundException;
import br.com.viniciusmassari.gestao_vagas.exceptions.UserNotFoundException;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.entity.ApplyJobEntity;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.repository.ApplyJobRepository;
import br.com.viniciusmassari.gestao_vagas.modules.company.repositories.JobRepository;

@Service
public class ApplyJobUseCase {
    @Autowired
    CandidateRepository candidateRepository;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    private ApplyJobRepository applyJobRepository;

    public void execute(UUID candidateId, UUID jobId) {
        var candidate = candidateRepository.findById(candidateId).orElseThrow(() -> {
            throw new UserNotFoundException();
        });
        var job = jobRepository.findById(jobId).orElseThrow(() -> {
            throw new JobNotFoundException();
        });
        ApplyJobEntity applyJobEntity = ApplyJobEntity.builder().candidateId(candidate.getId()).jobId(job.getId())
                .build();
        applyJobRepository.save(applyJobEntity);
    }
}
