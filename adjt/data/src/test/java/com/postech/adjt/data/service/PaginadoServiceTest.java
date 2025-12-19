package com.postech.adjt.data.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.postech.adjt.data.mapper.EntityMapper;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;
import com.postech.adjt.domain.enums.FiltroOperadorEnum;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@ExtendWith(MockitoExtension.class)
class PaginadoServiceTest {

    @Mock
    private JpaSpecificationExecutor<TestEntity> repository;

    @Mock
    private EntityMapper<TestEntity, TestDTO> mapper;

    @InjectMocks
    private PaginadoService<TestEntity, TestDTO> service;

    static class TestEntity {
        String nome;
    }

    static class TestDTO {
        String nome;
    }

    @Test
    @DisplayName("Deve listar paginado sem filtros e sem ordenação")
    void listarPaginado_SemFiltrosSemSort() {
        int page = 0;
        int size = 10;
        List<FilterDTO> filters = Collections.emptyList();
        List<SortDTO> sorts = Collections.emptyList();

        TestEntity entity = new TestEntity();
        Page<TestEntity> pageResult = new PageImpl<>(List.of(entity));
        TestDTO dto = new TestDTO();

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageResult);
        when(mapper.toDomain(entity)).thenReturn(dto);

        ResultadoPaginacaoDTO<TestDTO> result = service.listarPaginado(page, size, filters, sorts);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(0, result.getPageNumber());
        assertEquals(1, result.getPageSize());
    }

    @Test
    @DisplayName("Deve aplicar ordenação corretamente")
    void listarPaginado_ComOrdenacao() {
        int page = 0;
        int size = 10;
        List<FilterDTO> filters = Collections.emptyList();
        List<SortDTO> sorts = List.of(
                new SortDTO("nome", SortDTO.Direction.ASC),
                new SortDTO("idade", SortDTO.Direction.DESC)
        );

        Page<TestEntity> pageResult = new PageImpl<>(Collections.emptyList());
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageResult);

        service.listarPaginado(page, size, filters, sorts);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(repository).findAll(any(Specification.class), pageableCaptor.capture());

        Pageable capturedPageable = pageableCaptor.getValue();
        Sort sort = capturedPageable.getSort();

        assertNotNull(sort.getOrderFor("nome"));
        assertEquals(Sort.Direction.ASC, sort.getOrderFor("nome").getDirection());
        assertNotNull(sort.getOrderFor("idade"));
        assertEquals(Sort.Direction.DESC, sort.getOrderFor("idade").getDirection());
    }

    @Test
    @DisplayName("Deve construir Specification corretamente para todos os operadores")
    void listarPaginado_VerificarOperatorsSpecification() {
        int page = 0;
        int size = 10;
        List<SortDTO> sorts = Collections.emptyList();

        List<FilterDTO> filters = List.of(
                new FilterDTO("campo1", "val", FiltroOperadorEnum.EQUALS),
                new FilterDTO("campo2", "val", FiltroOperadorEnum.NOT_EQUALS),
                new FilterDTO("campo3", "val", FiltroOperadorEnum.LIKE),
                new FilterDTO("campo4", "10", FiltroOperadorEnum.GREATER_THAN),
                new FilterDTO("campo5", "10", FiltroOperadorEnum.LESS_THAN),
                new FilterDTO("campo6", "10", FiltroOperadorEnum.GREATER_EQUAL),
                new FilterDTO("campo7", "10", FiltroOperadorEnum.LESS_EQUAL),
                new FilterDTO("campo8", "10,20", FiltroOperadorEnum.BETWEEN)
        );

        when(repository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        service.listarPaginado(page, size, filters, sorts);

        ArgumentCaptor<Specification<TestEntity>> specCaptor = ArgumentCaptor.forClass(Specification.class);
        verify(repository).findAll(specCaptor.capture(), any(Pageable.class));

        Specification<TestEntity> capturedSpec = specCaptor.getValue();

        Root<TestEntity> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Path path = mock(Path.class);

        org.mockito.Mockito.lenient().when(root.get(any(String.class))).thenReturn(path);
        org.mockito.Mockito.lenient().when(cb.conjunction()).thenReturn(mock(Predicate.class));

        org.mockito.Mockito.lenient().when(cb.equal(any(), any())).thenReturn(mock(Predicate.class));
        org.mockito.Mockito.lenient().when(cb.notEqual(any(), any())).thenReturn(mock(Predicate.class));
        org.mockito.Mockito.lenient().when(cb.like(any(), anyString())).thenReturn(mock(Predicate.class));
        org.mockito.Mockito.lenient().when(cb.greaterThan(any(), anyString())).thenReturn(mock(Predicate.class));
        org.mockito.Mockito.lenient().when(cb.lessThan(any(), anyString())).thenReturn(mock(Predicate.class));
        org.mockito.Mockito.lenient().when(cb.greaterThanOrEqualTo(any(), anyString())).thenReturn(mock(Predicate.class));
        org.mockito.Mockito.lenient().when(cb.lessThanOrEqualTo(any(), anyString())).thenReturn(mock(Predicate.class));
        org.mockito.Mockito.lenient().when(cb.between(any(), anyString(), anyString())).thenReturn(mock(Predicate.class));

        capturedSpec.toPredicate(root, query, cb);

        // As verificações (verify) garantem que a lógica está correta, independentemente do stub ser lenient
        verify(cb).equal(path, "val");
        verify(cb).notEqual(path, "val");
        verify(cb).like(path, "%val%");
        verify(cb).greaterThan(path, "10");
        verify(cb).lessThan(path, "10");
        verify(cb).greaterThanOrEqualTo(path, "10");
        verify(cb).lessThanOrEqualTo(path, "10");
        verify(cb).between(path, "10", "20");
    }

    @Test
    @DisplayName("Deve lançar exceção para BETWEEN com formato inválido")
    void listarPaginado_ErroBetween() {
        List<FilterDTO> filters = List.of(
                new FilterDTO("campo", "10", FiltroOperadorEnum.BETWEEN)
        );

        assertThrows(IllegalArgumentException.class, () -> {
            service.listarPaginado(0, 10, filters, Collections.emptyList());
        });
    }
}