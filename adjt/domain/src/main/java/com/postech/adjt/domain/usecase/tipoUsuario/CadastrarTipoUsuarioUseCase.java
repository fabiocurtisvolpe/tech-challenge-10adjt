package com.postech.adjt.domain.usecase.tipoUsuario;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.TipoUsuarioFactory;
import com.postech.adjt.domain.ports.TipoUsuarioRepositoryPort;
import com.postech.adjt.domain.validators.TipoUsuarioValidator;

public class CadastrarTipoUsuarioUseCase {

    private final TipoUsuarioRepositoryPort tipoUsuarioRepository;

    private CadastrarTipoUsuarioUseCase(TipoUsuarioRepositoryPort tipoUsuarioRepository) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    public static CadastrarTipoUsuarioUseCase create(TipoUsuarioRepositoryPort tipoUsuarioRepository) {
        return new CadastrarTipoUsuarioUseCase(tipoUsuarioRepository);
    }

    public TipoUsuario run(TipoUsuario dto) {
        

        final TipoUsuario tipoUsuarioExistente = this.tipoUsuarioRepository.obterPorId(dto.getId()).orElse(null);
        
        if (tipoUsuarioExistente != null) {
            throw new NotificacaoException(MensagemUtil.TIPO_USUARIO_NAO_ENCONTRADO);
        }

        final TipoUsuario tipoUsuario = TipoUsuarioFactory.criar(dto.getNome(), dto.getDescricao());
        
        TipoUsuarioValidator.validar(tipoUsuario);

        return tipoUsuarioRepository.criar(tipoUsuario);
    }
 
}
