package br.com.viniciusmassari.gestao_vagas.modules.company.useCases;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.annotation.Description;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.viniciusmassari.gestao_vagas.modules.company.dto.AuthCompanyDTO;
import br.com.viniciusmassari.gestao_vagas.modules.company.dto.AuthCompanyResponseDTO;
import br.com.viniciusmassari.gestao_vagas.modules.company.entities.CompanyEntity;
import br.com.viniciusmassari.gestao_vagas.modules.company.repositories.CompanyRepository;
import br.com.viniciusmassari.gestao_vagas.utils.TokenUtil;
import br.com.viniciusmassari.gestao_vagas.utils.UserType;

@RunWith(MockitoJUnitRunner.class)
public class AuthCompanyTest {

    @InjectMocks
    AuthCompanyUseCase authCompanyUseCase;

    @Mock
    CompanyRepository companyRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    TokenUtil tokenUtil;

    @Description("Should not authenticate company if it does not exist")
    @Test
    public void should_not_authenticate_user_if_user_does_not_exist() {
        AuthCompanyDTO authCompanyDTO = new AuthCompanyDTO("username", "123456");
        when(companyRepository.findByUsername(authCompanyDTO.getUsername())).thenReturn(Optional.empty());
        try {
            authCompanyUseCase.execute(authCompanyDTO);
        } catch (Exception e) {
            assertInstanceOf(UsernameNotFoundException.class, e);
        }
    }

    @Description("Should authenticate company")
    @Test
    public void should_authenticate_company() {
        String rawPassword = "123456";
        CompanyEntity companyEntity = CompanyEntity.builder().username("username")
                .password(UUID.randomUUID().toString()).id(UUID.randomUUID()).build();
        AuthCompanyDTO authCompanyDTO = new AuthCompanyDTO(rawPassword, companyEntity.getUsername());

        when(companyRepository.findByUsername(authCompanyDTO.getUsername())).thenReturn(Optional.of(companyEntity));
        when(passwordEncoder.matches(rawPassword, companyEntity.getPassword())).thenReturn(true);
        when(tokenUtil.createToken(companyEntity.getId().toString(), UserType.COMPANY, "mysecret")).thenReturn("token");

        assertDoesNotThrow(() -> {
            var response = authCompanyUseCase.execute(authCompanyDTO);
            assertInstanceOf(AuthCompanyResponseDTO.class, response);

        });

    }
}
