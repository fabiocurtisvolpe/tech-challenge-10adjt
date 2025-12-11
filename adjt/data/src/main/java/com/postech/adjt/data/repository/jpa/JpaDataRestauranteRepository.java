package com.postech.adjt.data.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.postech.adjt.data.entidade.RestauranteEntirade;

public interface JpaDataRestauranteRepository extends JpaRepository<RestauranteEntirade, Integer>,
                JpaSpecificationExecutor<RestauranteEntirade> {

        Optional<RestauranteEntirade> findByNome(String nome);
}
