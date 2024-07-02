package br.com.viniciusmassari.gestao_vagas.modules.candidate.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfileResponseDTO {
    @Schema(example = "johnDoe")
    private String username;
    @Schema(example = "Desenvolvedor java")
    private String description;
    @Schema(example = "johndoe@email.com")
    private String email;
    private UUID id;
    @Schema(example = "John Doe")
    private String name;

}
