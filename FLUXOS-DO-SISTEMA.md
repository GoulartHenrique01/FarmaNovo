# Fluxos principais do FarmaControll

## 1. Autenticação e acesso
1. A empresa ou o administrador informa suas credenciais na tela de login.
2. O backend valida os dados e emite um token JWT.
3. O frontend envia esse token nas chamadas seguintes à API.
4. O filtro JWT identifica a conta e o Spring Security aplica as permissões.
5. `ROLE_ADMIN` gerencia empresas; `ROLE_USER` representa a conta única da empresa e gerencia seus próprios cadastros.

## 2. Cadastro de medicamentos
1. A empresa abre a tela Medicamentos e preenche o formulário.
2. O frontend envia os dados ao endpoint de medicamentos.
3. O service identifica a empresa autenticada e valida os dados.
4. O medicamento é salvo vinculado à empresa.
5. As listagens posteriores retornam somente os medicamentos daquela empresa.

## 3. Cadastro de pacientes
1. A empresa preenche os dados do paciente.
2. O backend valida os dados e associa o paciente à empresa autenticada.
3. A listagem e as operações posteriores respeitam esse vínculo.

## 4. Cadastro de receitas e seleção de medicamentos
1. Ao abrir o formulário de receita, o frontend consulta os pacientes e medicamentos ativos da empresa.
2. O usuário seleciona um paciente e marca um ou mais medicamentos existentes usando caixas de seleção.
3. Para cada medicamento selecionado, informa dosagem prescrita, posologia, quantidade e duração do tratamento.
4. O frontend envia os identificadores dos cadastros existentes; não cria medicamentos durante esse fluxo.
5. O backend valida se o paciente e todos os medicamentos pertencem à empresa autenticada e estão ativos.
6. A receita e seus itens são gravados na mesma transação. A entidade `ReceitaMedicamento` guarda os dados específicos de cada item.
7. A quantidade prescrita não altera automaticamente o estoque.

## 5. Consulta e exclusão de receitas
1. A tela solicita a lista de receitas da empresa autenticada.
2. O backend retorna somente receitas do escopo da empresa.
3. A exclusão é lógica: o registro recebe o status `EXCLUIDO`, preservando o histórico.

## 6. Organização do código
- **Controller:** recebe a requisição HTTP e devolve a resposta.
- **Service:** concentra validações, regras de negócio e controle de acesso aos dados da empresa.
- **Repository:** consulta e persiste as entidades no banco.
- **Entidade:** representa os dados e relacionamentos persistidos.
- **DTO:** define os dados que entram e saem da API.
- **Frontend:** apresenta formulários e tabelas e chama os serviços HTTP.

> Observação: o campo `tipo` foi retirado do fluxo da receita (formulário, DTO e listagem). A propriedade legada da entidade foi mantida vazia para compatibilidade com bancos existentes; ela não é mais solicitada nem retornada pela API.
