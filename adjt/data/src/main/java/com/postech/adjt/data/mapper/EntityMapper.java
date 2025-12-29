package com.postech.adjt.data.mapper;

public interface EntityMapper<E, D> {
    D toDomain(E entity);
    E toEntity(D domain);
}