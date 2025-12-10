package com.postech.adjt.data.repository.spring;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.postech.adjt.data.entidade.RestauranteEntirade;

public interface SpringDataRestauranteRepository extends JpaRepository<RestauranteEntirade, Integer>,
                JpaSpecificationExecutor<RestauranteEntirade> {

        Optional<RestauranteEntirade> findByNome(String nome);
}
