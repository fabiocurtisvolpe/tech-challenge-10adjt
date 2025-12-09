package com.postech.adjt.domain.usecase.cardapio;

import java.util.List;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;
import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.CardapioRepositoryPort;

public class PaginadoCardapioUseCase {

    private final CardapioRepositoryPort cardapioRepository;

    private PaginadoCardapioUseCase(CardapioRepositoryPort cardapioRepository) {
        this.cardapioRepository = cardapioRepository;
    }

    public static PaginadoCardapioUseCase create(CardapioRepositoryPort cardapioRepository) {
        return new PaginadoCardapioUseCase(cardapioRepository);
    }

    public ResultadoPaginacaoDTO<Cardapio> run(int page, int size, List<FilterDTO> filters, List<SortDTO> sorts) {
        
        if (page < 0 || size <= 0) {
            throw new NotificacaoException(MensagemUtil.PAGINA_SIZE_INVALIDA);
        }

        return cardapioRepository.listarPaginado(page, size, filters, sorts);
    }
 
}
