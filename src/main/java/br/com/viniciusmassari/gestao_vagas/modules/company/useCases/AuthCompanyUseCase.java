package br.com.viniciusmassari.gestao_vagas.modules.company.useCases;

import java.time.Instant;
import java.util.Arrays;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.viniciusmassari.gestao_vagas.modules.company.dto.AuthCompanyDTO;
import br.com.viniciusmassari.gestao_vagas.modules.company.dto.AuthCompanyResponseDTO;
import br.com.viniciusmassari.gestao_vagas.modules.company.repositories.CompanyRepository;
import java.time.Duration;

@Service
public class AuthCompanyUseCase {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${security.token.secret}")
    private String secretKey;

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

        Algorithm algorithm = Algorithm.HMAC256(this.secretKey);
        Instant expiresIn = Instant.now().plus(Duration.ofHours(2));
        String token = JWT.create().withIssuer("javagas").withSubject(company.getId().toString())
                .withClaim("roles", Arrays.asList("COMPANY"))
                .withExpiresAt(expiresIn).sign(algorithm);

        return AuthCompanyResponseDTO.builder().access_token(token).expires_in(expiresIn.toEpochMilli()).build();
    }

}
