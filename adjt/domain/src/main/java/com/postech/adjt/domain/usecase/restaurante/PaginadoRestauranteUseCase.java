package com.postech.adjt.domain.usecase.restaurante;

import java.util.List;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

public class PaginadoRestauranteUseCase {

    private final GenericRepositoryPort<Restaurante> restauranteRepository;

    private PaginadoRestauranteUseCase(GenericRepositoryPort<Restaurante> restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    public static PaginadoRestauranteUseCase create(GenericRepositoryPort<Restaurante> restauranteRepository) {
        return new PaginadoRestauranteUseCase(restauranteRepository);
    }

    public ResultadoPaginacaoDTO<Restaurante> run(int page, int size, List<FilterDTO> filters, List<SortDTO> sorts) {
        
        if (page < 0 || size <= 0) {
            throw new NotificacaoException(MensagemUtil.PAGINA_SIZE_INVALIDA);
        }

        return restauranteRepository.listarPaginado(page, size, filters, sorts);
    }
 
}
