# Fluxos do FarmaNovo / FarmaControll

Este documento descreve o comportamento encontrado no código-fonte enviado. A pasta `back/` (Desktop JavaFX) foi preservada sem qualquer alteração nesta revisão; por isso, o caminho do Desktop é explicado aqui, mas seus arquivos não receberam comentários nem mudanças.

## 1. Cadastro de administrador pelo Desktop — causa do HTTP 403

1. O usuário abre o aplicativo JavaFX e informa a chave local na tela `login-view.fxml`.
2. `LoginController.onLoginButtonClick()` compara o texto informado com uma chave fixa local. Esse login apenas libera a navegação do Desktop; não autentica na API e não gera JWT.
3. `MenuController.abrirCadastro()` abre `cadastro-usuario-view.fxml`, cuja tela é controlada por `UsuarioController`.
4. `UsuarioController.salvarUsuario()` monta um JSON com nome, CPF, e-mail, senha e `secretKey`, e envia `POST http://localhost:8080/usuarios/admin`.
5. No backend, `SecurityConfiguration` permite esse POST e `JwtFilter` ignora essa rota. Portanto, ela não exige JWT.
6. `presentation.UsuarioController.criarAdmin()` encaminha o DTO `CriarAdminRequest` para `UsuarioService.criarAdmin()`.
7. O service verifica a chave, os campos obrigatórios, a unicidade do e-mail e executa `usuarioRepository.existsByRole("ROLE_ADMIN")`.
8. **Se já existe um registro `ROLE_ADMIN`, o service lança `AccessDeniedException`. O Spring converte essa exceção em HTTP 403.** O Desktop mostra apenas o código HTTP, pois não lê o corpo da resposta.
9. Se não existir administrador, a chave precisa estar configurada no backend e ser igual à `secretKey` enviada pelo Desktop. A entidade `Usuario` é criada e salva por `UsuarioRepository`.

### O que cada resposta significa nesse cadastro

- `403`: já existe um administrador global (`ROLE_ADMIN`). A regra atual permite somente um.
- `400`: a chave enviada não confere, faltam campos ou o e-mail já está cadastrado.
- `500` com mensagem de configuração: `spring.admin-registration-key` não foi configurada no ambiente.
- `2xx`: cadastro concluído.

O `application.properties` agora declara `spring.admin-registration-key=${ADMIN_REGISTRATION_KEY:}`. No PowerShell usado para iniciar a API, defina `ADMIN_REGISTRATION_KEY` com exatamente o mesmo valor que o Desktop envia no campo `secretKey`. A chave atualmente está escrita no fonte do Desktop; isso é aceitável apenas como configuração de demonstração local, não como segredo de produção.

**Importante:** a tela é “Cadastro Admin”. Ela não cria um usuário de empresa. A conta da empresa é criada pelo fluxo separado de cadastro de empresa (`POST /empresas`) e recebe `ROLE_USER`.

## 2. Cadastro de empresa (conta ROLE_USER)

1. A tela web `front/app/cadastro-empresa/page.tsx` coleta os dados da empresa e do responsável.
2. `front/app/services/empresaService.ts` envia o objeto para `POST /empresas`. O Desktop também possui uma tela de cadastro de empresa em `EmpresaController`, mas seus arquivos foram mantidos intactos.
3. `presentation.EmpresaController.criar()` recebe `EmpresaRequest` e chama `EmpresaService.salvarEmpresa(request, 0L)`.
4. O service valida campos obrigatórios, CNPJ e e-mail duplicado; cria a entidade `Empresa`; copia os dados e persiste pelo `EmpresaRepository`.
5. `Empresa` representa a conta única da empresa e sua autoridade é `ROLE_USER`.
6. O frontend apresenta confirmação e encaminha a pessoa para o login.

## 3. Login web e autenticação JWT

1. `front/app/login/page.tsx` recolhe e-mail e senha.
2. `authService.loginBack()` envia `POST /auth/login`.
3. `AuthController.login()` chama `UsuarioService.validarUsuarioAutenticadoRetornaToken()`.
4. O service procura primeiro uma conta `Empresa` ativa; se não encontrar, verifica um `Usuario` administrador ativo.
5. `TokenService` assina o JWT e grava o token na tabela `token`.
6. A tela chama `GET /auth/me` com o token. `JwtFilter` valida o token; `TokenService.verificarToken()` consulta o token persistido e reconstrói `UsuarioLogado`.
7. A identidade e o token são armazenados na store Redux e a tela navega para `/home`.
8. Nas próximas requisições, `services/api.ts` injeta `Authorization: Bearer ...`. Se uma resposta protegida for 401, o interceptor despacha `logout()`.

O estado Redux é volátil. Apesar de existir `authCookies.ts`, ele não é usado pelo login atual; atualizar a página reinicia a store e encerra a sessão no frontend.

## 4. Autorização das rotas

1. `SecurityConfiguration` define quais caminhos são públicos e quais exigem `ROLE_ADMIN` ou `ROLE_USER`.
2. Para rotas protegidas, `JwtFilter` lê o cabeçalho `Authorization`, chama `TokenService.verificarToken()` e grava o principal no `SecurityContextHolder`.
3. O Spring Security confere a autoridade exigida pela rota.
4. Nos services operacionais, `UsuarioService.getUsuarioLogado()` recupera a identidade e os services usam `empresaId` nas consultas.
5. O layout e a Sidebar do frontend também ocultam rotas conforme a role, mas essa proteção visual não substitui a validação no backend.

