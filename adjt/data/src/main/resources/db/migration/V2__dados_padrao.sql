DO $$
BEGIN
  IF EXISTS (
    SELECT 1
    FROM information_schema.tables
    WHERE table_schema = 'public'
      AND table_name = 'tipo_usuario'
  ) THEN

    IF NOT EXISTS (
      SELECT 1
      FROM public.tipo_usuario
      WHERE nome = 'cliente'
    ) THEN

      INSERT INTO public.tipo_usuario (
        dt_alteracao,
        dt_criacao,
        nome,
        descricao,
        ativo
      ) VALUES (
        NOW(),
        NOW(),
        'cliente',
        'usuário do tipo cliente',
        TRUE
      );

    END IF;

	IF NOT EXISTS (
      SELECT 1
      FROM public.tipo_usuario
      WHERE nome = 'dono restaurante'
    ) THEN

      INSERT INTO public.tipo_usuario (
        dt_alteracao,
        dt_criacao,
        nome,
        descricao,
        ativo
      ) VALUES (
        NOW(),
        NOW(),
        'dono restaurante',
        'usuário do tipo dono de restaurante',
        TRUE
      );

    END IF;

  END IF;
END $$;