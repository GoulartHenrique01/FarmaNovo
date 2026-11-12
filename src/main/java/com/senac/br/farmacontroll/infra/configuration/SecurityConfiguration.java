package com.senac.br.farmacontroll.infra.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        return http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/swagger-resources/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuarios/admin").permitAll()
                        .requestMatchers(HttpMethod.POST, "/empresas").permitAll()
                        .requestMatchers("/usuarios/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/empresas/**").hasRole("ADMIN")
                        .requestMatchers("/receitas/**").hasAnyRole("ADMIN", "FARMACEUTICO", "USER")
                        .requestMatchers("/medicamentos/**")
                        .hasAnyRole(
                                "ADMIN",
                                "USER",
                                "ATENDENTE"
                        )

                        .requestMatchers("/pacientes/**")
                        .hasAnyRole(
                                "ADMIN",
                                "USER",
                                "ATENDENTE"
                        )
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
