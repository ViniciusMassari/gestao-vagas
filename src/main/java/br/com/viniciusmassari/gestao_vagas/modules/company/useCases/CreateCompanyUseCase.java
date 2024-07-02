package br.com.viniciusmassari.gestao_vagas.modules.company.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.viniciusmassari.gestao_vagas.exceptions.UserFoundException;
import br.com.viniciusmassari.gestao_vagas.modules.company.dto.CreateCompanyResponseDTO;
import br.com.viniciusmassari.gestao_vagas.modules.company.entities.CompanyEntity;
import br.com.viniciusmassari.gestao_vagas.modules.company.repositories.CompanyRepository;

@Service
public class CreateCompanyUseCase {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CreateCompanyResponseDTO execute(CompanyEntity companyEntity) {
        this.companyRepository.findByUsernameOrEmail(companyEntity.getUsername(), companyEntity.getEmail())
                .ifPresent(user -> {
                    throw new UserFoundException();
                });
        ;

        var password = passwordEncoder.encode(companyEntity.getPassword());

        companyEntity.setPassword(password);
        var company = companyRepository.save(companyEntity);
        return CreateCompanyResponseDTO.createDto(company);
    }
}
