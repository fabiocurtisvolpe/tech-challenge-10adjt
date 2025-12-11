package com.postech.adjt.domain.usecase.tipoUsuario;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.TipoUsuarioDTO;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.TipoUsuarioDonoRestaurante;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.TipoUsuarioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import com.postech.adjt.domain.validators.TipoUsuarioValidator;

public class AtualizarTipoUsuarioUseCase {

    private final GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository;

    private AtualizarTipoUsuarioUseCase(GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    public static AtualizarTipoUsuarioUseCase create(GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository) {
        return new AtualizarTipoUsuarioUseCase(tipoUsuarioRepository);
    }

    public TipoUsuario run(TipoUsuarioDTO dto) {

        final TipoUsuario tipoUsuarioExistente = this.tipoUsuarioRepository.obterPorId(dto.id()).orElse(null);

        if (tipoUsuarioExistente == null) {
            throw new NotificacaoException(MensagemUtil.TIPO_USUARIO_NAO_ENCONTRADO);
        }

        Boolean isDono = tipoUsuarioExistente instanceof TipoUsuarioDonoRestaurante ? true : false;
        if (dto.isDono() != null) {
            isDono = dto.isDono();
        }

        final TipoUsuario tipoUsuario = TipoUsuarioFactory.tipoUsuario(tipoUsuarioExistente.getId(),
                tipoUsuarioExistente.getNome(),
                tipoUsuarioExistente.getDescricao(), dto.ativo(), isDono);

        TipoUsuarioValidator.validar(tipoUsuario);

        return tipoUsuarioRepository.atualizar(tipoUsuario);
    }

}
