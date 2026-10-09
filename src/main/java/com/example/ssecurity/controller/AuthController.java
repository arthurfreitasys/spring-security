package com.example.ssecurity.controller;

import com.example.ssecurity.security.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// DTO para receber os dados limpos
record DadosLogin(String username, String password) {} // O Spring converte o JSON {"username": "...", "password": "..."} nesse objeto automaticamente

@RestController
public class AuthController { // Controller dedicado à autenticação
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        // verifica as credenciais, consulta o UserDetailsService,
        // compara a senha informada com o hash salvo (usando o PasswordEncoder) e diz se está correto.
    }

    @PostMapping("/login") // Mapeia requisições POST /login
    public String login(@RequestBody DadosLogin dados) { // diz ao Spring para ler o corpo da requisição (JSON)
                                                         // e transformar em um DadosLogin. O retorno é o token.
        var usernamePassword = new UsernamePasswordAuthenticationToken(dados.username(), dados.password()); // Cria o objeto de autenticação
        var auth = this.authenticationManager.authenticate(usernamePassword);
        /*
        busca o usuário, compara a senha e:
        se estiver correto, devolve um Authentication já autenticado
        se estiver errado, lança erro.
         */


        // Se a senha estiver correta, gera o token
        return tokenService.gerarToken((UserDetails) auth.getPrincipal()); // retorna o usuário autenticado
    }
}