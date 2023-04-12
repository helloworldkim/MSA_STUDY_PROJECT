package com.example.userservice;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@SpringBootTest
@Transactional
public class JwtTest {

    @Autowired
    Environment env;

    @Test
    void jwtTest() {
        String userId = UUID.randomUUID().toString();
        Long expiration_time = Long.valueOf(env.getProperty("token.expiration_time"));
        String secret = env.getProperty("token.secret");

        String token = Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + expiration_time))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        System.out.println("token = " + token);
    }
}
