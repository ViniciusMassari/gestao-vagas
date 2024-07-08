package br.com.viniciusmassari.gestao_vagas.modules.company.dto;

import java.util.UUID;

import br.com.viniciusmassari.gestao_vagas.modules.company.entities.JobEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateJobResponseDTO {
    private UUID id;
    private String description;
    private String benefits;
    private String level;
    private String title;

    public static CreateJobResponseDTO createDTO(JobEntity job) {
        return CreateJobResponseDTO.builder().benefits(job.getBenefits()).description(job.getDescription())
                .id(job.getId()).level(job.getLevel()).title(job.getTitle()).build();
    }
}
