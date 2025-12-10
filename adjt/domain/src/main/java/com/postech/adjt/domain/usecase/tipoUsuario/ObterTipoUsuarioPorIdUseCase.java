package com.postech.adjt.domain.usecase.tipoUsuario;

import java.util.Optional;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

public class ObterTipoUsuarioPorIdUseCase {

    private final GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository;

    private ObterTipoUsuarioPorIdUseCase(GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    public static ObterTipoUsuarioPorIdUseCase create(GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository) {
        return new ObterTipoUsuarioPorIdUseCase(tipoUsuarioRepository);
    }

    public Optional<TipoUsuario> run(Integer id) {

        if (id == null || id <= 0) {
            throw new NotificacaoException(MensagemUtil.ID_NULO);
        }
        
        Optional<TipoUsuario> tipoUsuarioExistente = this.tipoUsuarioRepository.obterPorId(id);
        
        if (tipoUsuarioExistente.isEmpty()) {
            throw new NotificacaoException(MensagemUtil.TIPO_USUARIO_NAO_ENCONTRADO);
        }

        return tipoUsuarioExistente;
    }
 
}
