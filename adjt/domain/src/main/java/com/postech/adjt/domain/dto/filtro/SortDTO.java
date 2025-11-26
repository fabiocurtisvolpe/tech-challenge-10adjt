package com.postech.adjt.domain.dto.filtro;

public class SortDTO {

    private final String field;
    private final Direction direction;

    public enum Direction { ASC, DESC }

    public SortDTO(String field, Direction direction) {
        this.field = field;
        this.direction = direction;
    }

    public String getField() {
        return field;
    }

    public Direction getDirection() {
        return direction;
    }
}
