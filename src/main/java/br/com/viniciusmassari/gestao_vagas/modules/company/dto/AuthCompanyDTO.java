package br.com.viniciusmassari.gestao_vagas.modules.company.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthCompanyDTO {

    @NotEmpty(message = "Campo [password] não pode estar vazio")
    private String password;

    @NotEmpty(message = "Campo [username] não pode estar vazio")
    private String username;
}
