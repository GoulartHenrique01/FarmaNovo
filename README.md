# FarmaNovo 💊

O **FarmaNovo** é um sistema web desenvolvido para auxiliar no **gerenciamento de uma farmácia**, com foco no controle e organização de **receitas médicas, medicamentos, pacientes e usuários**.

O projeto surgiu com a ideia de substituir processos manuais, como anotações e documentos físicos, por uma solução digital que facilite o **cadastro, consulta e organização das informações**.

## Para que serve?

O sistema tem como objetivo centralizar as principais informações utilizadas no dia a dia de uma farmácia, permitindo um gerenciamento mais organizado dos dados.

Entre os principais recursos estão:

* Cadastro e gerenciamento de usuários;
* Cadastro e gerenciamento de pacientes;
* Cadastro e gerenciamento de medicamentos;
* Cadastro e gerenciamento de receitas médicas;
* Sistema de login e autenticação;
* Recuperação de senha.

## Tecnologias

O projeto está sendo desenvolvido utilizando:

* **Next.js / React / TypeScript** no Frontend;
* **Java / Spring Boot** no Backend;
* **PostgreSQL** como banco de dados.

## Identidades e acesso

O cadastro da empresa cria uma identidade própria: o e-mail e a senha informados no cadastro são usados para entrar no sistema. Os usuários da empresa têm contas separadas e não compartilham a identidade ou as credenciais da empresa.

O backend aplica os escopos de acesso: a empresa gerencia somente seus usuários e medicamentos; usuários comuns consultam medicamentos da própria empresa e acessam somente suas receitas e pacientes; administradores globais mantêm acesso administrativo. Pacientes não possuem credenciais e ficam vinculados à empresa e ao usuário que os cadastrou. A empresa não consulta nem gerencia pacientes e não pode cadastrar receitas em nome dos funcionários. As senhas são armazenadas com BCrypt.

A estrutura anterior não possui um mecanismo de migração versionada. Antes de iniciar a versão com pacientes, execute uma vez o script `src/main/resources/db/migration-paciente.sql` no PostgreSQL. Ele remove a tabela antiga de farmacêuticos e seus registros, preserva a referência antiga de paciente das receitas e converte somente os vínculos de receita cujo responsável e empresa podem ser confirmados. Vínculos sem confirmação não são associados a pacientes por suposição.

## Status

🚧 **Em desenvolvimento**

O projeto ainda está sendo desenvolvido e novas funcionalidades serão adicionadas conforme sua evolução.
