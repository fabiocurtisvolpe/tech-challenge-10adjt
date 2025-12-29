package com.postech.adjt.data.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.postech.adjt.data.entidade.RestauranteEntidade;

public interface JpaDataRestauranteRepository extends JpaRepository<RestauranteEntidade, Integer>,
                JpaSpecificationExecutor<RestauranteEntidade> {

        Optional<RestauranteEntidade> findByNome(String nome);
}
