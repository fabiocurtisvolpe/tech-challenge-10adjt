package com.postech.adjt.specification;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.postech.adjt.constants.MensagemUtil;
import com.postech.adjt.dto.filtro.FiltroGenericoDTO;
import com.postech.adjt.enums.FiltroOperadorEnum;
import com.postech.adjt.exception.NotificacaoException;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * Classe utilitária para construção dinâmica de {@link Specification} JPA
 * com base em filtros genéricos fornecidos via {@link FiltroGenericoDTO}.
 * 
 * @author Fabio
 * @since 2025-09-19
 */
public class SpecificationGenerico {

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

            filtroDTO.getFiltros().forEach((filtro) -> {

                try {    
                    FiltroOperadorEnum operador = FiltroOperadorEnum.fromString(filtro.getOperador());
                    predicates.add(criarPredicate(cb, root, filtro.getCampo(), operador, filtro.getValor(), filtro.getTipo()));

                } catch (IllegalArgumentException e) {
                    throw e;
                } catch (Exception e) {
                    throw new NotificacaoException(MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
                }
            });

            return predicates.isEmpty() ? null : cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    static <T> Predicate criarPredicate(
            CriteriaBuilder cb,
            Root<T> root,
            String campo,
            FiltroOperadorEnum operador,
            String valor,
            String tipo) {

        return switch (tipo) {
            case "string" -> criarPredicateString(cb, root, campo, operador, valor);
            case "number" -> criarPredicateNumber(cb, root, campo, operador, valor);
            case "boolean" -> criarPredicateBoolean(cb, root, campo, valor);
            case "date" -> criarPredicateDate(cb, root, campo, operador, valor);
            default -> throw new IllegalArgumentException("Tipo de filtro não suportado: " + tipo);
        };
    }

    private static <T> Predicate criarPredicateString(CriteriaBuilder cb,
            Root<T> root, String campo, FiltroOperadorEnum operador, String valor) {
        switch (operador) {
            case LIKE:
                return cb.like(cb.lower(root.<String>get(campo)), "%" + valor.toLowerCase() + "%");
            case EQUALS:
                return cb.equal(cb.lower(root.<String>get(campo)), valor.toLowerCase());
            case NOT_EQUALS:
                return cb.notEqual(cb.lower(root.<String>get(campo)), valor.toLowerCase());
            default:
                throw new IllegalArgumentException("Operador não suportado para String: " + operador);
        }
    }

    private static <T> Predicate criarPredicateNumber(CriteriaBuilder cb,
            Root<T> root, String campo, FiltroOperadorEnum operador, String valor) {
        Number numeroValor = Integer.valueOf(valor);
        switch (operador) {
            case EQUALS:
                return cb.equal(root.<Number>get(campo), numeroValor);
            case NOT_EQUALS:
                return cb.notEqual(root.<Number>get(campo), numeroValor);
            case GREATER_THAN:
                return cb.gt(root.<Number>get(campo), numeroValor);
            case LESS_THAN:
                return cb.lt(root.<Number>get(campo), numeroValor);
            case GREATER_EQUAL:
                return cb.ge(root.<Number>get(campo), numeroValor);
            case LESS_EQUAL:
                return cb.le(root.<Number>get(campo), numeroValor);
            default:
                throw new IllegalArgumentException("Operador não suportado para Number: " + operador);
        }
    }

    private static <T> Predicate criarPredicateBoolean(CriteriaBuilder cb,
            Root<T> root, String campo, String valor) {
        return cb.equal(root.<Boolean>get(campo), Boolean.parseBoolean(valor));
    }

    private static <T> Predicate criarPredicateDate(CriteriaBuilder cb,
            Root<T> root,
            String campo,
            FiltroOperadorEnum operador,
            String valor) {

        valor = valor.replaceAll("\\ *", "");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        switch (operador) {
            case GREATER_THAN:
                return cb.greaterThan(root.<LocalDate>get(campo), LocalDate.parse(valor, formatter));
            case LESS_THAN:
                return cb.lessThan(root.<LocalDate>get(campo), LocalDate.parse(valor, formatter));
            case BETWEEN:
                String[] datas = valor.split(",");
                if (datas.length != 2) {
                    throw new IllegalArgumentException(
                            "Para operador BETWEEN são necessárias duas datas separadas por vírgula");
                }
                return cb.between(root.<LocalDate>get(campo),
                        LocalDate.parse(datas[0], formatter),
                        LocalDate.parse(datas[1], formatter));
            default:
                throw new IllegalArgumentException("Operador não suportado para DateTime: " + operador);
        }
    }
}