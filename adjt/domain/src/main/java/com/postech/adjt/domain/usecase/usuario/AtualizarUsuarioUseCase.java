package com.postech.adjt.domain.usecase.usuario;

import java.util.List;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.UsuarioDTO;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.EnderecoFactory;
import com.postech.adjt.domain.factory.UsuarioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

public class AtualizarUsuarioUseCase {

    private final GenericRepositoryPort<Usuario> usuarioRepository;
    private final GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository;

    private AtualizarUsuarioUseCase(GenericRepositoryPort<Usuario> usuarioRepository,
            GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    public static AtualizarUsuarioUseCase create(GenericRepositoryPort<Usuario> usuarioRepository,
            GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository) {
        return new AtualizarUsuarioUseCase(usuarioRepository, tipoUsuarioRepository);
    }

    public Usuario run(UsuarioDTO dto, String usuarioLogado) {

        final Usuario usuarioExistente = this.usuarioRepository.obterPorEmail(usuarioLogado)
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO));

        final TipoUsuario tipoUsuario = this.tipoUsuarioRepository.obterPorId(dto.tipoUsuario().id())
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.TIPO_USUARIO_NAO_ENCONTRADO));

        if (!dto.email().equals(usuarioLogado)) {
            this.usuarioRepository.obterPorEmail(dto.email())
                    .orElseThrow(() -> new NotificacaoException(MensagemUtil.USUARIO_EXISTENTE));
        }

        List<Endereco> enderecos = EnderecoFactory.toEnderecoList(dto.enderecos());

        final Usuario usuario = UsuarioFactory.usuario(usuarioExistente.getId(), dto.nome(),
                dto.email(), usuarioExistente.getSenha(),
                tipoUsuario, enderecos, dto.ativo());

        return usuarioRepository.atualizar(usuario);
    }
}
