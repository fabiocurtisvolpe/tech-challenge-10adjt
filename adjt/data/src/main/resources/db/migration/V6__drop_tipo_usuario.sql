DO $$ BEGIN -- Excluir tabelas tipo_usuario e tipo_usuario_aud se existirem
IF EXISTS (
  SELECT 1
  FROM information_schema.tables
  WHERE table_schema = 'public'
    AND table_name = 'tipo_usuario'
) THEN EXECUTE 'DROP TABLE public.tipo_usuario CASCADE';
END IF;
IF EXISTS (
  SELECT 1
  FROM information_schema.tables
  WHERE table_schema = 'public'
    AND table_name = 'tipo_usuario_aud'
) THEN EXECUTE 'DROP TABLE public.tipo_usuario_aud CASCADE';
END IF;
-- Remover colunas pode_ser_excluido e eh_dono_restaurante da tabela usuario se existirem
IF EXISTS (
  SELECT 1
  FROM information_schema.columns
  WHERE table_schema = 'public'
    AND table_name = 'usuario'
    AND column_name = 'pode_ser_excluido'
) THEN EXECUTE 'ALTER TABLE public.usuario DROP COLUMN pode_ser_excluido';
END IF;
IF EXISTS (
  SELECT 1
  FROM information_schema.columns
  WHERE table_schema = 'public'
    AND table_name = 'usuario'
    AND column_name = 'eh_dono_restaurante'
) THEN EXECUTE 'ALTER TABLE public.usuario DROP COLUMN eh_dono_restaurante';
END IF;
IF EXISTS (
  SELECT 1
  FROM information_schema.columns
  WHERE table_schema = 'public'
    AND table_name = 'usuario_aud'
    AND column_name = 'pode_ser_excluido'
) THEN EXECUTE 'ALTER TABLE public.usuario_aud DROP COLUMN pode_ser_excluido';
END IF;
IF EXISTS (
  SELECT 1
  FROM information_schema.columns
  WHERE table_schema = 'public'
    AND table_name = 'usuario_aud'
    AND column_name = 'eh_dono_restaurante'
) THEN EXECUTE 'ALTER TABLE public.usuario_aud DROP COLUMN eh_dono_restaurante';
END IF;
-- Alterar tipo_usuario_id para tipo_usuario VARCHAR(1)
IF EXISTS (
  SELECT 1
  FROM information_schema.columns
  WHERE table_schema = 'public'
    AND table_name = 'usuario'
    AND column_name = 'tipo_usuario_id'
) THEN EXECUTE 'ALTER TABLE public.usuario RENAME COLUMN tipo_usuario_id TO tipo_usuario';
EXECUTE 'ALTER TABLE public.usuario ALTER COLUMN tipo_usuario TYPE VARCHAR(1)';
END IF;
IF EXISTS (
  SELECT 1
  FROM information_schema.columns
  WHERE table_schema = 'public'
    AND table_name = 'usuario_aud'
    AND column_name = 'tipo_usuario_id'
) THEN EXECUTE 'ALTER TABLE public.usuario_aud RENAME COLUMN tipo_usuario_id TO tipo_usuario';
EXECUTE 'ALTER TABLE public.usuario_aud ALTER COLUMN tipo_usuario TYPE VARCHAR(1)';
END IF;
END $$;