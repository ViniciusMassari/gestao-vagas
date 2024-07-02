package br.com.viniciusmassari.gestao_vagas.modules.candidate.dto;

import java.util.UUID;

import br.com.viniciusmassari.gestao_vagas.modules.candidate.CandidateEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCandidateResponseDTO {
    @Schema(example = "johnDoe")
    private String username;

    private UUID id;
    @Schema(example = "John Doe")
    private String name;

    static public CreateCandidateResponseDTO createDTO(CandidateEntity candidate) {
        return CreateCandidateResponseDTO.builder().username(candidate.getUsername()).name(candidate.getName())
                .id(candidate.getId()).build();
    }
}
