BEGIN;

-- ==============================================================================
-- 1. Adicionar a coluna 'tipo_cozinha' (VARCHAR) para armazenar o Enum
--    Verifica se a coluna já existe antes de adicionar (IF NOT EXISTS)
-- ==============================================================================

ALTER TABLE public.restaurante 
ADD COLUMN IF NOT EXISTS tipo_cozinha VARCHAR(50);

ALTER TABLE public.restaurante_aud 
ADD COLUMN IF NOT EXISTS tipo_cozinha VARCHAR(50);


-- ==============================================================================
-- 2. Migração de Dados (Cópia dos valores)
--    Antes de apagar a tabela antiga, copiamos o nome para a nova coluna.
-- ==============================================================================

-- Atualiza tabela Restaurante
UPDATE public.restaurante r
SET tipo_cozinha = tc.nome
FROM public.tipo_cozinha tc
WHERE r.tipo_cozinha_id = tc.id
  AND r.tipo_cozinha IS NULL; -- Garante que só atualiza se estiver vazio

-- Atualiza tabela de Auditoria (Mantendo histórico consistente)
UPDATE public.restaurante_aud ra
SET tipo_cozinha = tc.nome
FROM public.tipo_cozinha tc
WHERE ra.tipo_cozinha_id = tc.id
  AND ra.tipo_cozinha IS NULL;


-- ==============================================================================
-- 3. Remover a Constraint (Foreign Key) de forma segura
-- ==============================================================================

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_restaurante_tipo_cozinha') THEN
        ALTER TABLE public.restaurante DROP CONSTRAINT fk_restaurante_tipo_cozinha;
    END IF;
END $$;


-- ==============================================================================
-- 4. Remover a coluna antiga 'tipo_cozinha_id'
-- ==============================================================================

ALTER TABLE public.restaurante 
DROP COLUMN IF EXISTS tipo_cozinha_id;

ALTER TABLE public.restaurante_aud 
DROP COLUMN IF EXISTS tipo_cozinha_id;


-- ==============================================================================
-- 5. Remover as tabelas antigas
-- ==============================================================================

DROP TABLE IF EXISTS public.tipo_cozinha CASCADE;
DROP TABLE IF EXISTS public.tipo_cozinha_aud CASCADE;

COMMIT;