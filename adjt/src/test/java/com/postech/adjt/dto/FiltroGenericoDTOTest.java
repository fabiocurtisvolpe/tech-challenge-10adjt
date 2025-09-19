package com.postech.adjt.dto;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class FiltroGenericoDTOTest {

    @Test
    void testDefaultValues() {
        FiltroGenericoDTO dto = new FiltroGenericoDTO();
        assertNotNull(dto.getFiltros());
        assertTrue(dto.getFiltros().isEmpty());
        assertEquals(0, dto.getPagina());
        assertEquals(10, dto.getTamanho());
    }

    @Test
    void testSetAndGetFiltros() {
        FiltroGenericoDTO dto = new FiltroGenericoDTO();
        Map<String, String> filtros = new HashMap<>();
        filtros.put("nome:dono", "Joao");
        dto.setFiltros(filtros);
        assertEquals(filtros, dto.getFiltros());
    }

    @Test
    void testSetAndGetPagina() {
        FiltroGenericoDTO dto = new FiltroGenericoDTO();
        dto.setPagina(2);
        assertEquals(2, dto.getPagina());
    }

    @Test
    void testSetAndGetTamanho() {
        FiltroGenericoDTO dto = new FiltroGenericoDTO();
        dto.setTamanho(25);
        assertEquals(25, dto.getTamanho());
    }
}