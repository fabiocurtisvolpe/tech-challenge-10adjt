DO $$
BEGIN

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_endereco_usuario') THEN
        ALTER TABLE public.endereco 
        ADD CONSTRAINT fk_endereco_usuario 
        FOREIGN KEY (usuario_id) REFERENCES public.usuario(id);
    END IF;

END $$;

CREATE INDEX IF NOT EXISTS idx_endereco_usuario_id ON public.endereco(usuario_id);