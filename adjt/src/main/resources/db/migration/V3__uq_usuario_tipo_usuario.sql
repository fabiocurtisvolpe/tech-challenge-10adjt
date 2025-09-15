DO $$
BEGIN
  IF EXISTS (
    SELECT 1
    FROM information_schema.tables
    WHERE table_schema = 'public'
      AND table_name = 'usuario'
  ) THEN

   	CREATE UNIQUE INDEX uq_usuario_email_ativo
	ON public.usuario (e_mail)
	WHERE ativo = true;

  END IF;
END $$;

DO $$
BEGIN
  IF EXISTS (
    SELECT 1
    FROM information_schema.tables
    WHERE table_schema = 'public'
      AND table_name = 'tipo_usuario'
  ) THEN

   	CREATE UNIQUE INDEX uq_tipo_usuario_nome_ativo
	ON public.tipo_usuario (nome)
	WHERE ativo = true;

  END IF;
END $$;