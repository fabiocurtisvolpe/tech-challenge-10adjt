DO $$
BEGIN
    IF EXISTS (
        SELECT 1 
        FROM information_schema.columns 
        WHERE table_schema = 'public' 
        AND table_name = 'restaurante' 
        AND column_name = 'horario_funcionamento'
    ) THEN
        ALTER TABLE public.restaurante 
        ALTER COLUMN horario_funcionamento TYPE VARCHAR(500) 
        USING horario_funcionamento::varchar;
    END IF;

    IF EXISTS (
        SELECT 1 
        FROM information_schema.columns 
        WHERE table_schema = 'public' 
        AND table_name = 'restaurante_aud' 
        AND column_name = 'horario_funcionamento'
    ) THEN
        ALTER TABLE public.restaurante_aud 
        ALTER COLUMN horario_funcionamento TYPE VARCHAR(500) 
        USING horario_funcionamento::varchar;
    END IF;

END $$;