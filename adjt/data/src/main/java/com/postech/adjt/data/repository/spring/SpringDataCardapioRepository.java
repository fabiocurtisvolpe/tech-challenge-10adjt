package com.postech.adjt.data.repository.spring;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.postech.adjt.data.entidade.CardapioEntidade;

public interface SpringDataCardapioRepository extends JpaRepository<CardapioEntidade, Integer>,
                JpaSpecificationExecutor<CardapioEntidade> {

        Optional<CardapioEntidade> findByNome(String nome);
}
