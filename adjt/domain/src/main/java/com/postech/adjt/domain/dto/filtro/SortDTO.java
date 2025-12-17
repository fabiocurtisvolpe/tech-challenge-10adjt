package com.postech.adjt.domain.dto.filtro;

import lombok.Getter;

@Getter
public class SortDTO {

    private final String campo;
    private final Direction ordem;

    public enum Direction { ASC, DESC }

    public SortDTO(String campo, Direction ordem) {
        this.campo = campo;
        this.ordem = ordem;
    }
}
