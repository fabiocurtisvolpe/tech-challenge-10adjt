package com.postech.adjt.domain.usecase.usuario;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.TipoUsuarioDonoRestaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

public class ObterUsuarioPorIdUseCase {

    private final GenericRepositoryPort<Usuario> usuarioRepository;

    private ObterUsuarioPorIdUseCase(GenericRepositoryPort<Usuario> usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public static ObterUsuarioPorIdUseCase create(GenericRepositoryPort<Usuario> usuarioRepository) {
        return new ObterUsuarioPorIdUseCase(usuarioRepository);
    }

    public Usuario run(Integer id, String usuarioLogado) {

        if (id == null || id <= 0) {
            throw new NotificacaoException(MensagemUtil.ID_NULO);
        }

        Usuario usuarioDono = this.usuarioRepository.obterPorEmail(usuarioLogado)
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO));
        ;
        if (!(usuarioDono.getTipoUsuario() instanceof TipoUsuarioDonoRestaurante)) {
            throw new NotificacaoException(MensagemUtil.USUARIO_NAO_PERMITE_OPERACAO);
        }

        Usuario usuario = this.usuarioRepository.obterPorId(id)
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO));
        ;

        return usuario;
    }
}
