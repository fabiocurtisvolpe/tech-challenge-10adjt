package com.postech.adjt.specification;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import com.postech.adjt.dto.FiltroGenericoDTO;

/**
 * Classe utilitária para construção dinâmica de {@link Specification} JPA
 * com base em filtros genéricos fornecidos via {@link FiltroGenericoDTO}.
 * 
 * <p>
 * Atualmente suporta filtros do tipo {@code String}, aplicando cláusulas
 * {@code LIKE} com case-insensitive.
 * </p>
 * 
 * <p>
 * Exemplo de valor esperado no filtro: {@code "nome:Fabio"} onde "nome" é o
 * campo
 * da entidade e "Fabio" é o valor a ser buscado.
 * </p>
 * 
 * @author Fabio
 * @since 2025-09-05
 */
public class SpecificationGenerico {

    /**
     * Cria uma {@link Specification} genérica com base nos filtros fornecidos.
     * 
     * @param <T>    Tipo da entidade JPA alvo da especificação.
     * @param filtro DTO contendo os filtros a serem aplicados.
     * @return Specification que pode ser usada em consultas JPA.
     */
    public static <T> Specification<T> comFiltro(FiltroGenericoDTO filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            filtro.getFiltros().forEach((valor, tipo) -> {

                switch (tipo) {
                    case "String":
                        String v[] = valor.split(":");
                        predicates.add(cb.like(cb.lower(root.get(v[0])), v[1].toLowerCase() + "%"));
                        break;

                    default:
                        break;
                }

            });

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
