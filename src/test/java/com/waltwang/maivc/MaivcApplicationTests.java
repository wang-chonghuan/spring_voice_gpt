package com.waltwang.maivc;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@SpringBootTest
class MaivcApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void jwtKeyGenerator() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        SecretKey secretKey = keyGenerator.generateKey();
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("Base64 Encoded Secret Key: " + encodedKey);
    }

    @Test
    public void apikeyToBase64() {
        String apiKey = "";
        String encodedApiKey = Base64.getEncoder().encodeToString(apiKey.getBytes(StandardCharsets.UTF_8));
        System.out.println("Encoded API Key: " + encodedApiKey);
    }

}
