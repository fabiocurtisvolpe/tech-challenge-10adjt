DO $$ BEGIN -- Remove índice parcial se existir
IF EXISTS (
  SELECT 1
  FROM pg_indexes
  WHERE schemaname = 'public'
    AND indexname = 'uq_usuario_email_ativo'
) THEN EXECUTE 'DROP INDEX public.uq_usuario_email_ativo';
END IF;
-- Remove índice global antigo se existir
IF EXISTS (
  SELECT 1
  FROM pg_indexes
  WHERE schemaname = 'public'
    AND indexname = 'uq_usuario_email'
) THEN EXECUTE 'DROP INDEX public.uq_usuario_email';
END IF;
-- Garante que e_mail seja NOT NULL
IF EXISTS (
  SELECT 1
  FROM information_schema.columns
  WHERE table_schema = 'public'
    AND table_name = 'usuario'
    AND column_name = 'e_mail'
) THEN EXECUTE 'ALTER TABLE public.usuario ALTER COLUMN e_mail SET NOT NULL';
END IF;
-- Cria índice único global para e_mail
EXECUTE 'CREATE UNIQUE INDEX uq_usuario_email ON public.usuario (e_mail)';
END $$;