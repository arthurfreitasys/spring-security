package com.example.ssecurity.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/*
Esta classe é o filtro que intercepta cada requisição, lê o token JWT do cabeçalho e,
se for válido, avisa ao Spring Security quem é o usuário.
É a peça que faz a autenticação stateless funcionar
 */

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;
    // As duas dependências do filtro: uma valida o token, a outra carrega o usuário.

    public SecurityFilter(TokenService tokenService, UserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;

    }

    @Override
    // O método que roda a cada requisição.
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = recuperarToken(request);
        if (token != null) {
            var username = tokenService.validarToken(token);
            UserDetails usuario = userDetailsService.loadUserByUsername(username); //busca o usuário completo (com permissões) a partir do username.

            // Força a autenticação no contexto do Spring
            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities()); // Cria o objeto de autenticação;null=senha
            SecurityContextHolder.getContext().setAuthentication(authentication); // A partir daqui, enxergam o usuário como logado.
        }
        filterChain.doFilter(request, response); // Passa a requisição adiante pro filter
    }

    private String recuperarToken(HttpServletRequest request) { // Método privado que extrai o token JWT do cabeçalho "Authorization"
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}