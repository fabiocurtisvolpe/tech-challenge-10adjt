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

        final Usuario usuario = this.usuarioRepository.obterPorId(id)
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO));

        final Usuario usrLogado = this.usuarioRepository.obterPorEmail(usuarioLogado)
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.CARDAPIO_NAO_ENCONTRADO));

        if ((usrLogado.getTipoUsuario() instanceof TipoUsuarioDonoRestaurante)
                || (usuario.getId().equals(id))) {
            ;

            return usuarioRepository.atualizar(UsuarioFactory.usuario(usuario.getId(), usuario.getNome(),
                    usuario.getEmail(), usuario.getSenha(),
                    usuario.getTipoUsuario(), usuario.getEnderecos(), ativar));
        }

        throw new NotificacaoException(MensagemUtil.USUARIO_NAO_PERMITE_OPERACAO);
    }
}
