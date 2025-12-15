package com.postech.adjt.domain.usecase.tipoUsuario;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.TipoUsuarioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

public class AtivarInativarTipoUsuarioUseCase {

    private final GenericRepositoryPort<TipoUsuario> repositoryPort;
    private final GenericRepositoryPort<Usuario> usuarioRepository;

    private AtivarInativarTipoUsuarioUseCase(GenericRepositoryPort<TipoUsuario> repositoryPort,
            GenericRepositoryPort<Usuario> usuarioRepository) {
        this.repositoryPort = repositoryPort;
        this.usuarioRepository = usuarioRepository;
    }

    public static AtivarInativarTipoUsuarioUseCase create(GenericRepositoryPort<TipoUsuario> repositoryPort,
            GenericRepositoryPort<Usuario> usuarioRepository) {
        return new AtivarInativarTipoUsuarioUseCase(repositoryPort, usuarioRepository);
    }

    public TipoUsuario run(Boolean ativar, Integer id, String usuarioLogado) {

        final TipoUsuario tipoUsuario = this.repositoryPort.obterPorId(id).orElse(null);

        if (tipoUsuario == null) {
            throw new NotificacaoException(MensagemUtil.TIPO_USUARIO_NAO_ENCONTRADO);
        }

        final Usuario usrLogado = this.usuarioRepository.obterPorEmail(usuarioLogado).orElse(null);
        if (usrLogado == null) {
            throw new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO);
        }

        return repositoryPort.atualizar(TipoUsuarioFactory.tipoUsuario(tipoUsuario.getId(),
                tipoUsuario.getNome(), tipoUsuario.getDescricao(),
                ativar, tipoUsuario.getIsDono(),
                tipoUsuario.getRestaurante(), usrLogado.getId()));
    }
}
