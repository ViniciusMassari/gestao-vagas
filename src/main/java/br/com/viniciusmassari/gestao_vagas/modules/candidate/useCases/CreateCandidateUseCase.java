package br.com.viniciusmassari.gestao_vagas.modules.candidate.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.viniciusmassari.gestao_vagas.exceptions.UserFoundException;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.dto.CreateCandidateResponseDTO;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.entity.CandidateEntity;

@Service
public class CreateCandidateUseCase {
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CreateCandidateResponseDTO execute(CandidateEntity candidateEntity) {
        this.candidateRepository.findByUsernameOrEmail(candidateEntity.getUsername(),
                candidateEntity.getEmail())
                .ifPresent(user -> {
                    throw new UserFoundException();
                });

        var passwordHash = this.passwordEncoder.encode(candidateEntity.getPassword());
        candidateEntity.setPassword(passwordHash);
        var candidate = this.candidateRepository.save(candidateEntity);
        return CreateCandidateResponseDTO.createDTO(candidate);

    }
}
