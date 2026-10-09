package com.example.ssecurity.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/*
TokenService sabe criar e validar tokens,
e o SecurityFilter é quem usa essa validação a cada chamada.
 */

/*
Um JWT tem três partes separadas por ponto (.):
xxxxx.yyyyy.zzzzz
Header: o algoritmo usado e o tipo (JWT).
Payload: os dados do TokenService: emissor (iss), usuário (sub) e expiração (exp).
Assinatura: o cálculo com a sua chave secreta, que impede alteração.
 */

@Service
public class TokenService { // Classe responsável por gerar e validar tokens.

    //(application.properties)
    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(UserDetails usuario) {
        Algorithm algoritmo = Algorithm.HMAC256(secret);
        return JWT.create() //Inicia a construção de um novo token
                .withIssuer("SistemaLaboratorios") // indica quem emitiu o token
                .withSubject(usuario.getUsername()) // indica a quem o token pertence
                .withExpiresAt(Instant.now().plus(2, ChronoUnit.HOURS)) // Expira em 2 horas
                .sign(algoritmo); // gera o header e o payload, calcula a assinatura com a chave e devolve a string
    }

    public String validarToken(String token) { // Recebe o token enviado pelo cliente e devolve o username dono dele.
        Algorithm algoritmo = Algorithm.HMAC256(secret);
        return JWT.require(algoritmo) // Inicia a construção de um verificador
                .withIssuer("SistemaLaboratorios") //Exige que o emissor seja exatamente esse
                .build()
                .verify(token)// Se algo estiver errado, lança erro. Se estiver tudo certo, devolve o token decodificado.
                .getSubject(); // Extrai o campo sub do token já verificado, que é o username.
    }
}
