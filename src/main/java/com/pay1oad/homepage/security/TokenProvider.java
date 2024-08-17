package com.pay1oad.homepage.security;

import com.pay1oad.homepage.model.login.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;


@Slf4j
@Service
public class TokenProvider {


    @Value("${JWT_SECRET}")
    private String secret_key;

    public String create(Member member){
        Date expireDate=Date.from(
                Instant.now()
                        .plus(1,ChronoUnit.DAYS)
        );

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, secret_key )
                .setSubject(String.valueOf(member.getUserid()))
                .setIssuer("Pay1oad Homepage")
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .compact();

    }

    public String validateAndGetUserId(String token){
        Claims claims=Jwts.parserBuilder()
                .setSigningKey(secret_key).build().parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }




}
