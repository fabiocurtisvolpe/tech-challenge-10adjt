package com.postech.adjt.domain.usecase.usuario;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.TrocarSenhaUsuarioDTO;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.UsuarioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

public class AtualizarSenhaUsuarioUseCase {

    private final GenericRepositoryPort<Usuario> usuarioRepository;

    private AtualizarSenhaUsuarioUseCase(GenericRepositoryPort<Usuario> usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public static AtualizarSenhaUsuarioUseCase create(GenericRepositoryPort<Usuario> usuarioRepository) {
        return new AtualizarSenhaUsuarioUseCase(usuarioRepository);
    }

    public Usuario run(TrocarSenhaUsuarioDTO dto) {

        final Usuario usuario = this.usuarioRepository.obterPorEmail(dto.email())
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO));

        return usuarioRepository.atualizar(UsuarioFactory.usuario(usuario.getId(), usuario.getNome(),
                usuario.getEmail(), dto.senhaCodificada(),
                usuario.getTipoUsuario(), usuario.getEnderecos(), true));
    }
}
