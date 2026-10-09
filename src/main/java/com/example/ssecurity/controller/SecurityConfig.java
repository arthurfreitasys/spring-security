package com.example.ssecurity.controller;

import com.example.ssecurity.security.SecurityFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
   /*
   define as regras de acesso, deixa a API stateless,
   registra o SecurityFilter, expõe o AuthenticationManager
   e cria os usuários
    */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, SecurityFilter securityFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desabilitamos CSRF porque tokens já protegem contra isso
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/login").permitAll() // Rota de login é pública
                        .requestMatchers("/horarios").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/gerenciar").hasRole("ADMIN")
                        .anyRequest().authenticated() // qualquer rota não listada exige login
                )
                // Coloca o nosso filtro ANTES do filtro padrão do Spring
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Expõe o gerenciador de autenticação para usarmos no Controller
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // 2. Criação dos Usuários em Memória
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        // Criando um perfil de Aluno
        UserDetails aluno = User.builder()
                .username("abner")
                .password(encoder.encode("senha123"))
                .roles("USER")
                .build();

        // Criando um perfil de Professor (Admin)
        UserDetails professor = User.builder()
                .username("maromo")
                .password(encoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(aluno, professor);
    }

    // 3. Definindo o codificador de senhas (Obrigatório no Spring Security)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}