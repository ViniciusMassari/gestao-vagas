package br.com.viniciusmassari.gestao_vagas.utils;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class TokenUtilTest {

    @Test
    public void should_create_a_token() {
        TokenUtil tokenUtil = new TokenUtil();
        String secretKey = "mysecretkey";
        var roles = Arrays.asList("CANDIDATE");
        String token = tokenUtil.createToken(UUID.randomUUID().toString(), roles, secretKey);

        assertInstanceOf(String.class, token);
        assertNotNull(token);
    }
}
