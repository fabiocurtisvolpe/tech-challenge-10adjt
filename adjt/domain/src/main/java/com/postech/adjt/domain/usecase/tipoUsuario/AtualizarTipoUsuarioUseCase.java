package com.postech.adjt.domain.usecase.tipoUsuario;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.TipoUsuarioDTO;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.TipoUsuarioFactory;
import com.postech.adjt.domain.ports.TipoUsuarioRepositoryPort;
import com.postech.adjt.domain.validators.TipoUsuarioValidator;

public class AtualizarTipoUsuarioUseCase {

    private final TipoUsuarioRepositoryPort tipoUsuarioRepository;

    private AtualizarTipoUsuarioUseCase(TipoUsuarioRepositoryPort tipoUsuarioRepository) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    public static AtualizarTipoUsuarioUseCase create(TipoUsuarioRepositoryPort tipoUsuarioRepository) {
        return new AtualizarTipoUsuarioUseCase(tipoUsuarioRepository);
    }

    public TipoUsuario run(TipoUsuarioDTO dto) {
        
        final TipoUsuario tipoUsuarioExistente = this.tipoUsuarioRepository.obterPorId(dto.id()).orElse(null);
        
        if (tipoUsuarioExistente == null) {
            throw new NotificacaoException(MensagemUtil.TIPO_USUARIO_NAO_ENCONTRADO);
        }

        final TipoUsuario tipoUsuario = TipoUsuarioFactory.atualizar(tipoUsuarioExistente.getId(), tipoUsuarioExistente.getNome(), 
        tipoUsuarioExistente.getDescricao(), true);

        TipoUsuarioValidator.validar(tipoUsuario);

        return tipoUsuarioRepository.atualizar(tipoUsuario);
    }
 
}
