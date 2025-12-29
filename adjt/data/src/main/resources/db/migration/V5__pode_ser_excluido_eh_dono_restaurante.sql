DO $$
DECLARE tbl TEXT;
BEGIN -- Adiciona coluna pode_ser_excluido com default true
FOR tbl IN
SELECT unnest(
    ARRAY ['tipo_usuario', 'tipo_usuario_aud', 'usuario', 'usuario_aud']
  ) LOOP IF NOT EXISTS (
    SELECT 1
    FROM information_schema.columns
    WHERE table_schema = 'public'
      AND table_name = tbl
      AND column_name = 'pode_ser_excluido'
  ) THEN EXECUTE format(
    'ALTER TABLE public.%I ADD COLUMN pode_ser_excluido BOOLEAN DEFAULT true;',
    tbl
  );
END IF;
END LOOP;
-- Adiciona coluna eh_dono_restaurante com default false
FOR tbl IN
SELECT unnest(ARRAY ['usuario', 'usuario_aud']) LOOP IF NOT EXISTS (
    SELECT 1
    FROM information_schema.columns
    WHERE table_schema = 'public'
      AND table_name = tbl
      AND column_name = 'eh_dono_restaurante'
  ) THEN EXECUTE format(
    'ALTER TABLE public.%I ADD COLUMN eh_dono_restaurante BOOLEAN DEFAULT false;',
    tbl
  );
END IF;
END LOOP;
-- Atualiza pode_ser_excluido para false nos registros específicos
UPDATE public.tipo_usuario
SET pode_ser_excluido = false
WHERE nome IN ('cliente', 'dono restaurante');
END $$;