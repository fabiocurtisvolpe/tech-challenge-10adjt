package com.postech.adjt.domain.enums;

public enum FiltroOperadorEnum {
    EQUALS("eq"),
    NOT_EQUALS("ne"),
    LIKE("like"),
    GREATER_THAN("gt"),
    LESS_THAN("lt"),
    GREATER_EQUAL("ge"),
    LESS_EQUAL("le"),
    BETWEEN("bt");

    private final String operator;

    FiltroOperadorEnum(String operator) {
        this.operator = operator;
    }

    public static FiltroOperadorEnum fromString(String operator) {
        for (FiltroOperadorEnum fo : FiltroOperadorEnum.values()) {
            if (fo.operator.equalsIgnoreCase(operator)) {
                return fo;
            }
        }
        throw new IllegalArgumentException("Filtro Operador Inválido: " + operator);
    }
}
