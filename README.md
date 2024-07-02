# Mobiauto

Mobiauto é uma API para a venda de veículos através de oportunidades, oferecida por revendas que possuem usuários que fazem as vendas para os clientes.

## Arquitetura da Aplicação

A aplicação segue a [Arquitetura Cebola (Onion Architecture)](https://www.macoratti.net/21/05/net_onion1.htm), um padrão que promove a separação de responsabilidades e a inversão de dependências, conforme os princípios do Domain-Driven Design (DDD). A aplicação está organizada em camadas distintas, cada uma com suas responsabilidades bem definidas.

## Camadas da Arquitetura

### Application
Orquestra chamadas entre as camadas de domínio e infraestrutura. Contém a lógica de negócios, serviços e mapeadores.

### Domain
Define entidades, agregados, serviços de domínio e contratos. Representa o modelo de negócios central e é independente de tecnologia específica.

### Infrastructure
Implementa os contratos definidos no domínio. Inclui repositórios, integrações externas, configuração de banco de dados e serviços de suporte como mensageria e segurança.

### Presentation
Recebe requisições HTTP e envia respostas. Contém os controladores que expõem a API da aplicação e gerenciam a autenticação e autorização.

## Dependências da Aplicação

A aplicação utiliza as seguintes dependências conforme especificado no arquivo `pom.xml`:

- **Spring Boot Starter Data JPA**
  - `org.springframework.boot:spring-boot-starter-data-jpa`

- **Spring Boot Starter Security**
  - `org.springframework.boot:spring-boot-starter-security`

- **Spring Boot Starter Web**
  - `org.springframework.boot:spring-boot-starter-web`

- **Spring Boot Starter Validation**
  - `org.springframework.boot:spring-boot-starter-validation`

- **Lombok**
  - `org.projectlombok:lombok`

- **Spring Boot Starter Test**
  - `org.springframework.boot:spring-boot-starter-test`

- **Spring Security Test**
  - `org.springframework.security:spring-security-test`

- **PostgreSQL JDBC Driver**
  - `org.postgresql:postgresql`

- **Springfox Boot Starter**
  - `io.springfox:springfox-boot-starter`

- **Spring Boot DevTools**
  - `org.springframework.boot:spring-boot-devtools`

- **Flyway Core**
  - `org.flywaydb:flyway-core`

- **Flyway PostgreSQL**
  - `org.flywaydb:flyway-database-postgresql`

- **Java JWT**
  - `com.auth0:java-jwt`

- **Springdoc OpenAPI WebMVC UI Starter**
  - `org.springdoc:springdoc-openapi-starter-webmvc-ui`

- **Spring Boot Starter AMQP**
  - `org.springframework.boot:spring-boot-starter-amqp`

- **Spring Rabbit Test**
  - `org.springframework.amqp:spring-rabbit-test`



# Como Executar a Aplicação

A aplicação acompanha um arquivo `docker-compose.yml` que permite a execução em um ambiente Docker.

## Passo a Passo

1. **Configurar Variáveis de Ambiente:**
  - Utilize o arquivo `.env_sample` como referência para configurar as variáveis de ambiente necessárias.
  - Renomeie o arquivo para `.env` e ajuste os valores conforme necessário.

2. **Iniciar os Containers:**
  - Entre na pasta `mobiauto-backend`:
    ```sh
    cd mobiauto-backend
    ```
  - Execute o comando para subir os containers:
    ```sh
    docker-compose up -d --build
    ```
  - Isso iniciará três containers: o banco de dados, a aplicação e o RabbitMQ.

3. ## Configurar a IDE

1. **Configure o perfil a ser usado na sua IDE:**
  - **Local:** Caso não utilize Docker.
  - **Docker:** Caso utilize Docker.

2. **Configure as variáveis de ambiente na sua IDE de acordo com o perfil escolhido.**

### Exemplo de Variáveis de Ambiente

- **Local:**
  - `POSTGRES_DB=mobiauto`
  - `POSTGRES_USER=mobiauto_user`
  - `POSTGRES_PASSWORD=mobiauto_password`
  - `SPRING_RABBITMQ_HOST=localhost`
  - `SPRING_RABBITMQ_USERNAME=guest`
  - `SPRING_RABBITMQ_PASSWORD=guest`

- **Docker:**
  - `POSTGRES_DB=mobiauto`
  - `POSTGRES_USER=mobiauto_user`
  - `POSTGRES_PASSWORD=mobiauto_password`
  - `SPRING_RABBITMQ_HOST=rabbitmq`
  - `SPRING_RABBITMQ_USERNAME=guest`
  - `SPRING_RABBITMQ_PASSWORD=guest`


# Funcionamento da Aplicação

A aplicação é uma API REST que permite a gestão de revendas, veículos, usuários, perfis, oportunidades e clientes. Os cargos são Enums que definem as permissões de acesso dos usuários.
É possível visualizar os requisitos do teste [aqui](Senior%20Backend%20Developers%20Interview.pdf).

Abaixo estão as etapas para utilização da API:

## Registro de Usuário
- **Endpoint:** `POST api/v1/auth/register`
- **Corpo da requisição:**
  ```json
  {
    "email": "teste@gmail.com",
    "senha": "senha",
    "nome": "Tester"
  }
  ```

## Login
- **Endpoint:** `POST api/v1/auth/login`
- **Corpo da requisição:**
  ```json
  {
    "email": "teste@gmail.com",
    "senha": "senha"
  }
  ```
- **Resposta:** Um token JWT será gerado.

## Obtenção de Perfil
Com o token JWT gerado no login, você terá acesso ao endpoint de Perfil. Todos os outros endpoints estarão bloqueados enquanto você não tiver um Perfil. Para obter um Perfil, é necessário que um Proprietário ou Gerente o cadastre em uma Revenda. Após obter um Perfil, você terá um ID de Perfil que será utilizado para gerar um novo token JWT.

- **Endpoint:** `POST api/v1/auth/perfil`
- **Corpo da requisição:**
  ```json
  {
    "perfilId": 18
  }
  ```
- **Resposta:** Um novo token JWT será gerado. Este token deverá ser utilizado como Header com o Auth Type Bearer Token para todas as requisições do sistema.

## Permissões de Rotas
A seção abaixo lista as permissões de cada rota.

> **Nota:** Certifique-se de usar o token JWT atualizado em todas as requisições subsequentes para garantir acesso às funcionalidades da aplicação.



# Regras de Acesso e Operações dos Endpoints

Para facilitar o entendimento, foi criado um DER (Diagrama de Entidade-Relacionamento) que pode ser visualizado [aqui](DER.png).

Também para sua comodidade, há uma [API Collection](Mobiauto.postman_collection.json) disponível para que você possa testar no Postman, Insomnia, Bruno ou qualquer outra ferramenta de sua preferência.

Por fim, há um [dump](dump.sql) com algumas inserções já realizadas, que você pode executar diretamente no banco ou subir no seu volume Docker para testar a aplicação.

O dump também possui uma conta admin com as credenciais: admin@admin e senha: admin.

A ideia da aplicação consiste em:

- Cada Cliente ser apartado da aplicação, isso é, não possuir dependências.
- As Revendas tem como ponto de acesso o Perfil.
- O Perfil relaciona Revendas, Usuarios e Cargos.
- Cargos definem as permissões de acesso.
- Veículos são de uma Revenda e fazem parte de uma Oportunidade.
- A Oportunidade é uma interação entre um Cliente, um Veículo e um Usuário da Revenda.

## 1. Revenda

### Regras Gerais
- **Autenticação:** Todas as operações requerem que o usuário esteja autenticado com um perfil.
- **Verificação de Acesso:** A verificação de acesso é realizada para operações que envolvem diferentes níveis de permissão.

### Operações e Regras Específicas
- **GET /api/v1/revendas**
  - **Admin:** Pode visualizar todas as revendas.
  - **Outros Usuários:** Podem visualizar apenas as revendas às quais têm acesso através de seu perfil.

- **GET /api/v1/revendas/{id}**
  - **Admin:** Pode visualizar qualquer revenda.
  - **Outros Usuários:** Podem visualizar apenas as revendas às quais têm acesso através de seu perfil.

- **POST /api/v1/revendas**
  - **Admin:** Apenas usuários com perfil de administrador podem criar revendas.

- **PUT /api/v1/revendas/{id}**
  - **Admin e Proprietário da Revenda:** Apenas administradores e proprietários da revenda podem atualizar a revenda.

- **DELETE /api/v1/revendas/{id}**
  - **Admin:** Apenas usuários com perfil de administrador podem deletar revendas.

## 2. Veiculo

### Regras Gerais
- **Autenticação:** Todas as operações requerem que o usuário esteja autenticado.
- **Verificação de Acesso:** A verificação de acesso é realizada para operações que envolvem diferentes níveis de permissão.

### Operações e Regras Específicas
- **GET /api/v1/veiculos**
  - **Admin:** Pode visualizar todos os veículos.
  - **Outros Usuários:** Proprietários, Gerentes e Assistentes podem visualizar os veículos da revenda à qual têm acesso.

- **GET /api/v1/veiculos/{id}**
  - **Admin e Usuários com Acesso:** Podem visualizar qualquer veículo, desde que tenham acesso à revenda do veículo.

- **GET /api/v1/veiculos/revenda/{revendaId}**
  - **Admin e Usuários com Acesso:** Podem visualizar os veículos de uma revenda específica, desde que tenham acesso a essa revenda.

- **POST /api/v1/veiculos**
  - **Admin e Proprietário:** Apenas administradores e proprietários podem criar veículos.

- **PUT /api/v1/veiculos/{id}**
  - **Admin e Proprietário:** Apenas administradores e proprietários podem atualizar veículos.

- **DELETE /api/v1/veiculos/{id}**
  - **Admin e Proprietário:** Apenas administradores e proprietários podem deletar veículos.

## 3. Usuario

### Regras Gerais
- **Autenticação:** Todas as operações requerem que o usuário esteja autenticado.
- **Verificação de Acesso:** A verificação de acesso é realizada para operações que envolvem diferentes níveis de permissão.

### Operações e Regras Específicas
- **GET /api/v1/usuarios/ativos**
  - **Admin:** Pode visualizar todos os usuários ativos.
  - **Proprietário e Gerente:** Podem visualizar os usuários ativos da revenda à qual têm acesso.

- **GET /api/v1/usuarios/inativos**
  - **Admin:** Pode visualizar todos os usuários inativos.
  - **Proprietário e Gerente:** Podem visualizar os usuários inativos da revenda à qual têm acesso.

- **GET /api/v1/usuarios/{id}**
  - **Admin e Usuários com Acesso:** Podem visualizar qualquer usuário, desde que tenham acesso à revenda do usuário.

- **POST /api/v1/usuarios**
  - **Admin:** Pode criar novos usuários.

- **PUT /api/v1/usuarios/{id}**
  - **Admin e Usuário:** Administradores podem atualizar qualquer usuário. Usuários podem atualizar seu próprio perfil.

- **PUT /api/v1/usuarios/{id}/reativar**
  - **Admin:** Pode reativar usuários inativos.

- **DELETE /api/v1/usuarios/{id}**
  - **Admin:** Pode deletar usuários. A deleção pode ser lógica ou física.

- **GET /api/v1/usuarios/{id}/oportunidades**
  - **Admin, Proprietário, Gerente e Assistente (para seu próprio perfil):** Podem visualizar as oportunidades de um usuário, desde que tenham acesso à revenda do usuário.

## 4. Perfil

### Regras Gerais
- **Autenticação:** Todas as operações requerem que o usuário esteja autenticado.
- **Verificação de Acesso:** A verificação de acesso é realizada para operações que envolvem diferentes níveis de permissão.

### Operações e Regras Específicas
- **GET /api/v1/perfis**
  - **Proprietário e Gerente:** Podem visualizar todos os perfis da revenda à qual têm acesso.
  - **Assistentes:** Não têm acesso.

- **GET /api/v1/perfis/{id}**
  - **Admin e Usuários com Acesso:** Podem visualizar qualquer perfil, desde que tenham acesso à revenda do perfil.

- **POST /api/v1/perfis**
  - **Admin, Proprietário e Gerente:** Podem criar novos perfis para a revenda à qual têm acesso. Devem ser autorizados a atribuir o cargo especificado.

- **PUT /api/v1/perfis/{id}**
  - **Admin e Proprietário:** Podem atualizar perfis, desde que tenham acesso à revenda do perfil.

- **DELETE /api/v1/perfis/{id}**
  - **Admin e Proprietário:** Podem deletar perfis.

- **GET /api/v1/perfis/revenda/{revendaId}**
  - **Admin e Usuários com Acesso:** Podem visualizar perfis de uma revenda específica, desde que tenham acesso a essa revenda.

## 5. Oportunidade

### Regras Gerais
- **Autenticação:** Todas as operações requerem que o usuário esteja autenticado.
- **Verificação de Acesso:** A verificação de acesso é realizada para operações que envolvem diferentes níveis de permissão.

### Operações e Regras Específicas
- **GET /api/v1/oportunidades**
  - **Admin:** Pode visualizar todas as oportunidades.
  - **Outros Usuários:** Podem visualizar apenas as oportunidades das revendas às quais têm acesso.

- **GET /api/v1/oportunidades/{id}**
  - **Admin e Usuários com Acesso:** Podem visualizar qualquer oportunidade, desde que tenham acesso à revenda da oportunidade.

- **POST /api/v1/oportunidades**
  - **Usuários com Perfil:** Podem criar oportunidades se estiverem logados com um perfil.

- **PUT /api/v1/oportunidades/{id}**
  - **Usuários com Permissão:** Podem atualizar oportunidades se tiverem permissão para editar.

- **DELETE /api/v1/oportunidades/{id}**
  - **Admin e Proprietário:** Podem deletar oportunidades.

- **PUT /api/v1/oportunidades/{id}/finalizar**
  - **Proprietário, Gerente e Dono da Oportunidade:** Podem finalizar uma oportunidade, desde que tenham permissão para isso.

- **POST /api/v1/oportunidades/{id}/transferir**
  - **Admin, Proprietário e Gerente:** Podem transferir oportunidades.

- **POST /api/v1/oportunidades/distribuir**
  - **Sistema:** Oportunidades são distribuídas automaticamente para assistentes disponíveis.

## 6. Cliente

### Regras Gerais
- **Autenticação:** Todas as operações requerem que o usuário esteja autenticado com um perfil.
- **Verificação de Acesso:** A verificação de acesso é realizada para operações que envolvem diferentes níveis de permissão.

### Operações e Regras Específicas
- **GET /api/v1/clientes/ativos**
  - **Admin:** Pode visualizar todos os clientes ativos.
  - **Outros Usuários:** Podem visualizar apenas os clientes ativos das revendas às quais têm acesso.

- **GET /api/v1/clientes/inativos**
  - **Admin:** Pode visualizar todos os clientes inativos.
  - **Outros Usuários:** Não têm permissão para visualizar clientes inativos.

- **GET /api/v1/clientes/{id}**
  - **Admin e Usuários com Acesso:** Podem visualizar qualquer cliente, desde que tenham acesso à revenda do cliente.

- **POST /api/v1/clientes**
  - **Usuários com Perfil:** Podem criar clientes se estiverem logados com um perfil. O email do cliente deve ser único.

- **PUT /api/v1/clientes/{id}**
  - **Usuários com Perfil:** Podem atualizar clientes se estiverem logados com um perfil. O email do cliente deve ser único.

- **PUT /api/v1/clientes/{id}/reativar**
  - **Admin:** Pode reativar clientes inativos.

- **DELETE /api/v1/clientes/{id}**
  - **Admin:** Pode deletar clientes. A deleção pode ser lógica ou física.


# Pontos de Melhoria

- Não foram ainda implementados os testes unitários e de integração.
- Não foi ainda implementado uma ferramenta de documentação automática como o Swagger.
- É possível fazer a aplicação utilizando NoSQL como MongoDB para se aproveitar da flexibilidade do schema.
- É preciso refatorar a aplicação para enchugar o código e melhorar a manutenibilidade.