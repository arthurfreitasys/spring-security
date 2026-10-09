# Spring Security

Repositório criado para estudar e entender como funciona a segurança de uma API Java usando Spring Security e JWT.

O código foi disponibilizado pelo professor para acompanhar as aulas e testar requisições no Postman. Durante os estudos, adicionei comentários ao código e utilizei ferramentas de IA para ajudar a entender como cada parte funciona.

## Tecnologias utilizadas

- Java
- Spring Boot
- Spring Security
- JWT
- Postman
- Maven

## 1. O que é Spring Security?

O Spring Security é uma ferramenta que ajuda a proteger uma aplicação. Com ele, podemos definir quem pode acessar a API e quais recursos cada usuário pode utilizar.

Existem dois conceitos importantes:

- **Autenticação:** verificar quem é o usuário. Por exemplo, conferir se o login e a senha estão corretos.
- **Autorização:** verificar o que esse usuário pode acessar. Por exemplo, permitir que apenas administradores excluam usuários.

### Como funciona?

Quando uma requisição chega à aplicação, o Spring Security verifica se ela atende às regras de segurança configuradas antes de liberar o acesso ao recurso solicitado.

```text
Requisição
    |
    v
Verificação de segurança
    |
    v
Usuário pode acessar?
    |
    +---- Sim ----> Acesso liberado
    |
    +---- Não ----> Acesso bloqueado
```

## 2. O que é JWT?

JWT significa *JSON Web Token*. É um token que pode ser utilizado para identificar um usuário depois que ele faz login.

Pense nele como uma espécie de credencial digital. Depois de fazer login, o usuário recebe um token e o envia nas próximas requisições que precisam de autenticação.

### Como funciona na prática?

1. O usuário informa seu login e sua senha.
2. A aplicação verifica se os dados estão corretos.
3. Se estiver tudo certo, a aplicação gera um token JWT.
4. O usuário recebe esse token.
5. Nas próximas requisições protegidas, o token é enviado para que a aplicação verifique a autenticação.

### Como é formado um JWT?

Um JWT possui três partes:

```text
HEADER.PAYLOAD.SIGNATURE
```

- **Header:** informa detalhes sobre o token, como o algoritmo de assinatura utilizado.
- **Payload:** contém informações sobre o usuário ou sobre o próprio token, como a data de expiração.
- **Signature:** permite verificar se o token foi alterado e se sua assinatura é válida.

Exemplo de informações que podem estar no payload:

```json
{
  "sub": "arthur",
  "exp": 1791496800
}
```

Nesse exemplo:

- `sub`: identifica o usuário associado ao token.
- `exp`: informa quando o token expira.

**Importante:** o conteúdo de um JWT comum pode ser lido por quem tiver acesso ao token. Por isso, nunca devemos colocar senhas ou informações secretas no payload.

## 3. O que significa Stateless?

Stateless significa, de forma simples, que o servidor não precisa manter uma sessão de login para cada usuário entre as requisições.

Em uma aplicação que utiliza JWT dessa maneira, o cliente envia seu token sempre que precisa acessar um recurso protegido.

### Comparando os dois modelos

**Com sessão:**

```text
Usuário faz login
       |
       v
Servidor cria uma sessão
       |
       v
Servidor mantém os dados
da sessão
       |
       v
Usuário continua acessando
a aplicação pela sessão
```

**Com JWT e stateless:**

```text
Usuário faz login
       |
       v
Recebe um token JWT
       |
       v
Envia o token nas requisições
       |
       v
Servidor verifica o token
       |
       v
Acesso liberado ou bloqueado
```

### Por que utilizar stateless?

- O servidor não precisa manter uma sessão de autenticação para cada usuário.
- Pode facilitar a execução da aplicação em vários servidores.
- O token acompanha as requisições que precisam de autenticação.

Isso não significa que a aplicação não armazena nenhum dado. Ela ainda pode utilizar bancos de dados e outros recursos. A ideia é não depender de uma sessão de login mantida no servidor para reconhecer o usuário.

