1. RevendaService
   Regras Gerais
   Autenticação: Todas as operações requerem que o usuário esteja autenticado com um perfil.
   Verificação de Acesso: A verificação de acesso é realizada para operações que envolvem diferentes níveis de permissão.
   Operações e Regras Específicas
   findAll

Admin: Pode visualizar todas as revendas.
Outros Usuários: Podem visualizar apenas as revendas às quais têm acesso através de seu perfil.
findById

Admin: Pode visualizar qualquer revenda.
Outros Usuários: Podem visualizar apenas as revendas às quais têm acesso através de seu perfil.
createRevenda

Admin: Apenas usuários com perfil de administrador podem criar revendas.
updateRevenda

Admin e Proprietário da Revenda: Apenas administradores e proprietários da revenda podem atualizar a revenda.
deleteRevenda

Admin: Apenas usuários com perfil de administrador podem deletar revendas.
2. VeiculoService
   Regras Gerais
   Autenticação: Todas as operações requerem que o usuário esteja autenticado.
   Verificação de Acesso: A verificação de acesso é realizada para operações que envolvem diferentes níveis de permissão.
   Operações e Regras Específicas
   findAll

Admin: Pode visualizar todos os veículos.
Outros Usuários: Proprietários, Gerentes e Assistentes podem visualizar os veículos da revenda à qual têm acesso.
findById

Admin e Usuários com Acesso: Podem visualizar qualquer veículo, desde que tenham acesso à revenda do veículo.
findByRevenda

Admin e Usuários com Acesso: Podem visualizar os veículos de uma revenda específica, desde que tenham acesso a essa revenda.
createVeiculo

Admin e Proprietário: Apenas administradores e proprietários podem criar veículos.
updateVeiculo

Admin e Proprietário: Apenas administradores e proprietários podem atualizar veículos.
deleteVeiculo

Admin e Proprietário: Apenas administradores e proprietários podem deletar veículos.
3. UsuarioService
   Regras Gerais
   Autenticação: Todas as operações requerem que o usuário esteja autenticado.
   Verificação de Acesso: A verificação de acesso é realizada para operações que envolvem diferentes níveis de permissão.
   Operações e Regras Específicas
   findAllActive

Admin: Pode visualizar todos os usuários ativos.
Proprietário e Gerente: Podem visualizar os usuários ativos da revenda à qual têm acesso.
findAllInactive

Admin: Pode visualizar todos os usuários inativos.
Proprietário e Gerente: Podem visualizar os usuários inativos da revenda à qual têm acesso.
findById

Admin e Usuários com Acesso: Podem visualizar qualquer usuário, desde que tenham acesso à revenda do usuário.
createUsuario

Admin: Pode criar novos usuários.
updateUsuario

Admin e Usuário: Administradores podem atualizar qualquer usuário. Usuários podem atualizar seu próprio perfil.
reativarUsuario

Admin: Pode reativar usuários inativos.
delete

Admin: Pode deletar usuários. A deleção pode ser lógica ou física.
findOportunidadesByUsuario

Admin, Proprietário, Gerente e Assistente (para seu próprio perfil): Podem visualizar as oportunidades de um usuário, desde que tenham acesso à revenda do usuário.
4. PerfilService
   Regras Gerais
   Autenticação: Todas as operações requerem que o usuário esteja autenticado.
   Verificação de Acesso: A verificação de acesso é realizada para operações que envolvem diferentes níveis de permissão.
   Operações e Regras Específicas
   findAll

Proprietário e Gerente: Podem visualizar todos os perfis da revenda à qual têm acesso.
Assistentes: Não têm acesso.
findById

Admin e Usuários com Acesso: Podem visualizar qualquer perfil, desde que tenham acesso à revenda do perfil.
createPerfil

Admin, Proprietário e Gerente: Podem criar novos perfis para a revenda à qual têm acesso. Devem ser autorizados a atribuir o cargo especificado.
updatePerfil

Admin e Proprietário: Podem atualizar perfis, desde que tenham acesso à revenda do perfil.
deletePerfil

Admin e Proprietário: Podem deletar perfis.
findByRevenda

Admin e Usuários com Acesso: Podem visualizar perfis de uma revenda específica, desde que tenham acesso a essa revenda.
5. OportunidadeService
   Regras Gerais
   Autenticação: Todas as operações requerem que o usuário esteja autenticado.
   Verificação de Acesso: A verificação de acesso é realizada para operações que envolvem diferentes níveis de permissão.
   Operações e Regras Específicas
   findAll

Admin: Pode visualizar todas as oportunidades.
Outros Usuários: Podem visualizar apenas as oportunidades das revendas às quais têm acesso.
findById

Admin e Usuários com Acesso: Podem visualizar qualquer oportunidade, desde que tenham acesso à revenda da oportunidade.
createOportunidade

Usuários com Perfil: Podem criar oportunidades se estiverem logados com um perfil.
updateOportunidade

Usuários com Permissão: Podem atualizar oportunidades se tiverem permissão para editar.
deleteOportunidade

Admin e Proprietário: Podem deletar oportunidades.
finalizarOportunidade

Proprietário, Gerente e Dono da Oportunidade: Podem finalizar uma oportunidade, desde que tenham permissão para isso.
transferirOportunidade

Usuários com Permissão: Podem transferir oportunidades se tiverem permissão para isso.
distributeOportunidade

Sistema: Oportunidades são distribuídas automaticamente para assistentes disponíveis.
6. ClienteService
   Regras Gerais
   Autenticação: Todas as operações requerem que o usuário esteja autenticado com um perfil.
   Verificação de Acesso: A verificação de acesso é realizada para operações que envolvem diferentes níveis de permissão.
   Operações e Regras Específicas
   findAllActive

Admin: Pode visualizar todos os clientes ativos.
Outros Usuários: Podem visualizar apenas os clientes ativos das revendas às quais têm acesso.
findAllInactive

Admin: Pode visualizar todos os clientes inativos.
Outros Usuários: Não têm permissão para visualizar clientes inativos.
findById

Admin e Usuários com Acesso: Podem visualizar qualquer cliente, desde que tenham acesso à revenda do cliente.
createCliente

Usuários com Perfil: Podem criar clientes se estiverem logados com um perfil. O email do cliente deve ser único.
updateCliente

Usuários com Perfil: Podem atualizar clientes se estiverem logados com um perfil. O email do cliente deve ser único.
reativarCliente

Admin: Pode reativar clientes inativos.
delete

Admin: Pode deletar clientes. A deleção pode ser lógica ou física.