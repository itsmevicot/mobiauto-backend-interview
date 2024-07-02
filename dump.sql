-- Inserir dados nas tabelas

-- Revendas
INSERT INTO revenda (cnpj, codigo, nome_social, ativo) VALUES
                                                           ('12345678000100', 'R001', 'Revenda A', TRUE),
                                                           ('23456789000111', 'R002', 'Revenda B', TRUE),
                                                           ('34567890000122', 'R003', 'Revenda C', TRUE);

-- Usuarios
INSERT INTO usuario (codigo, nome, email, senha, ativo, is_superuser) VALUES
                                                            ('U100', 'Super User', 'admin@admin', 'admin', TRUE, TRUE),
                                                            ('U001', 'Proprietario A', 'proprietario.a@example.com', 'password', TRUE, FALSE),
                                                            ('U002', 'Gerente A', 'gerente.a@example.com', 'password', TRUE, FALSE),
                                                            ('U003', 'Assistente A1', 'assistente.a1@example.com', 'password', TRUE, FALSE),
                                                            ('U004', 'Assistente A2', 'assistente.a2@example.com', 'password', TRUE, FALSE),
                                                            ('U005', 'Assistente A3', 'assistente.a3@example.com', 'password', TRUE, FALSE),
                                                            ('U006', 'Proprietario B', 'proprietario.b@example.com', 'password', TRUE, FALSE),
                                                            ('U007', 'Gerente B', 'gerente.b@example.com', 'password', TRUE, FALSE),
                                                            ('U008', 'Assistente B1', 'assistente.b1@example.com', 'password', TRUE, FALSE),
                                                            ('U009', 'Assistente B2', 'assistente.b2@example.com', 'password', TRUE, FALSE),
                                                            ('U010', 'Assistente B3', 'assistente.b3@example.com', 'password', TRUE, FALSE),
                                                            ('U011', 'Proprietario C', 'proprietario.c@example.com', 'password', TRUE, FALSE),
                                                            ('U012', 'Gerente C', 'gerente.c@example.com', 'password', TRUE, FALSE),
                                                            ('U013', 'Assistente C1', 'assistente.c1@example.com', 'password', TRUE, FALSE),
                                                            ('U014', 'Assistente C2', 'assistente.c2@example.com', 'password', TRUE, FALSE),
                                                            ('U015', 'Assistente C3', 'assistente.c3@example.com', 'password', TRUE, FALSE);

-- Perfis para Revenda A
INSERT INTO perfil (revenda_id, usuario_id, cargo) VALUES
                                                       (1, 1, 'PROPRIETARIO'), -- Proprietario A
                                                       (1, 2, 'GERENTE'),      -- Gerente A
                                                       (1, 3, 'ASSISTENTE'),   -- Assistente A1
                                                       (1, 4, 'ASSISTENTE'),   -- Assistente A2
                                                       (1, 5, 'ASSISTENTE');   -- Assistente A3

-- Perfis para Revenda B
INSERT INTO perfil (revenda_id, usuario_id, cargo) VALUES
                                                       (2, 6, 'PROPRIETARIO'), -- Proprietario B
                                                       (2, 7, 'GERENTE'),      -- Gerente B
                                                       (2, 8, 'ASSISTENTE'),   -- Assistente B1
                                                       (2, 9, 'ASSISTENTE'),   -- Assistente B2
                                                       (2, 10, 'ASSISTENTE');  -- Assistente B3

-- Perfis para Revenda C
INSERT INTO perfil (revenda_id, usuario_id, cargo) VALUES
                                                       (3, 11, 'PROPRIETARIO'), -- Proprietario C
                                                       (3, 12, 'GERENTE'),      -- Gerente C
                                                       (3, 13, 'ASSISTENTE'),   -- Assistente C1
                                                       (3, 14, 'ASSISTENTE'),   -- Assistente C2
                                                       (3, 15, 'ASSISTENTE');   -- Assistente C3

-- Veiculos para Revenda A
INSERT INTO veiculo (codigo, marca, modelo, versao, ano_modelo, ativo, revenda_id) VALUES
                                                                                       ('V001A', 'Marca A', 'Modelo A1', 'Versao A1', 2021, TRUE, 1),
                                                                                       ('V002A', 'Marca A', 'Modelo A2', 'Versao A2', 2022, TRUE, 1),
                                                                                       ('V003A', 'Marca A', 'Modelo A3', 'Versao A3', 2023, TRUE, 1),
                                                                                       ('V004A', 'Marca A', 'Modelo A4', 'Versao A4', 2021, TRUE, 1),
                                                                                       ('V005A', 'Marca A', 'Modelo A5', 'Versao A5', 2022, TRUE, 1);

