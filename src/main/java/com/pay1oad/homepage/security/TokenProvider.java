package com.pay1oad.homepage.security;

import com.pay1oad.homepage.dto.JwtToken;
import com.pay1oad.homepage.model.login.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;


@Slf4j
@Service
public class TokenProvider {


    @Value("${JWT_SECRET}")
    private String secret_key;

    public JwtToken create(Member member){
        Date acessExpireDate=Date.from(
                Instant.now()
                        .plus(60,ChronoUnit.MINUTES)
        );

        Date refreshExpireDate=Date.from(
                Instant.now()
                        .plus(3,ChronoUnit.DAYS)
        );

        String accessToken =  Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, secret_key )
                .setSubject(String.valueOf(member.getUserid()))
                .setIssuer("Pay1oad Homepage")
                .setIssuedAt(new Date())
                .setExpiration(acessExpireDate)
                .claim("auth", member.getMemberAuth())
                .compact();

        String refreshToken =  Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, secret_key )
                .setSubject(String.valueOf(member.getUserid()))
                .setIssuer("Pay1oad Homepage")
                .setIssuedAt(new Date())
                .setExpiration(acessExpireDate)
                .compact();

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }

    public String validateAndGetUserId(String token){
        Claims claims=Jwts.parserBuilder()
                .setSigningKey(secret_key).build().parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public Authentication getAuthentication(UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                null,
                userDetails.getAuthorities()
        );
    }

    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secret_key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Long getExpiration(String accessToken) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret_key)  // Make sure to use the same secret key used for signing
                .parseClaimsJws(accessToken)
                .getBody();

        Date expirationDate = claims.getExpiration();

        Date currentDate = new Date();

        long differenceInMilliseconds = expirationDate.getTime() - currentDate.getTime();

        return differenceInMilliseconds;
    }

}
