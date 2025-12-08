package com.postech.adjt.domain.usecase.tipoUsuario;

import java.util.List;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.TipoUsuarioRepositoryPort;

public class PaginadoTipoUsuarioUseCase {

    private final TipoUsuarioRepositoryPort tipoUsuarioRepository;

    private PaginadoTipoUsuarioUseCase(TipoUsuarioRepositoryPort tipoUsuarioRepository) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    public static PaginadoTipoUsuarioUseCase create(TipoUsuarioRepositoryPort tipoUsuarioRepository) {
        return new PaginadoTipoUsuarioUseCase(tipoUsuarioRepository);
    }

    public ResultadoPaginacaoDTO<TipoUsuario> run(int page, int size, List<FilterDTO> filters, List<SortDTO> sorts) {
        
        if (page < 0 || size <= 0) {
            throw new NotificacaoException(MensagemUtil.PAGINA_SIZE_INVALIDA);
        }

        return tipoUsuarioRepository.listarPaginado(page, size, filters, sorts);
    }
 
}
