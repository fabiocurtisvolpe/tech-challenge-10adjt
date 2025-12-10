package com.postech.adjt.domain.usecase.tipoUsuario;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.TipoUsuarioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

public class AtivarInativarTipoUsuarioUseCase {

    private final GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository;
    
    private AtivarInativarTipoUsuarioUseCase(GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    public static AtivarInativarTipoUsuarioUseCase create(GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository) {
        return new AtivarInativarTipoUsuarioUseCase(tipoUsuarioRepository);
    }

    public TipoUsuario run(Integer id, Boolean ativo) throws IllegalArgumentException {

        final TipoUsuario tipoUsuarioExistente = this.tipoUsuarioRepository.obterPorId(id).orElse(null);

        if (tipoUsuarioExistente == null) {
            throw new NotificacaoException(MensagemUtil.TIPO_USUARIO_NAO_ENCONTRADO);
        }

        final TipoUsuario novoTipoUsuario = TipoUsuarioFactory.atualizar(tipoUsuarioExistente.getId(),
                tipoUsuarioExistente.getNome(), tipoUsuarioExistente.getDescricao(), ativo);

        return tipoUsuarioRepository.atualizar(novoTipoUsuario);
    }
}
