package com.postech.adjt.domain.dto.filtro;

import com.postech.adjt.domain.enums.FiltroOperadorEnum;

import lombok.Getter;

@Getter
public class FilterDTO {

    private final String field;
    private final String value;
    private final FiltroOperadorEnum operador;

    public FilterDTO(String field, String value, FiltroOperadorEnum operador) {
        this.field = field;
        this.value = value;
        this.operador = operador;
    }
}
