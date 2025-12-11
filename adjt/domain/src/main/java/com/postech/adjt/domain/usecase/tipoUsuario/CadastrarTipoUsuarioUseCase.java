package com.postech.adjt.domain.usecase.tipoUsuario;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.TipoUsuarioDTO;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.TipoUsuarioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import com.postech.adjt.domain.validators.TipoUsuarioValidator;

public class CadastrarTipoUsuarioUseCase {

    private final GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository;

    private CadastrarTipoUsuarioUseCase(GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    public static CadastrarTipoUsuarioUseCase create(GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository) {
        return new CadastrarTipoUsuarioUseCase(tipoUsuarioRepository);
    }

    public TipoUsuario run(TipoUsuarioDTO dto) {
        

        final TipoUsuario tipoUsuarioExistente = this.tipoUsuarioRepository.obterPorNome(dto.nome()).orElse(null);
        
        if (tipoUsuarioExistente != null) {
            throw new NotificacaoException(MensagemUtil.TIPO_USUARIO_JA_CADASTRADO);
        }

        final TipoUsuario tipoUsuario = TipoUsuarioFactory.criar(dto.nome(), dto.descricao(), dto.isDono());
        
        TipoUsuarioValidator.validar(tipoUsuario);

        return tipoUsuarioRepository.criar(tipoUsuario);
    }
 
}
