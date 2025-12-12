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

public class CadastrarUsuarioUseCase {

    private final GenericRepositoryPort<Usuario> usuarioRepository;
    private final GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository;

    private CadastrarUsuarioUseCase(GenericRepositoryPort<Usuario> usuarioRepository,
            GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    public static CadastrarUsuarioUseCase create(GenericRepositoryPort<Usuario> usuarioRepository,
            GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository) {
        return new CadastrarUsuarioUseCase(usuarioRepository, tipoUsuarioRepository);
    }

    public Usuario run(UsuarioDTO dto) {

        final Usuario usuarioExistente = this.usuarioRepository.obterPorEmail(dto.email()).orElse(null);

        if (usuarioExistente != null) {
            throw new NotificacaoException(MensagemUtil.USUARIO_EXISTENTE);
        }

        final TipoUsuario tipoUsuario = this.tipoUsuarioRepository.obterPorId(dto.tipoUsuario().id()).orElse(null);

        if (tipoUsuario == null) {
            throw new NotificacaoException(MensagemUtil.TIPO_USUARIO_NAO_ENCONTRADO);
        }

        List<Endereco> enderecos = EnderecoFactory.toEnderecoList(dto.enderecos());

        final Usuario usuario = UsuarioFactory.novo(dto.nome(),
                dto.email(), dto.senha(),
                tipoUsuario, enderecos);

        return usuarioRepository.criar(usuario);
    }

}
