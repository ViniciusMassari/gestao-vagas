package br.com.viniciusmassari.gestao_vagas.modules.company.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateJobDTO {

    @Schema(example = "Vaga para pessoa desenvolvedora júnior", requiredMode = RequiredMode.REQUIRED)
    @NotBlank(message = "O campo [title] é obrigatório")
    private String title;

    @Schema(example = "Vaga para pessoa desenvolvedora júnior", requiredMode = RequiredMode.REQUIRED)
    @NotBlank(message = "O campo [description] é obrigatório")
    private String description;

    @Schema(example = "Plano de saúde, plano odontológico, participação de lucros", requiredMode = RequiredMode.REQUIRED)
    @NotBlank(message = "O campo [benefits] é obrigatório")
    private String benefits;

    @Schema(example = "Júnior", requiredMode = RequiredMode.REQUIRED)
    @NotBlank(message = "O campo [level] é obrigatório")
    private String level;

}
