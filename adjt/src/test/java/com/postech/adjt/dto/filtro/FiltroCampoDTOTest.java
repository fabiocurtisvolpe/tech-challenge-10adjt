package com.postech.adjt.dto.filtro;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FiltroCampoDTOTest {

    @Test
    void testGetTipoReturnsCorrectValue() {
        FiltroCampoDTO dto = new FiltroCampoDTO("campo", "operador", "valor", "tipoTeste");
        assertEquals("tipoTeste", dto.getTipo());
    }

    @Test
    void testGetTipoAfterSetTipo() {
        FiltroCampoDTO dto = new FiltroCampoDTO();
        dto.setTipo("novoTipo");
        assertEquals("novoTipo", dto.getTipo());
    }

    @Test
    void testGetTipoWhenTipoIsNull() {
        FiltroCampoDTO dto = new FiltroCampoDTO();
        assertNull(dto.getTipo());
    }
}