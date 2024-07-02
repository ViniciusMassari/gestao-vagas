package br.com.viniciusmassari.gestao_vagas.modules.candidate.useCases;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.viniciusmassari.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.viniciusmassari.gestao_vagas.modules.candidate.dto.CandidateProfileResponseDTO;

@Service
public class CandidateProfileUseCase {

    @Autowired
    private CandidateRepository candidateRepository;

    public CandidateProfileResponseDTO execute(UUID idCandidate) {
        var candidate = this.candidateRepository.findById(idCandidate).orElseThrow(() -> {
            throw new UsernameNotFoundException("User not found");
        });

        return CandidateProfileResponseDTO.builder()
                .id(candidate.getId())
                .name(candidate.getName())
                .description(candidate.getDescription())
                .username(candidate.getUsername())
                .email(candidate.getEmail()).build();
    }
}
