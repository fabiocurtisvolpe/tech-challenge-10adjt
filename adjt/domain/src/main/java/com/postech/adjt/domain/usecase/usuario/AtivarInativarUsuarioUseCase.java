package com.postech.adjt.domain.usecase.usuario;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.TipoUsuarioDonoRestaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.UsuarioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

public class AtivarInativarUsuarioUseCase {

    private final GenericRepositoryPort<Usuario> usuarioRepository;

    private AtivarInativarUsuarioUseCase(GenericRepositoryPort<Usuario> usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public static AtivarInativarUsuarioUseCase create(GenericRepositoryPort<Usuario> usuarioRepository) {
        return new AtivarInativarUsuarioUseCase(usuarioRepository);
    }

    public Usuario run(Boolean ativar, Integer id, String usuarioLogado) {

        final Usuario usuarioExistente = this.usuarioRepository.obterPorId(id).orElse(null);

        if (usuarioExistente == null) {
            throw new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO);
        }

        final Usuario usrLogado = this.usuarioRepository.obterPorEmail(usuarioLogado).orElse(null);

        if ((usrLogado.getTipoUsuario() instanceof TipoUsuarioDonoRestaurante)
                || (usuarioExistente.getId().equals(id))) {

            final Usuario usuario = UsuarioFactory.usuario(usuarioExistente.getId(), usuarioExistente.getNome(),
                    usuarioExistente.getEmail(), usuarioExistente.getSenha(),
                    usuarioExistente.getTipoUsuario(), usuarioExistente.getEnderecos(), ativar);

            return usuarioRepository.atualizar(usuario);
        }

        throw new NotificacaoException(MensagemUtil.USUARIO_NAO_PERMITE_OPERACAO);
    }
}
