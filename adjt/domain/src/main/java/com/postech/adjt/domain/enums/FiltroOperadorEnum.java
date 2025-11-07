package com.postech.adjt.domain.enums;

/**
 * Enumeração dos operadores suportados para filtros dinâmicos.
 * 
 * Suporta os seguintes operadores:
 * - EQUALS (eq): Igualdade
 * - NOT_EQUALS (ne): Desigualdade
 * - LIKE (like): Similaridade (contém)
 * - GREATER_THAN (gt): Maior que
 * - LESS_THAN (lt): Menor que
 * - GREATER_EQUAL (ge): Maior ou igual a
 * - LESS_EQUAL (le): Menor ou igual a
 * - BETWEEN (bt): Entre dois valores (inclusive)
 * 
 * @author Fabio
 * @since 2025-09-19
 */
public enum FiltroOperadorEnum {
    EQUALS("eq"),
    NOT_EQUALS("ne"),
    LIKE("like"),
    GREATER_THAN("gt"),
    LESS_THAN("lt"),
    GREATER_EQUAL("ge"),
    LESS_EQUAL("le"),
    BETWEEN("bt");

    private String operator;

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
