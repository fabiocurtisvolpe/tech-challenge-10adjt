package com.postech.adjt.dto.usuario;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioSenhaDTOTest {

    @Test
    void testSetIdWithValidValue() {
        UsuarioSenhaDTO dto = new UsuarioSenhaDTO();
        dto.setId(123);
        assertEquals(123, dto.getId());
    }

    @Test
    void testSetIdWithNullValue() {
        UsuarioSenhaDTO dto = new UsuarioSenhaDTO();
        dto.setId(null);
        assertNull(dto.getId());
    }
}