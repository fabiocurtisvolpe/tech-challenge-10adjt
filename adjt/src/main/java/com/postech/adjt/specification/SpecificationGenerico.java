package com.postech.adjt.specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.postech.adjt.dto.FiltroGenericoDTO;

/**
 * Classe utilitária para construção dinâmica de {@link Specification} JPA
 * com base em filtros genéricos fornecidos via {@link FiltroGenericoDTO}.
 * 
 * <p>
 * Suporta os seguintes tipos de filtros:
 * - String: LIKE, EQUALS, NOT_EQUALS
 * - Number: EQUALS, GREATER_THAN, LESS_THAN, GREATER_EQUAL, LESS_EQUAL
 * - Boolean: EQUALS
 * - DateTime: EQUALS, GREATER_THAN, LESS_THAN, BETWEEN
 * </p>
 * 
 * <p>
 * Formato do filtro: "campo:operador:valor"
 * Exemplos:
 * - nome:like:João
 * - idade:gt:18
 * - ativo:eq:true
 * - dataCriacao:between:2025-01-01T00:00:00,2025-12-31T23:59:59
 * </p>
 * 
 * @author Fabio
 * @since 2025-09-19
 */
public class SpecificationGenerico {

    public enum FilterOperator {
        EQUALS("eq"),
        NOT_EQUALS("ne"),
        LIKE("like"),
        GREATER_THAN("gt"),
        LESS_THAN("lt"),
        GREATER_EQUAL("ge"),
        LESS_EQUAL("le"),
        BETWEEN("between");

        private String operator;

        FilterOperator(String operator) {
            this.operator = operator;
        }

        public static FilterOperator fromString(String operator) {
            for (FilterOperator fo : FilterOperator.values()) {
                if (fo.operator.equalsIgnoreCase(operator)) {
                    return fo;
                }
            }
            throw new IllegalArgumentException("Operador inválido: " + operator);
        }
    }

    /**
     * Cria uma Specification com base nos filtros fornecidos
     * 
     * @param filtroDTO DTO contendo os filtros e informações de paginação
     * @return Specification construída
     */
    public static <T> Specification<T> criarSpecification(FiltroGenericoDTO filtroDTO) {
        return comFiltro(filtroDTO);
    }

    /**
     * Cria um PageRequest com base nas informações de paginação do filtro
     * 
     * @param filtroDTO DTO contendo as informações de paginação
     * @return Pageable configurado
     */
    public static Pageable criarPageable(FiltroGenericoDTO filtroDTO) {
        return PageRequest.of(filtroDTO.getPagina(), filtroDTO.getTamanho());
    }

    private static <T> Specification<T> comFiltro(FiltroGenericoDTO filtroDTO) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtroDTO.getFiltros() == null || filtroDTO.getFiltros().isEmpty()) {
                return null;
            }

            filtroDTO.getFiltros().forEach((valor, tipo) -> {
                try {
                    String[] partes = valor.split(":");
                    validarFormatoFiltro(partes);

                    String campo = partes[0];
                    FilterOperator operador = FilterOperator.fromString(partes[1]);
                    String valorFiltro = partes[2];

                    predicates.add(criarPredicate(cb, root, campo, operador, valorFiltro, tipo));
                } catch (Exception e) {
                    throw new IllegalArgumentException("Erro ao processar filtro: " + valor, e);
                }
            });

            return predicates.isEmpty() ? null : cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void validarFormatoFiltro(String[] partes) {
        if (partes.length < 3) {
            throw new IllegalArgumentException(
                    "Formato de filtro inválido. Use: campo:operador:valor");
        }
    }

    private static <T> Predicate criarPredicate(
            jakarta.persistence.criteria.CriteriaBuilder cb,
            jakarta.persistence.criteria.Root<T> root,
            String campo,
            FilterOperator operador,
            String valor,
            String tipo) {

        return switch (tipo) {
            case "String" -> criarPredicateString(cb, root, campo, operador, valor);
            case "Number" -> criarPredicateNumber(cb, root, campo, operador, valor);
            case "Boolean" -> criarPredicateBoolean(cb, root, campo, valor);
            case "DateTime" -> criarPredicateDateTime(cb, root, campo, operador, valor);
            default -> throw new IllegalArgumentException("Tipo de filtro não suportado: " + tipo);
        };
    }

    private static <T> Predicate criarPredicateString(jakarta.persistence.criteria.CriteriaBuilder cb,
            jakarta.persistence.criteria.Root<T> root, String campo, FilterOperator operador, String valor) {
        switch (operador) {
            case LIKE:
                return cb.like(cb.lower(root.get(campo)), "%" + valor.toLowerCase() + "%");
            case EQUALS:
                return cb.equal(cb.lower(root.get(campo)), valor.toLowerCase());
            case NOT_EQUALS:
                return cb.notEqual(cb.lower(root.get(campo)), valor.toLowerCase());
            default:
                throw new IllegalArgumentException("Operador não suportado para String: " + operador);
        }
    }

    private static <T> Predicate criarPredicateNumber(jakarta.persistence.criteria.CriteriaBuilder cb,
            jakarta.persistence.criteria.Root<T> root, String campo, FilterOperator operador, String valor) {
        Number numeroValor = Double.parseDouble(valor);
        switch (operador) {
            case EQUALS:
                return cb.equal(root.get(campo), numeroValor);
            case NOT_EQUALS:
                return cb.notEqual(root.get(campo), numeroValor);
            case GREATER_THAN:
                return cb.gt(root.get(campo), numeroValor);
            case LESS_THAN:
                return cb.lt(root.get(campo), numeroValor);
            case GREATER_EQUAL:
                return cb.ge(root.get(campo), numeroValor);
            case LESS_EQUAL:
                return cb.le(root.get(campo), numeroValor);
            default:
                throw new IllegalArgumentException("Operador não suportado para Number: " + operador);
        }
    }

    private static <T> Predicate criarPredicateBoolean(jakarta.persistence.criteria.CriteriaBuilder cb,
            jakarta.persistence.criteria.Root<T> root, String campo, String valor) {
        return cb.equal(root.get(campo), Boolean.parseBoolean(valor));
    }

    private static <T> Predicate criarPredicateDateTime(jakarta.persistence.criteria.CriteriaBuilder cb,
            jakarta.persistence.criteria.Root<T> root, String campo, FilterOperator operador, String valor) {
        switch (operador) {
            case EQUALS:
                return cb.equal(root.get(campo), LocalDateTime.parse(valor));
            case GREATER_THAN:
                return cb.greaterThan(root.get(campo), LocalDateTime.parse(valor));
            case LESS_THAN:
                return cb.lessThan(root.get(campo), LocalDateTime.parse(valor));
            case BETWEEN:
                String[] datas = valor.split(",");
                if (datas.length != 2) {
                    throw new IllegalArgumentException(
                            "Para operador BETWEEN são necessárias duas datas separadas por vírgula");
                }
                return cb.between(root.get(campo),
                        LocalDateTime.parse(datas[0]),
                        LocalDateTime.parse(datas[1]));
            default:
                throw new IllegalArgumentException("Operador não suportado para DateTime: " + operador);
        }
    }
}