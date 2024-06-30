
CREATE TABLE cliente (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         nome VARCHAR(255) NOT NULL,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         telefone VARCHAR(20) NOT NULL,
                         ativo BOOLEAN NOT NULL DEFAULT true
);


CREATE TABLE cargo (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       nome VARCHAR(50) NOT NULL,  -- Storing enum as String
                       ativo BOOLEAN NOT NULL DEFAULT true
);


CREATE TABLE permissao (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           descricao VARCHAR(255) NOT NULL UNIQUE,
                           ativo BOOLEAN NOT NULL DEFAULT true
);


CREATE TABLE revenda (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         cnpj VARCHAR(20) NOT NULL UNIQUE,
                         codigo VARCHAR(50) NOT NULL UNIQUE,
                         nome_social VARCHAR(255) NOT NULL,
                         status VARCHAR(50) NOT NULL,  -- Storing enum as String
                         ativo BOOLEAN NOT NULL DEFAULT true
);


CREATE TABLE usuario (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         codigo VARCHAR(50) NOT NULL UNIQUE,
                         nome VARCHAR(255) NOT NULL,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         senha VARCHAR(255) NOT NULL,
                         ativo BOOLEAN NOT NULL DEFAULT true
);


CREATE TABLE veiculo (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         codigo VARCHAR(50) NOT NULL UNIQUE,
                         marca VARCHAR(255) NOT NULL,
                         modelo VARCHAR(255) NOT NULL,
                         versao VARCHAR(255) NOT NULL,
                         ano_modelo INT NOT NULL,
                         ativo BOOLEAN NOT NULL DEFAULT true
);


CREATE TABLE oportunidade (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              codigo VARCHAR(50) NOT NULL UNIQUE,
                              status VARCHAR(50) NOT NULL,  -- Storing enum as String
                              motivo_conclusao VARCHAR(255),
                              data_atribuicao DATETIME,
                              data_conclusao DATETIME,
                              cliente_id BIGINT NOT NULL,
                              revenda_id BIGINT NOT NULL,
                              veiculo_id BIGINT NOT NULL,
                              responsavel_atendimento_id BIGINT NOT NULL,
                              FOREIGN KEY (cliente_id) REFERENCES cliente(id),
                              FOREIGN KEY (revenda_id) REFERENCES revenda(id),
                              FOREIGN KEY (veiculo_id) REFERENCES veiculo(id),
                              FOREIGN KEY (responsavel_atendimento_id) REFERENCES usuario(id)
);


CREATE TABLE perfil (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        revenda_id BIGINT NOT NULL,
                        usuario_id BIGINT NOT NULL,
                        cargo_id BIGINT NOT NULL,
                        FOREIGN KEY (revenda_id) REFERENCES revenda(id),
                        FOREIGN KEY (usuario_id) REFERENCES usuario(id),
                        FOREIGN KEY (cargo_id) REFERENCES cargo(id)
);


CREATE TABLE CargoPermissao (
                                cargo_id BIGINT NOT NULL,
                                permissao_id BIGINT NOT NULL,
                                PRIMARY KEY (cargo_id, permissao_id),
                                FOREIGN KEY (cargo_id) REFERENCES cargo(id),
                                FOREIGN KEY (permissao_id) REFERENCES permissao(id)
);
