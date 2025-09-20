package com.postech.adjt.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FiltroOperadorEnumTest {

    @Test
    void testFromStringValidOperators() {
        assertEquals(FiltroOperadorEnum.EQUALS, FiltroOperadorEnum.fromString("eq"));
        assertEquals(FiltroOperadorEnum.NOT_EQUALS, FiltroOperadorEnum.fromString("ne"));
        assertEquals(FiltroOperadorEnum.LIKE, FiltroOperadorEnum.fromString("like"));
        assertEquals(FiltroOperadorEnum.GREATER_THAN, FiltroOperadorEnum.fromString("gt"));
        assertEquals(FiltroOperadorEnum.LESS_THAN, FiltroOperadorEnum.fromString("lt"));
        assertEquals(FiltroOperadorEnum.GREATER_EQUAL, FiltroOperadorEnum.fromString("ge"));
        assertEquals(FiltroOperadorEnum.LESS_EQUAL, FiltroOperadorEnum.fromString("le"));
        assertEquals(FiltroOperadorEnum.BETWEEN, FiltroOperadorEnum.fromString("bt"));
    }

    @Test
    void testFromStringCaseInsensitive() {
        assertEquals(FiltroOperadorEnum.EQUALS, FiltroOperadorEnum.fromString("EQ"));
        assertEquals(FiltroOperadorEnum.LIKE, FiltroOperadorEnum.fromString("LiKe"));
    }

    @Test
    void testFromStringInvalidOperator() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            FiltroOperadorEnum.fromString("invalid");
        });
        assertTrue(exception.getMessage().contains("Operador inválido"));
    }
}