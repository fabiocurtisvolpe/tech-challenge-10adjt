package com.postech.adjt.domain.usecase.tipoUsuario;

import java.util.Optional;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

public class ObterTipoUsuarioPorIdUseCase {

    private final GenericRepositoryPort<TipoUsuario> repositoryPort;
    private final GenericRepositoryPort<Usuario> usuarioRepository;

    private ObterTipoUsuarioPorIdUseCase(GenericRepositoryPort<TipoUsuario> repositoryPort,
            GenericRepositoryPort<Usuario> usuarioRepository) {
        this.repositoryPort = repositoryPort;
        this.usuarioRepository = usuarioRepository;
    }

    public static ObterTipoUsuarioPorIdUseCase create(GenericRepositoryPort<TipoUsuario> repositoryPort,
            GenericRepositoryPort<Usuario> usuarioRepository) {
        return new ObterTipoUsuarioPorIdUseCase(repositoryPort, usuarioRepository);
    }

    public Optional<TipoUsuario> run(Integer id, String usuarioLogado) {

        if (id == null || id <= 0) {
            throw new NotificacaoException(MensagemUtil.ID_NULO);
        }

        Optional<TipoUsuario> tipoUsuario = this.repositoryPort.obterPorId(id);

        if (tipoUsuario.isEmpty()) {
            throw new NotificacaoException(MensagemUtil.TIPO_USUARIO_NAO_ENCONTRADO);
        }

         final Usuario usrLogado = this.usuarioRepository.obterPorEmail(usuarioLogado).orElse(null);
        if (usrLogado == null) {
            throw new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO);
        }

        if (!tipoUsuario.get().getRestaurante().getDono().getId().equals(usrLogado.getId())) {
            throw new NotificacaoException(MensagemUtil.USUARIO_NAO_PERMITE_OPERACAO);
        }

        return tipoUsuario;
    }

}
