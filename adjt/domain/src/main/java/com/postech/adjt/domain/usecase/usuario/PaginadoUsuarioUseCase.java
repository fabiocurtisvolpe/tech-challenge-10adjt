package com.postech.adjt.domain.usecase.usuario;

import java.util.List;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

public class PaginadoUsuarioUseCase {

    private final GenericRepositoryPort<Usuario> usuarioRepository;
    
    private PaginadoUsuarioUseCase(GenericRepositoryPort<Usuario> usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public static PaginadoUsuarioUseCase create(GenericRepositoryPort<Usuario> usuarioRepository) {
        return new PaginadoUsuarioUseCase(usuarioRepository);
    }

    public ResultadoPaginacaoDTO<Usuario> run(int page, int size, List<FilterDTO> filters, List<SortDTO> sorts) {
        
        if (page < 0 || size <= 0) {
            throw new NotificacaoException(MensagemUtil.PAGINA_SIZE_INVALIDA);
        }

        return usuarioRepository.listarPaginado(page, size, filters, sorts);
    }
 
}
