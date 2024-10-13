package br.com.viniciusmassari.gestao_vagas.modules.company.useCases;

import java.util.Arrays;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.viniciusmassari.gestao_vagas.modules.company.dto.AuthCompanyDTO;
import br.com.viniciusmassari.gestao_vagas.modules.company.dto.AuthCompanyResponseDTO;
import br.com.viniciusmassari.gestao_vagas.modules.company.repositories.CompanyRepository;
import br.com.viniciusmassari.gestao_vagas.utils.TokenUtil;
import br.com.viniciusmassari.gestao_vagas.utils.UserType;

@Service
public class AuthCompanyUseCase {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${security.token.secret}")
    private String secretKey;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private CompanyRepository companyRepository;

    public AuthCompanyResponseDTO execute(AuthCompanyDTO authCompanyDTO) throws AuthenticationException {
        var company = this.companyRepository.findByUsername(authCompanyDTO.getUsername())
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("Empresa não encontrada, dados incorretos");
                });

        boolean isSamePassword = this.passwordEncoder.matches(authCompanyDTO.getPassword(), company.getPassword());

        if (!isSamePassword)
            throw new AuthenticationException();

        var roles = Arrays.asList(UserType.COMPANY.toString());

        String token = tokenUtil.createToken(company.getId().toString(), roles, secretKey);

        return AuthCompanyResponseDTO.builder().access_token(token).roles(roles).build();
    }

}