-- Veiculos para Revenda B
INSERT INTO veiculo (codigo, marca, modelo, versao, ano_modelo, ativo, revenda_id) VALUES
                                                                                       ('V001B', 'Marca B', 'Modelo B1', 'Versao B1', 2021, TRUE, 2),
                                                                                       ('V002B', 'Marca B', 'Modelo B2', 'Versao B2', 2022, TRUE, 2),
                                                                                       ('V003B', 'Marca B', 'Modelo B3', 'Versao B3', 2023, TRUE, 2),
                                                                                       ('V004B', 'Marca B', 'Modelo B4', 'Versao B4', 2021, TRUE, 2),
                                                                                       ('V005B', 'Marca B', 'Modelo B5', 'Versao B5', 2022, TRUE, 2);

-- Veiculos para Revenda C
INSERT INTO veiculo (codigo, marca, modelo, versao, ano_modelo, ativo, revenda_id) VALUES
                                                                                       ('V001C', 'Marca C', 'Modelo C1', 'Versao C1', 2021, TRUE, 3),
                                                                                       ('V002C', 'Marca C', 'Modelo C2', 'Versao C2', 2022, TRUE, 3),
                                                                                       ('V003C', 'Marca C', 'Modelo C3', 'Versao C3', 2023, TRUE, 3),
                                                                                       ('V004C', 'Marca C', 'Modelo C4', 'Versao C4', 2021, TRUE, 3),
                                                                                       ('V005C', 'Marca C', 'Modelo C5', 'Versao C5', 2022, TRUE, 3);

-- Clientes
INSERT INTO cliente (nome, email, telefone, ativo) VALUES
                                                       ('Cliente A1', 'cliente.a1@example.com', '1111-1111', TRUE),
                                                       ('Cliente A2', 'cliente.a2@example.com', '2222-2222', TRUE),
                                                       ('Cliente A3', 'cliente.a3@example.com', '3333-3333', TRUE),
                                                       ('Cliente B1', 'cliente.b1@example.com', '4444-4444', TRUE),
                                                       ('Cliente B2', 'cliente.b2@example.com', '5555-5555', TRUE),
                                                       ('Cliente B3', 'cliente.b3@example.com', '6666-6666', TRUE),
                                                       ('Cliente C1', 'cliente.c1@example.com', '7777-7777', TRUE),
                                                       ('Cliente C2', 'cliente.c2@example.com', '8888-8888', TRUE),
                                                       ('Cliente C3', 'cliente.c3@example.com', '9999-9999', TRUE);

-- Oportunidades para Assistentes em Revenda A
INSERT INTO oportunidade (codigo, status, motivo_conclusao, data_atribuicao, data_conclusao, cliente_id, revenda_id, veiculo_id, responsavel_atendimento_id) VALUES
                                                                                                                                                                 ('O001A', 'NOVO', NULL, '2023-01-01 10:00:00', NULL, 1, 1, 1, 3), -- Oportunidade para Assistente A1
                                                                                                                                                                 ('O002A', 'NOVO', NULL, '2023-01-01 10:00:00', NULL, 2, 1, 2, 4), -- Oportunidade para Assistente A2
                                                                                                                                                                 ('O003A', 'NOVO', NULL, '2023-01-01 10:00:00', NULL, 3, 1, 3, 5); -- Oportunidade para Assistente A3

-- Oportunidades para Assistentes em Revenda B
INSERT INTO oportunidade (codigo, status, motivo_conclusao, data_atribuicao, data_conclusao, cliente_id, revenda_id, veiculo_id, responsavel_atendimento_id) VALUES
                                                                                                                                                                 ('O001B', 'NOVO', NULL, '2023-01-01 10:00:00', NULL, 4, 2, 6, 8), -- Oportunidade para Assistente B1
                                                                                                                                                                 ('O002B', 'NOVO', NULL, '2023-01-01 10:00:00', NULL, 5, 2, 7, 9), -- Oportunidade para Assistente B2
                                                                                                                                                                 ('O003B', 'NOVO', NULL, '2023-01-01 10:00:00', NULL, 6, 2, 8, 10); -- Oportunidade para Assistente B3

-- Oportunidades para Assistentes em Revenda C
INSERT INTO oportunidade (codigo, status, motivo_conclusao, data_atribuicao, data_conclusao, cliente_id, revenda_id, veiculo_id, responsavel_atendimento_id) VALUES
                                                                                                                                                                 ('O001C', 'NOVO', NULL, '2023-01-01 10:00:00', NULL, 7, 3, 11, 13), -- Oportunidade para Assistente C1
                                                                                                                                                                 ('O002C', 'NOVO', NULL, '2023-01-01 10:00:00', NULL, 8, 3, 12, 14), -- Oportunidade para Assistente C2
                                                                                                                                                                 ('O003C', 'NOVO', NULL, '2023-01-01 10:00:00', NULL, 9, 3, 13, 15); -- Oportunidade para Assistente C3
