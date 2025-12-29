package com.postech.adjt.api.payload;

import java.util.List;

import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaginacaoPayLoad {

    @Min(0)
    private int pagina;

    @Min(5)
    private int qtdPagina;

    private List<FilterDTO> filtros;

    private List<SortDTO> ordenacao;

    public PaginacaoPayLoad() {
    }

}
