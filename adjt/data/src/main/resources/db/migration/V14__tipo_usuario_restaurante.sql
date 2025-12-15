DO $$
BEGIN
  
  IF NOT EXISTS (
    SELECT 1
    FROM information_schema.columns
    WHERE table_schema = 'public'
      AND table_name = 'tipo_usuario'
      AND column_name = 'restaurante_id'
  ) THEN
    ALTER TABLE public.tipo_usuario ADD COLUMN restaurante_id INT NULL;

    ALTER TABLE public.tipo_usuario
      ADD CONSTRAINT fk_tipo_usuario_restaurante
      FOREIGN KEY (restaurante_id) REFERENCES public.restaurante(id);
  END IF;

  IF NOT EXISTS (
    SELECT 1
    FROM information_schema.columns
    WHERE table_schema = 'public'
      AND table_name = 'tipo_usuario_aud'
      AND column_name = 'restaurante_id'
  ) THEN
    ALTER TABLE public.tipo_usuario_aud ADD COLUMN restaurante_id INT NULL;
  END IF;

  IF NOT EXISTS (
    SELECT 1
    FROM pg_indexes
    WHERE schemaname = 'public'
      AND tablename = 'tipo_usuario'
      AND indexname = 'uq_tipo_usuario_nome_restaurante'
  ) THEN
    CREATE UNIQUE INDEX uq_tipo_usuario_nome_restaurante
    ON public.tipo_usuario (restaurante_id, nome);
  END IF;

END $$;
