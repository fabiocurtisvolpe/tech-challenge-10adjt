package com.postech.adjt.specification;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.postech.adjt.dto.FiltroGenericoDTO;
import com.postech.adjt.exception.NotificacaoException;

class SpecificationGenericoTest {

    @Test
    void deveCriarPageableCorretamente() {
        FiltroGenericoDTO dto = new FiltroGenericoDTO();
        dto.setPagina(2);
        dto.setTamanho(10);

        Pageable pageable = SpecificationGenerico.criarPageable(dto);

        assertEquals(2, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
    }

    @Test
    void deveCriarSpecificationComFiltroStringLike() {
        FiltroGenericoDTO dto = new FiltroGenericoDTO();
        dto.setFiltros(Map.of("nome:like:João", "String"));
        dto.setPagina(0);
        dto.setTamanho(10);

        Specification<Object> spec = SpecificationGenerico.criarSpecification(dto);

        assertNotNull(spec);
    }

    @Test
    void deveCriarSpecificationComFiltroNumberGt() {
        FiltroGenericoDTO dto = new FiltroGenericoDTO();
        dto.setFiltros(Map.of("idade:gt:18", "Number"));
        dto.setPagina(0);
        dto.setTamanho(10);

        Specification<Object> spec = SpecificationGenerico.criarSpecification(dto);

        assertNotNull(spec);
    }

    @Test
    void deveCriarSpecificationComFiltroBooleanEq() {
        FiltroGenericoDTO dto = new FiltroGenericoDTO();
        dto.setFiltros(Map.of("ativo:eq:true", "Boolean"));
        dto.setPagina(0);
        dto.setTamanho(10);

        Specification<Object> spec = SpecificationGenerico.criarSpecification(dto);

        assertNotNull(spec);
    }

    @Test
    void deveCriarSpecificationComFiltroDateBetween() {
        FiltroGenericoDTO dto = new FiltroGenericoDTO();
        dto.setFiltros(Map.of("dataCriacao:between:2025-01-01,2025-12-31", "Date"));
        dto.setPagina(0);
        dto.setTamanho(10);

        Specification<Object> spec = SpecificationGenerico.criarSpecification(dto);

        assertNotNull(spec);
    }

    @Test
    void deveLancarExcecaoParaFiltroComFormatoInvalido() {
        FiltroGenericoDTO dto = new FiltroGenericoDTO();
        dto.setFiltros(Map.of("nome:like", "String"));
        dto.setPagina(0);
        dto.setTamanho(5);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            SpecificationGenerico.criarSpecification(dto);
        });

        assertTrue(ex.getMessage().contains("Formato de filtro inválido"));
    }

    @Test
    void deveLancarExcecaoParaOperadorInvalido() {
        FiltroGenericoDTO dto = new FiltroGenericoDTO();
        dto.setFiltros(Map.of("nome:invalid:João", "String"));
        dto.setPagina(0);
        dto.setTamanho(5);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            SpecificationGenerico.criarSpecification(dto);
        });

        assertTrue(ex.getMessage().contains("Operador inválido"));
    }

    @Test
    void deveLancarNotificacaoExceptionParaErroGenerico() {
        FiltroGenericoDTO dto = new FiltroGenericoDTO();
        dto.setFiltros(Map.of("idade:gt:notanumber", "Number")); // valor inválido
        dto.setPagina(0);
        dto.setTamanho(5);

        NotificacaoException ex = assertThrows(NotificacaoException.class, () -> {
            SpecificationGenerico.criarSpecification(dto);
        });

        assertEquals("Não foi possível executar a operação", ex.getMessage());
    }
}
