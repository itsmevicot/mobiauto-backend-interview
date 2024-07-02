-- Criando os tipos ENUM
CREATE TYPE cargos_enum AS ENUM ('PROPRIETARIO', 'GERENTE', 'ASSISTENTE');
CREATE TYPE status_oportunidade_enum AS ENUM ('NOVO', 'EM_ATENDIMENTO', 'CONCLUIDO');

-- Tabela revenda
CREATE TABLE revenda (
                         id SERIAL PRIMARY KEY,
                         cnpj VARCHAR(255) UNIQUE NOT NULL,
                         codigo VARCHAR(255) UNIQUE NOT NULL,
                         nome_social VARCHAR(255) NOT NULL,
                         ativo BOOLEAN NOT NULL DEFAULT TRUE
);

-- Tabela cliente
CREATE TABLE cliente (
                         id SERIAL PRIMARY KEY,
                         nome VARCHAR(255) NOT NULL,
                         email VARCHAR(255) UNIQUE NOT NULL,
                         telefone VARCHAR(255) NOT NULL,
                         ativo BOOLEAN NOT NULL DEFAULT TRUE
);

-- Tabela usuario
CREATE TABLE usuario (
                         id SERIAL PRIMARY KEY,
                         codigo VARCHAR(255) UNIQUE NOT NULL,
                         nome VARCHAR(255) NOT NULL,
                         email VARCHAR(255) UNIQUE NOT NULL,
                         senha VARCHAR(255) NOT NULL,
                         is_superuser BOOLEAN NOT NULL DEFAULT FALSE,
                         ativo BOOLEAN NOT NULL DEFAULT TRUE

);

-- Tabela perfil
CREATE TABLE perfil (
                        id SERIAL PRIMARY KEY,
                        revenda_id INT REFERENCES revenda(id) NOT NULL,
                        usuario_id INT REFERENCES usuario(id) NOT NULL,
                        cargo cargos_enum NOT NULL -- Usando cargos_enum diretamente
);

-- Tabela veiculo
CREATE TABLE veiculo (
                         id SERIAL PRIMARY KEY,
                         codigo VARCHAR(255) UNIQUE NOT NULL,
                         marca VARCHAR(255) NOT NULL,
                         modelo VARCHAR(255) NOT NULL,
                         versao VARCHAR(255) NOT NULL,
                         ano_modelo INT NOT NULL,
                         ativo BOOLEAN NOT NULL DEFAULT TRUE,
                         revenda_id INT REFERENCES revenda(id) NOT NULL
);

-- Tabela oportunidade
CREATE TABLE oportunidade (
                              id SERIAL PRIMARY KEY,
                              codigo VARCHAR(255) UNIQUE NOT NULL,
                              status status_oportunidade_enum NOT NULL,
                              motivo_conclusao VARCHAR(255),
                              data_atribuicao TIMESTAMP,
                              data_conclusao TIMESTAMP,
                              cliente_id INT REFERENCES cliente(id) NOT NULL,
                              revenda_id INT REFERENCES revenda(id) NOT NULL,
                              veiculo_id INT REFERENCES veiculo(id) NOT NULL,
                              responsavel_atendimento_id INT REFERENCES usuario(id) NULL
);
