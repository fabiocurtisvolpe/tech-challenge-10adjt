package com.postech.adjt.specification;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.postech.adjt.dto.filtro.FiltroCampoDTO;
import com.postech.adjt.dto.filtro.FiltroGenericoDTO;
import com.postech.adjt.enums.FiltroOperadorEnum;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@ExtendWith(MockitoExtension.class)
class SpecificationGenericoTest {

    @Mock
    private Root<Object> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Path<String> pathString;

    @Mock
    private Path<Number> pathNumber;

    @Mock
    private Path<Boolean> pathBoolean;

    @Mock
    private Path<LocalDate> pathDate;

    private FiltroGenericoDTO filtroDTO;

    @BeforeEach
    void setUp() {
        filtroDTO = new FiltroGenericoDTO();
        filtroDTO.setPagina(0);
        filtroDTO.setTamanho(10);
    }

    @Test
    void testCriarSpecificationComFiltroString() {
        // Arrange
        FiltroCampoDTO filtro = new FiltroCampoDTO("nome", "like", "João", "string");
        List<FiltroCampoDTO> filtros = new ArrayList<>();
        filtros.add(filtro);
        filtroDTO.setFiltros(filtros);

        when(root.<String>get("nome")).thenReturn(pathString);
        when(criteriaBuilder.lower(any())).thenReturn(pathString);
        when(criteriaBuilder.like(any(), anyString())).thenReturn(mock(Predicate.class));

        // Act
        Specification<Object> spec = SpecificationGenerico.criarSpecification(filtroDTO);
        spec.toPredicate(root, query, criteriaBuilder);

        // Assert
        verify(criteriaBuilder).like(any(), eq("%joão%"));
    }

    @Test
    void testCriarSpecificationComFiltroNumerico() {
        // Arrange
        FiltroCampoDTO filtro = new FiltroCampoDTO("idade", "gt", "18", "number");
        List<FiltroCampoDTO> filtros = new ArrayList<>();
        filtros.add(filtro);
        filtroDTO.setFiltros(filtros);

        when(root.<Number>get("idade")).thenReturn(pathNumber);
        when(criteriaBuilder.gt(any(), anyInt())).thenReturn(mock(Predicate.class));

        // Act
        Specification<Object> spec = SpecificationGenerico.criarSpecification(filtroDTO);
        spec.toPredicate(root, query, criteriaBuilder);

        // Assert
        verify(criteriaBuilder).gt(any(), eq(18));
    }

    @Test
    void testCriarSpecificationComFiltroBooleano() {
        // Arrange
        FiltroCampoDTO filtro = new FiltroCampoDTO("ativo", "eq", "true", "boolean");
        List<FiltroCampoDTO> filtros = new ArrayList<>();
        filtros.add(filtro);
        filtroDTO.setFiltros(filtros);

        when(root.<Boolean>get("ativo")).thenReturn(pathBoolean);
        when(criteriaBuilder.equal(any(), anyBoolean())).thenReturn(mock(Predicate.class));

        // Act
        Specification<Object> spec = SpecificationGenerico.criarSpecification(filtroDTO);
        spec.toPredicate(root, query, criteriaBuilder);

        // Assert
        verify(criteriaBuilder).equal(any(), eq(true));
    }

    @Test
    void testCriarSpecificationComFiltroData() {
        // Arrange
        FiltroCampoDTO filtro = new FiltroCampoDTO(
                "dataNascimento",
                "bt",
                "2000-01-01,2000-12-31",
                "date");
        List<FiltroCampoDTO> filtros = new ArrayList<>();
        filtros.add(filtro);
        filtroDTO.setFiltros(filtros);

        when(root.<LocalDate>get("dataNascimento")).thenReturn(pathDate);
        when(criteriaBuilder.between(any(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(mock(Predicate.class));

        // Act
        Specification<Object> spec = SpecificationGenerico.criarSpecification(filtroDTO);
        spec.toPredicate(root, query, criteriaBuilder);

        // Assert
        verify(criteriaBuilder).between(
                any(),
                eq(LocalDate.parse("2000-01-01")),
                eq(LocalDate.parse("2000-12-31")));
    }

    @Test
    void testCriarPageable() {
        // Arrange
        filtroDTO.setPagina(2);
        filtroDTO.setTamanho(15);

        // Act
        Pageable pageable = SpecificationGenerico.criarPageable(filtroDTO);

        // Assert
        assertEquals(2, pageable.getPageNumber());
        assertEquals(15, pageable.getPageSize());
    }

    @Test
    void testFiltroComOperadorInvalido() {
        // Arrange
        FiltroCampoDTO filtro = new FiltroCampoDTO("nome", "INVALID", "test", "string");
        List<FiltroCampoDTO> filtros = new ArrayList<>();
        filtros.add(filtro);
        filtroDTO.setFiltros(filtros);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            Specification<Object> spec = SpecificationGenerico.criarSpecification(filtroDTO);
            spec.toPredicate(root, query, criteriaBuilder);
        });
    }
}