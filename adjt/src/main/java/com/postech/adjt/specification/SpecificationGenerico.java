package com.postech.adjt.specification;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import com.postech.adjt.dto.FiltroGenericoDTO;
import com.postech.adjt.model.TipoUsuario;

public class SpecificationGenerico {

    public static Specification<TipoUsuario> comFiltro(FiltroGenericoDTO filtro) {
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
