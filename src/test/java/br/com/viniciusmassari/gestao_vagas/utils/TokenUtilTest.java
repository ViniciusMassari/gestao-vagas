package br.com.viniciusmassari.gestao_vagas.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.UUID;

import org.junit.Test;

public class TokenUtilTest {

    @Test
    public void should_create_a_token() {
        TokenUtil tokenUtil = new TokenUtil();
        String secretKey = "mysecretkey";
        String token = tokenUtil.createToken(UUID.randomUUID().toString(), UserType.CANDIDATE, secretKey);

        assertInstanceOf(String.class, token);
        assertNotNull(token);
    }
}
