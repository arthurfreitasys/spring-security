package com.example.ssecurity.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Configuração da autorização das rotas
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").permitAll() // Rota raiz é publica
                        .requestMatchers("/horarios").hasAnyRole("USER", "ADMIN")// Acesso de alunos e professores
                        .requestMatchers("/gerenciar").hasAnyRole("ADMIN") // Apenas professores
                        .requestMatchers("/limpeza").hasAnyRole("LIMPEZA")
                        .anyRequest().authenticated() //Qualquer outra rota exige login
                )
                .formLogin(Customizer.withDefaults()) //Habilita o formulario de login padrão do Spring
                .httpBasic(Customizer.withDefaults()); //Permite testes via Postman/Insomnia
        return http.build();
    }

// Criação dos Usuários em Memória
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
// Criando um perfil de Aluno
        UserDetails aluno = User.builder()
                .username("Arthur")
                .password(encoder.encode("senha123"))
                .roles("USER")
                .build();
// Criando um perfil de Professor (Admin)
        UserDetails professor = User.builder()
                .username("Maromo")
                .password(encoder.encode("admin123"))
                .roles("ADMIN")
                .build();
//Criando perfil de de funcionarios da limpeza
        UserDetails funcLimpeza = User.builder()
                .username("Francisco")
                .password(encoder.encode("limpeza123"))
                .roles("LIMPEZA")
                .build();
        return new InMemoryUserDetailsManager(aluno, professor, funcLimpeza);
    }
    // Definindo o codificador de senhas (Obrigatório no Spring Security)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

