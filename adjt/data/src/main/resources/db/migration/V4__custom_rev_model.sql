DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM information_schema.tables
    WHERE table_schema = 'public'
      AND table_name = 'custom_rev_model'
  ) THEN

  CREATE TABLE public.custom_rev_model (
    rev SERIAL PRIMARY KEY,
    revtstmp TIMESTAMP
  );

  END IF;
END $$;