## 5. Medicamentos

1. A tela de medicamentos chama funções de `medicamentoService.ts`.
2. O Axios envia a requisição à rota `/medicamentos` com JWT.
3. `MedicamentoController` recebe e delega a operação a `MedicamentoService`.
4. O service identifica a empresa pelo token, confirma que ela está ativa, valida os dados e define o vínculo `medicamento.empresa`.
5. `MedicamentoRepository` grava ou consulta os dados; listagens e buscas usam `empresa_id`.
6. `MedicamentoResponse` transforma a entidade em resposta JSON.

O endpoint `/medicamentos/tipo` também exige empresa autenticada e cria um registro de tipo vinculado à empresa.

## 6. Pacientes

1. A tela chama `pacienteService.ts`, que envia os campos do paciente para `/pacientes`.
2. `PacienteController` encaminha o DTO para `PacienteService`.
3. O service recupera a empresa autenticada, valida os dados e o CPF, e associa a entidade `Paciente` à empresa.
4. `PacienteRepository` persiste e consulta usando `empresa_id`.
5. Exclusão é lógica: o status passa a `EXCLUIDO`, sem apagar a linha.

## 7. Receitas e itens de medicamento

1. `ReceitaForm.tsx` carrega pacientes e medicamentos existentes por meio dos services correspondentes.
2. A pessoa escolhe um paciente e seleciona um ou mais medicamentos já cadastrados. O formulário envia os IDs e os dados da prescrição de cada item.
3. `receitaService.ts` monta `ReceitaRequest` e envia `POST /receitas` ou `PUT /receitas/{id}`.
4. `ReceitaMedicaController` encaminha o DTO ao `ReceitaService`.
5. O service valida a empresa autenticada, o paciente e cada medicamento; confirma que todos pertencem à mesma empresa e estão ativos.
6. A entidade `Receita` recebe o vínculo da empresa e do paciente. Cada medicamento selecionado gera uma entidade `ReceitaMedicamento`, que guarda dosagem, posologia, quantidade prescrita e duração.
7. `ReceitaRepository.save()` persiste a receita e os itens em cascata na mesma transação. `ReceitaResponse` converte o resultado para JSON.

A quantidade prescrita não altera automaticamente o estoque. O medicamento do catálogo é referenciado, não recriado.

## 8. Empresas e administração

1. O frontend administrativo consulta `/empresas` usando `empresaService.ts`.
2. `EmpresaController` recebe a requisição e `EmpresaService` exige `ROLE_ADMIN` nas consultas, edições, bloqueios e exclusões.
3. O repository consulta ou persiste `Empresa`; respostas são convertidas em `EmpresaResponse`.
4. Bloquear/ativar altera status. Excluir marca `EXCLUIDO`, sem remoção física.

## 9. Endereço por CEP

1. `EnderecoController` recebe `GET /endereco/{cep}`.
2. `EnderecoService` chama a interface `IViaCepExternalRepository`.
3. `ViaCepExternalRepository` consulta o serviço externo com `HttpClient`, converte o JSON em `ViaCepResponse` e devolve o resultado.
4. `EnderecoService` monta `EnderecoResponse`, que é devolvido ao cliente. O contrato atual é síncrono.

## 10. Organização das camadas

- **Tela / componente:** coleta dados e apresenta resultado.
- **Service do frontend:** prepara DTO e executa chamada HTTP com Axios.
- **Controller Spring:** recebe URL, método HTTP e DTO; define o status da resposta.
- **Service Spring:** executa validações, regras de negócio, autorização por empresa e coordena a operação.
- **Repository:** executa consultas e persistência JPA.
- **Entidade:** representa a tabela e os relacionamentos do banco.
- **DTO:** define os dados que entram e saem da API, evitando expor diretamente a entidade.

## 11. Pontos encontrados na revisão

- O CRUD de usuários do frontend (`/usuarios`) é legado: o backend atual bloqueia essas rotas e não fornece esses endpoints. O cadastro atual de `ROLE_USER` é feito por empresa; `Usuario` é usado para o administrador global.
- O login do Desktop é uma verificação local de chave e não cria sessão JWT na API. Por isso, o cadastro de tipo de medicamento do Desktop chama `/medicamentos/tipo` sem `Authorization`; essa rota exige `ROLE_USER` e o JWTFilter retorna 401. O Desktop foi preservado, então esse comportamento foi apenas documentado.
- Senhas de `Usuario` e `Empresa` são comparadas em texto puro. É uma limitação de segurança do código atual; esta revisão não introduziu PasswordEncoder/BCrypt.
- O frontend mantém sessão somente em Redux, embora exista um utilitário de cookies não integrado.
- `ViaCepExternalRepository` tinha `@Async`, mas o contrato retorna `ViaCepResponse` diretamente e não há `@EnableAsync`; a anotação foi removida para manter o fluxo síncrono coerente.
- O `pom.xml` da API e do Desktop exige Java 25. O ambiente de análise disponível para esta revisão possui Java 21; portanto, não foi possível confirmar build completo com a versão exigida.
- `spring.secretkey` possui um valor padrão de desenvolvimento em `application.properties`; antes de qualquer implantação, defina `JWT_SECRET` com um segredo forte e exclusivo.
- `MedicamentoService.criarTipo()` não valida se `request` ou seus campos estão vazios antes de construir a entidade; chamadas inválidas podem gerar erro de servidor em vez de HTTP 400.
