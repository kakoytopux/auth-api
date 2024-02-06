package com.example.authapi.security;

import com.example.authapi.middlewares.ValidateToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfFilter;

/*
 * Вера в себя – это ключ к преодолению любых преград.
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http

            .addFilterAt(new ValidateToken(), CsrfFilter.class)
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(requests -> requests
                    .requestMatchers("/api/signin", "/api/users/signup")
                    .permitAll()
                    .anyRequest().authenticated()
            );

    return http.build();
  }
}
