package br.com.viniciusmassari.gestao_vagas.modules.company.dto;

import java.util.UUID;

import br.com.viniciusmassari.gestao_vagas.modules.company.entities.CompanyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCompanyResponseDTO {
    private UUID id;
    private String email;
    private String username;
    private String website;
    private String name;
    private String description;

    public static CreateCompanyResponseDTO createDto(CompanyEntity company) {
        return CreateCompanyResponseDTO.builder().description(company.getDescription()).email(company.getEmail())
                .id(company.getId()).name(company.getName()).username(company.getUsername())
                .website(company.getWebsite()).build();

    }
}
