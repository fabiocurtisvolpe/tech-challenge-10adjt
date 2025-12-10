package com.postech.adjt.domain.usecase.usuario;

import java.util.Optional;

import com.postech.adjt.domain.constants.MensagemUtil;
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

    public Optional<Usuario> run(Integer id) {

        if (id == null || id <= 0) {
            throw new NotificacaoException(MensagemUtil.ID_NULO);
        }
        
        Optional<Usuario> usuarioExistente = this.usuarioRepository.obterPorId(id);
        
        if (usuarioExistente.isEmpty()) {
            throw new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO);
        }

        return usuarioExistente;
    }
 
}
