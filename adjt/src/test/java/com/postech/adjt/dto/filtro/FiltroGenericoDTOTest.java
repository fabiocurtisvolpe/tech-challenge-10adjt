package com.postech.adjt.dto.filtro;

import org.junit.jupiter.api.Test;

import com.postech.adjt.dto.filtro.FiltroCampoDTO;
import com.postech.adjt.dto.filtro.FiltroGenericoDTO;

import java.util.ArrayList;
import java.util.List;
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
        dto.setPagina(0);
        dto.setTamanho(5);
        
        List<FiltroCampoDTO> filtros = new ArrayList<>();
        filtros.add(new FiltroCampoDTO("nome", "like", "Joao", "string"));
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