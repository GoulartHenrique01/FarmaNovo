-- Execute uma única vez em uma base PostgreSQL já existente do FarmaControll.
-- Os vínculos antigos com usuários são preservados, mas passam a ser opcionais.
ALTER TABLE paciente ALTER COLUMN usuario_id DROP NOT NULL;
ALTER TABLE receita ALTER COLUMN usuario_id DROP NOT NULL;

-- Cria a tabela de itens da receita (uma receita pode ter vários medicamentos).
CREATE TABLE IF NOT EXISTS receita_medicamento (
    id BIGSERIAL PRIMARY KEY,
    receita_id BIGINT NOT NULL REFERENCES receita(id),
    medicamento_id BIGINT NOT NULL REFERENCES medicamento(id),
    dosagem_prescrita VARCHAR(255),
    posologia VARCHAR(1000),
    quantidade_prescrita INTEGER,
    duracao_tratamento VARCHAR(255)
);

-- Impede repetir o mesmo medicamento na mesma receita.
CREATE UNIQUE INDEX IF NOT EXISTS ux_receita_medicamento_receita_medicamento
    ON receita_medicamento (receita_id, medicamento_id);

-- Migra os medicamentos vinculados ao modelo antigo (um medicamento por receita).
-- A coluna antiga é mantida para permitir conferência/rollback; a aplicação passa a usar a tabela nova.
DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'receita' AND column_name = 'medicamento_id'
    ) THEN
        INSERT INTO receita_medicamento (receita_id, medicamento_id)
        SELECT r.id, r.medicamento_id
        FROM receita r
        WHERE r.medicamento_id IS NOT NULL
        ON CONFLICT (receita_id, medicamento_id) DO NOTHING;
    END IF;
END $$;