## 4. Como o Spring Security e o JWT trabalham juntos?

O JWT é o token utilizado para identificar o usuário, enquanto o Spring Security é responsável por aplicar as regras de segurança da aplicação.

De forma simplificada, o processo funciona assim:

```text
1. Usuário faz login
         |
         v
2. Aplicação valida os dados
         |
         v
3. JWT é gerado
         |
         v
4. Cliente envia o token
         |
         v
5. Spring Security verifica
   a autenticação
         |
         v
6. Aplicação verifica
   se o acesso é permitido
```

O token não libera automaticamente qualquer recurso. A aplicação também precisa verificar as permissões exigidas pelo endpoint.

## 5. O que são os filtros de segurança?

O Spring Security utiliza filtros para analisar as requisições antes que elas cheguem ao código responsável pelo recurso solicitado.

Alguns filtros podem verificar credenciais, processar o JWT ou ajudar a identificar o usuário autenticado.

Pense nos filtros como etapas de verificação pelas quais uma requisição precisa passar antes de chegar ao seu destino.

```text
Requisição HTTP
       |
       v
Filtros do Spring Security
       |
       v
Verificação do token
       |
       v
Regras de acesso
       |
       v
Endpoint da API
```

A sequência exata depende de como a segurança foi configurada no projeto.

## 6. Testando com Postman

O Postman permite enviar requisições para a API e verificar como ela responde em diferentes situações.

### Testando a autenticação

1. Inicie a aplicação Java.
2. Envie uma requisição para o endpoint de login configurado no projeto.
3. Confira se as credenciais foram aceitas e se a resposta contém um token.
4. Copie o token recebido.
5. Envie uma requisição para um endpoint protegido.

### Como enviar o JWT?

O token geralmente é enviado no cabeçalho `Authorization`:

```http
Authorization: Bearer SEU_TOKEN
```

No Postman, você também pode acessar a aba **Authorization**, selecionar o tipo **Bearer Token** e colar o token no campo correspondente.

### O que testar?

| Teste | O que você aprende |
|---|---|
| Fazer login com dados corretos | Como funciona a autenticação |
| Fazer login com dados incorretos | Como a aplicação rejeita credenciais |
| Acessar uma rota sem token | Como funciona a proteção de endpoints |
| Acessar uma rota com token válido | Como o token permite identificar o usuário |
| Enviar um token inválido | Como a aplicação trata tokens incorretos |
| Enviar um token expirado | Como funciona a expiração do JWT |

As respostas dependem das configurações do projeto. Por exemplo, uma rota pode ser pública e não exigir token.

## 7. Resumo para consultar depois

| Conceito | Lembre-se de que... |
|---|---|
| Spring Security | Controla a segurança e o acesso à aplicação. |
| Autenticação | Verifica quem é o usuário. |
| Autorização | Verifica o que o usuário pode acessar. |
| JWT | É um token que pode identificar o usuário nas requisições. |
| Header | Guarda informações sobre o token. |
| Payload | Guarda informações chamadas claims. |
| Signature | Permite verificar a assinatura e detectar alterações no token. |
| Stateless | A autenticação não depende de uma sessão mantida entre as requisições. |
| Filtros | Analisam as requisições antes de chegarem aos recursos da aplicação. |
| Bearer Token | É a forma de enviar o token no cabeçalho da requisição. |
| Postman | Permite testar a API e verificar suas respostas. |

## Sobre o projeto

Este repositório é voltado para estudo e aprendizado. O código-base foi fornecido pelo professor, e meu trabalho foi adicionar comentários, analisar a implementação e estudar seu funcionamento com apoio de ferramentas de IA.

A ideia é utilizar o projeto como material de consulta para revisar os conceitos de Spring Security, JWT e autenticação stateless e, futuramente, aplicar esse conhecimento em projetos próprios.
