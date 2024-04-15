# Começando

Este projeto é um exemplo de uma aplicação Spring Boot 3 com Couchbase para cadastro de usuários. Questões referentes a autenticação e autorização não foram implementadas. A ideia é podermos explorar o uso do Couchbase como banco de dados NoSQL, uso do Docker e Docker Compose para facilitar a execução da aplicação e do banco de dados, e a implementação de uma API RESTful com Spring Boot e Java 22 em cima de uma arquitetura hexagonal.

## Introdução

Este documento serve como a principal fonte de informação para a configuração e práticas de desenvolvimento do nosso projeto Spring Boot 3. Ele visa fornecer aos desenvolvedores, novos e experientes, o conhecimento necessário para entender e contribuir efetivamente para um projeto RESTful baseado em Spring Boot 3 e Couchbase.

## Motivação

Além de podermos explorar aspectos técnicos do Couchbase, Docker e Docker Compose, a ideia é que possamos ter um projeto base para podermos explorar e discutir boas práticas de desenvolvimento de software, como:

- **Código Limpo**: Escrever código limpo e legível, seguindo boas práticas de programação.
- **DDD (Domain-Driven Design)**: Organizar o código em torno do domínio da aplicação que, apesar de ser simples, fornece meios de explorar conceitos como agregados, entidades, objetos de valor, serviços de domínio, repositórios, etc.
- **Testes Unitários**: Implementar testes unitários para garantir a qualidade do código e a integridade das funcionalidades. Aqui, podemos explorar o uso de JUnit, Mockito e também como criar testes de domínio utilizando stubs.
- **Arquitetura Hexagonal**: Organizar o código em torno de um núcleo de domínio, com portas de entrada e saída que permitem a comunicação com o mundo externo.

## Arquitetura

A arquitetura do projeto segue o padrão hexagonal, também conhecido como arquitetura de portas e adaptadores. A ideia aqui não é explorar a arquitetura by the book, mas sim ter um projeto base que nos permita explorar e discutir seus conceitos e também demonstrar que isolar totalmente o domínio da aplicação é algo que pode ser custoso e desnecessário em muitos casos.
Tendo em vista isso, ainda acoplamos o domínio ao Spring Data Couchbase, o que não é uma prática recomendada, mas que nos permite explorar o uso do Couchbase de forma mais simples e pragmática.

A arquitetura está abaixo:

```
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── com
│   │   │   │   ├── users
│   │   │   │   │   ├── adapters
│   │   │   │   │   │   ├── input
│   │   │   │   │   │   │   output
│   │   │   │   │   │   aplication
│   │   │   │   │   │   domain
│   │   │   │   │   │   ├── model
│   │   │   │   │   │   ├── repositories
│   │   │   │   │   │   ├── exceptions
│   │   │   │   │   │   infrastructure
```

### Adapter Layer
A camada de adaptadores é responsável por adaptar as entradas e saídas da aplicação. Aqui só temos um adapter de entrada, que é o `UsersController`, que é responsável por receber as requisições HTTP e delegar para a camada de aplicação. A camada de saída é representada pelo `UsersRepositoryAdapter`, que é um adapter para o repositório do Spring Data Couchbase.

### Application Layer
A camada de aplicação é responsável por orquestrar a execução das regras de negócio da aplicação. Aqui, temos o `UsersService`, que é responsável por orquestrar a execução das regras de negócio da aplicação. O `UsersService` delega a execução das regras de negócio para a camada de domínio.

### Domain Layer
A camada de domínio contém as regras de negócio da aplicação. Aqui, definimos os objetos de valor, entidades, agregados, serviços de domínio e exceções de domínio. Os repositórios também são definidos aqui, mas são implementados na camada de output. A ideia por trás disso é que a lógica de acesso de e persistência de dados faz parte do domínio, mas a implementação, por questões técnicas, não faz. Sendo assim, para fins de exemplificação, a implementação aqui fica na classe `UsersRepositoryAdapter`, sendo ela também um adapter para a classe `CouchbaseUsersRepository`. Isso não é normalemtne feito, mas serve para exemplificar como podemos fazer para separar o contrato da implementação concrete e, como os repositórios do Spring Data Couchbase são interfaces, podemos seguir o padrão Adapter para fazer isso.

### Infrastructure Layer
A camada de infraestrutura é responsável por implementar os detalhes técnicos da aplicação. Aqui, podemos ter configurações de integrações, frameworks, bibliotecas, e também a implementação de alguams questões de caráter técnico e que não tem relação direta com o domínio da aplicação.

### Modelo de domínio

Abaixo o modelo de domínio da aplicação:

