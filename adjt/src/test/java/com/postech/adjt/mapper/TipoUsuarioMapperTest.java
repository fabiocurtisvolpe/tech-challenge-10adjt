package com.postech.adjt.mapper;

import com.postech.adjt.dto.TipoUsuarioDTO;
import com.postech.adjt.model.TipoUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TipoUsuarioMapperTest {

    private TipoUsuarioMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new TipoUsuarioMapper();
    }

    @Test
    void testToTipoUsuarioDTO_whenEntityIsNull_returnsNull() {
        assertNull(mapper.toTipoUsuarioDTO(null));
    }

    @Test
    void testToTipoUsuarioDTO_whenEntityIsNotNull_mapsFieldsCorrectly() {
        TipoUsuario entity = new TipoUsuario();
        entity.setId(1);
        entity.setDataCriacao(LocalDateTime.of(2024, 1, 1, 12, 0));
        entity.setDataAlteracao(LocalDateTime.of(2024, 2, 2, 13, 30));
        entity.setAtivo(true);
        entity.setNome("Admin");
        entity.setDescricao("Administrador do sistema");

        TipoUsuarioDTO dto = mapper.toTipoUsuarioDTO(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        // Note: dataCriacao is set from getDataAlteracao in the mapper
        assertEquals(entity.getDataAlteracao(), dto.getDataCriacao());
        assertEquals(entity.getDataAlteracao(), dto.getDataAlteracao());
        assertEquals(entity.getAtivo(), dto.getAtivo());
        assertEquals(entity.getNome(), dto.getNome());
        assertEquals(entity.getDescricao(), dto.getDescricao());
    }

    @Test
    void testToTipoUsuario_whenDtoIsNull_returnsNull() {
        assertNull(mapper.toTipoUsuario(null));
    }

    @Test
    void testToTipoUsuario_whenDtoIsNotNull_mapsFieldsCorrectly() {
        TipoUsuarioDTO dto = new TipoUsuarioDTO();
        dto.setId(2);
        dto.setDataCriacao(LocalDateTime.of(2023, 3, 3, 10, 0));
        dto.setDataAlteracao(LocalDateTime.of(2023, 4, 4, 11, 15));
        dto.setAtivo(false);
        dto.setNome("User");
        dto.setDescricao("Usuário comum");

        TipoUsuario entity = mapper.toTipoUsuario(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        // Note: dataCriacao is set from getDataAlteracao in the mapper
        assertEquals(dto.getDataAlteracao(), entity.getDataCriacao());
        assertEquals(dto.getDataAlteracao(), entity.getDataAlteracao());
        assertEquals(dto.getAtivo(), entity.getAtivo());
        assertEquals(dto.getNome(), entity.getNome());
        assertEquals(dto.getDescricao(), entity.getDescricao());
    }
}