package com.postech.adjt.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.postech.adjt.data.entidade.TipoCozinhaEntidade;

public interface SpringDataTipoCozinhaRepository extends JpaRepository<TipoCozinhaEntidade, Integer>,
                JpaSpecificationExecutor<TipoCozinhaEntidade> {

        Optional<TipoCozinhaEntidade> findByNome(String nome);
}