![Domain Model](https://github.com/PedroSebastian/meli-users/blob/main/engineering/domain_model.png)

#### Considerações sobre o modelo de domínio
A classe `User` é a entidade principal, e único agregado da aplicação. Temos também algumas classes de serviço, que são basicamente classes que implementam alguma coisa no contexto do domínio da aplicação. Algumas pessoas tendem a implementar como utils ou helpers, o que não é uma boa prática e ainda pode ser considerado como anti padrões. A classe de serviço `UserRegistrationService` implementa as regras de cadastro, pois elas são importântes para o contexto da aplicação. Ela recebe o repositório via injeção por construtor e utiliza ele para verificar se email ou CPF já estão em uso. O restante das classes são basicamente implementações simples de objetos de valor, que são classes que representam valores que não são entidades, mas que são importantes para o domínio.

## Executando a Aplicação com Docker Compose

Executar esta aplicação Spring Boot com Docker Compose simplifica o processo de configuração da aplicação e dos serviços necessários, como o Couchbase. As etapas a seguir descrevem como construir e executar a aplicação usando Docker e Docker Compose.

### Pré-requisitos

- Docker instalado em sua máquina.
- Docker Compose instalado.
- Acesso ao terminal ou linha de comando.

### Visão Geral da Configuração do Docker

- **Container da Aplicação (`users`)**: Constrói e executa a aplicação Spring Boot, utilizando o `Dockerfile` de múltiplos estágios para minimizar o tamanho da imagem enquanto garante que todas as dependências estejam corretamente empacotadas.
- **Container do Couchbase (`couchbase`)**: Configura um servidor Couchbase do qual a aplicação depende. Configura-o usando variáveis de ambiente e um script de inicialização.

### Arquivos e Diretórios

Certifique-se de que os seguintes arquivos e diretórios estejam presentes:
- `compose.yaml`: Contém a configuração tanto para a aplicação quanto para o serviço Couchbase.
- `Dockerfile`: Define o processo de construção de múltiplos estágios para a aplicação.
- `init-couchbase.sh`: Um script para configurar o serviço Couchbase na inicialização (certifique-se de que seja executável).
- `src/`: Código-fonte da aplicação.
- `pom.xml`: Arquivo de configuração de build do Maven.

### Etapas para Executar

1. **Construir e Executar com Docker Compose:**

   Navegue até o diretório raiz do projeto onde o `compose.yaml` e o `Dockerfile` estão localizados. Execute o seguinte comando para construir a imagem da aplicação e iniciar todos os serviços definidos no arquivo Docker Compose:

   ```sh
   docker-compose up --build
   ```

## Documentação da API com SpringDoc

Este projeto utiliza o SpringDoc para gerar e servir automaticamente a documentação OpenAPI para sua API RESTful. Você pode acessar a documentação nestes endpoints:

- **Documentos da API (Formato JSON):** `/openapi/v3/api-docs`
- **Swagger UI (Exploração Interativa da API):** `/swagger-ui/index.html`

## API RESTful com HATEOAS e Método PATCH

### HATEOAS

Esta aplicação emprega os princípios de HATEOAS (Hypermedia as the Engine of Application State), garantindo que as respostas da nossa API REST contenham hiperlinks para outros recursos da API. Essa abordagem permite que os clientes naveguem nossos serviços dinamicamente, seguindo os links fornecidos nas respostas, promovendo a descobrimento e facilidade de integração.

### Usando PATCH para Atualizações Parciais

O método PATCH é usado para realizar atualizações parciais em recursos, especificamente para a entidade `User`. Ele permite que os clientes enviem um documento JSON Patch (`application/json-patch+json`) descrevendo mudanças no recurso. Este método é particularmente útil para atualizar atributos específicos de um recurso sem a necessidade de enviar o recurso inteiro.

**Exemplo de Requisição JSON Patch:**
```json
[
  { "op": "replace", "path": "/name", "value": "Novo Nome" },
  { "op": "replace", "path": "/password", "value": "novasenha123" }
]
```

**Endpoint:**
```
PATCH /api/v1/users/{id}
```
Este endpoint consome `application/json-patch+json`, que não é tão comumente usado quanto outros tipos de mídia, mas é altamente eficaz para atualizações parciais.

**Restrições:**
Certos campos, como `id`, `cpf`, `createdAt`, `editedAt` e `email`, são imutáveis. Tentativas de modificar esses campos resultarão em erro.

### Motivação e Considerações

O uso do PATCH proporciona flexibilidade na atualização de recursos e reduz o uso de banda larga, pois apenas os dados alterados precisam ser enviados. No entanto, requer que os clientes lidem com o formato JSON Patch e estejam cientes dos campos que são imutáveis. Apesar de ser menos comum, `application/json-patch+json` permite atualizações de dados mais eficientes, tornando-o ideal para aplicações móveis e cenários de baixa largura de banda.

### Documentação de Referência
Para referências adicionais, considere as seguintes seções:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.4/maven-plugin/reference/html/)
* [Spring Boot Testcontainers support](https://docs.spring.io/spring-boot/docs/3.2.4/reference/html/features.html#features.testing.testcontainers)
* [Testcontainers Couchbase Module Reference Guide](https://java.testcontainers.org/modules/databases/couchbase/)
* [Docker Compose Support](https://docs.spring.io/spring-boot/docs/3.2.4/reference/htmlsingle/index.html#features.docker-compose)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.2.4/reference/htmlsingle/index.html#web)
* [Spring HATEOAS](https://docs.spring.io/spring-boot/docs/3.2.4/reference/htmlsingle/index.html#web.spring-hateoas)
* [Spring Data Couchbase](https://docs.spring.io/spring-boot/docs/3.2.4/reference/htmlsingle/index.html#data.nosql.couchbase)
* [Validation](https://docs.spring.io/spring-boot/docs/3.2.4/reference/htmlsingle/index.html#io.validation)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/3.2.4/reference/htmlsingle/index.html#actuator)

### Guides
Os guias a seguir ilustram como usar alguns recursos concretamente:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Building a Hypermedia-Driven RESTful Web Service](https://spring.io/guides/gs/rest-hateoas/)
* [Validation](https://spring.io/guides/gs/validating-form-input/)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
