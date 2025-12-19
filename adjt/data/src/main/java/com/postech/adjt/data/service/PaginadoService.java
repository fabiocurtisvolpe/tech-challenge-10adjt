package com.postech.adjt.data.service;

import java.util.List;

import com.postech.adjt.domain.enums.FiltroOperadorEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.postech.adjt.data.mapper.EntityMapper;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;

import jakarta.transaction.Transactional;

public class PaginadoService<E, D> {

    private final JpaSpecificationExecutor<E> repository;
    private final EntityMapper<E, D> mapper;

    public PaginadoService(JpaSpecificationExecutor<E> repository, EntityMapper<E, D> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public ResultadoPaginacaoDTO<D> listarPaginado(int page, int size, List<FilterDTO> filters,
            List<SortDTO> sorts) {

        for (FilterDTO f : filters) {
            if (f.getOperador() == FiltroOperadorEnum.BETWEEN) {
                String[] valores = f.getValor().split(",");
                if (valores.length != 2) {
                    throw new IllegalArgumentException("Valor inválido para BETWEEN: " + f.getValor());
                }
            }
        }

        Specification<E> spec = (root, query, cb) -> cb.conjunction();

        for (FilterDTO f : filters) {
            spec = spec.and((root, query, cb) -> {

                switch (f.getOperador()) {
                    case EQUALS:
                        return cb.equal(root.get(f.getCampo()), f.getValor());
                    case NOT_EQUALS:
                        return cb.notEqual(root.get(f.getCampo()), f.getValor());
                    case LIKE:
                        return cb.like(root.get(f.getCampo()), "%" + f.getValor() + "%");
                    case GREATER_THAN:
                        return cb.greaterThan(root.get(f.getCampo()), f.getValor());
                    case LESS_THAN:
                        return cb.lessThan(root.get(f.getCampo()), f.getValor());
                    case GREATER_EQUAL:
                        return cb.greaterThanOrEqualTo(root.get(f.getCampo()), f.getValor());
                    case LESS_EQUAL:
                        return cb.lessThanOrEqualTo(root.get(f.getCampo()), f.getValor());
                    case BETWEEN:
                        String[] valores = f.getValor().split(",");
                        return cb.between(root.get(f.getCampo()), valores[0], valores[1]);
                    default:
                        throw new UnsupportedOperationException("Operador não suportado: " + f.getOperador());
                }
            });
        }

        Sort springSort = Sort.unsorted();
        for (SortDTO s : sorts) {
            springSort = springSort
                    .and(Sort.by(s.getOrdem() == SortDTO.Direction.ASC ? Sort.Direction.ASC : Sort.Direction.DESC,
                            s.getCampo()));
        }

        Page<E> result = repository.findAll(spec, PageRequest.of(page, size, springSort));

        List<D> content = result.getContent()
                .stream()
                .map(mapper::toDomain)
                .toList();

        return new ResultadoPaginacaoDTO<>(content, result.getNumber(), result.getSize(),
                result.getTotalElements());
    }
}