# Mobiauto

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

3. **Configurar a IDE:**
  - Configure o perfil a ser usado na sua IDE:
    - **Local:** Caso não utilize Docker.
    - **Docker:** Caso utilize Docker.
  - Configure as variáveis de ambiente na sua IDE de acordo com o perfil escolhido.

Seguindo esses passos, você deverá ser capaz de executar a aplicação corretamente em um ambiente Docker.


# Funcionamento da Aplicação

A aplicação é uma API REST que permite a gestão de revendas, veículos, usuários, perfis, oportunidades, clientes e cargos. Abaixo estão as etapas para utilização da API:

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

## 7. Cargo

### Regras Gerais
- **Autenticação:** Todas as operações requerem que o usuário esteja autenticado como superusuário.
- **Verificação de Acesso:** Apenas superusuários têm permissão para executar as operações.

### Operações e Regras Específicas
- **GET /api/v1/cargos**
  - **Superusuário:** Pode visualizar todos os cargos.

- **GET /api/v1/cargos/{id}**
  - **Superusuário:** Pode visualizar um cargo específico pelo ID.

- **POST /api/v1/cargos**
  - **Superusuário:** Pode criar um novo cargo.

- **PUT /api/v1/cargos/{id}**
  - **Superusuário:** Pode atualizar um cargo específico pelo ID.

- **DELETE /api/v1/cargos/{id}**
  - **Superusuário:** Pode deletar um cargo específico pelo ID.


# Pontos de Melhoria

- Não foram ainda implementados os testes unitários e de integração.
- Não foi ainda implementado uma ferramenta de documentação automática como o Swagger.
- É possível fazer a aplicação utilizando NoSQL como MongoDB para se aproveitar da flexibilidade do schema.
- O RabbitMQ não está funcionando como deveria.
- Não está sendo realizada a conexão do RabbitMQ com a aplicação sem o uso do Docker.
- É preciso refatorar a aplicação para enchugar o código e melhorar a manutenibilidade.