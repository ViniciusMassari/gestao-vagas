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
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.viniciusmassari.gestao_vagas.exceptions.UserFoundException;
import br.com.viniciusmassari.gestao_vagas.modules.company.entities.CompanyEntity;
import br.com.viniciusmassari.gestao_vagas.modules.company.repositories.CompanyRepository;

@RunWith(MockitoJUnitRunner.class)
public class CreateCompanyUseCaseTest {

    @InjectMocks
    private CreateCompanyUseCase createCompanyUseCase;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Description("Should not create a new company if its email already exists")
    @Test
    public void should_not_create_a_new_company() {
        CompanyEntity company = CompanyEntity.builder().email("companyEmail@example.com").build();
        when(this.companyRepository.findByUsernameOrEmail(null, company.getEmail())).thenReturn(Optional.of(company));

        try {
            createCompanyUseCase.execute(company);
        } catch (Exception e) {
            assertInstanceOf(UserFoundException.class, e);
        }
    }

    @Description("Should not create a new company if its username already exists")
    @Test
    public void should_not_create_a_new_company_by_username() {
        CompanyEntity company = CompanyEntity.builder().username("companyusername").build();
        when(this.companyRepository.findByUsernameOrEmail(company.getUsername(), null))
                .thenReturn(Optional.of(company));

        try {
            createCompanyUseCase.execute(company);
        } catch (Exception e) {
            assertInstanceOf(UserFoundException.class, e);
        }
    }

    @Description("Should create a new company")
    @Test
    public void should_create_a_new_company() {
        CompanyEntity company = CompanyEntity.builder().id(UUID.randomUUID()).name("company").email("company@email.com")
                .website("http://company.com")
                .username("companyusername").password("123456").description("company description").build();
        when(this.companyRepository.findByUsernameOrEmail(company.getUsername(), company.getEmail()))
                .thenReturn(Optional.empty());

        when(this.companyRepository.save(company)).thenReturn(company);

        assertDoesNotThrow(() -> {
            createCompanyUseCase.execute(company);
        });
    }
}