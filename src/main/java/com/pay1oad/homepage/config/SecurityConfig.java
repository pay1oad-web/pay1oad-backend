package com.pay1oad.homepage.config;

import com.pay1oad.homepage.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers("/", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()

                        .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/auth/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/verify/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/admin/**")).permitAll()

                        .anyRequest().authenticated()
                )

                .csrf((csrf) -> csrf
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/auth/**"))
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/verify/**"))
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/board/**"))
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/admin/**"))
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/email")))

                .headers((headers) -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))

                .addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        ;
        return http.build();
    }
}
