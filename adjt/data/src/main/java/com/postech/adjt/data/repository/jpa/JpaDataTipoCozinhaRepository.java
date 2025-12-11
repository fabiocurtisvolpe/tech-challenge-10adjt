package com.postech.adjt.data.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.postech.adjt.data.entidade.TipoCozinhaEntidade;

public interface JpaDataTipoCozinhaRepository extends JpaRepository<TipoCozinhaEntidade, Integer>,
                JpaSpecificationExecutor<TipoCozinhaEntidade> {

        Optional<TipoCozinhaEntidade> findByNome(String nome);
}
