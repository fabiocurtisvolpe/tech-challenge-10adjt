package com.postech.adjt.dto;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ResultadoPaginacaoDTOTest {

    @Test
    void testConstructorAndGettersWithNonEmptyList() {
        List<String> items = Arrays.asList("item1", "item2");
        int quantidade = 2;
        ResultadoPaginacaoDTO<String> dto = new ResultadoPaginacaoDTO<>(items, quantidade);

        assertEquals(items, dto.getResultado());
        assertEquals(quantidade, dto.getQuantidade());
    }

    @Test
    void testConstructorAndGettersWithEmptyList() {
        List<String> items = Collections.emptyList();
        int quantidade = 0;
        ResultadoPaginacaoDTO<String> dto = new ResultadoPaginacaoDTO<>(items, quantidade);

        assertEquals(items, dto.getResultado());
        assertEquals(quantidade, dto.getQuantidade());
    }

    @Test
    void testConstructorWithNullList() {
        int quantidade = 0;
        ResultadoPaginacaoDTO<String> dto = new ResultadoPaginacaoDTO<>(null, quantidade);

        assertNull(dto.getResultado());
        assertEquals(quantidade, dto.getQuantidade());
    }
}