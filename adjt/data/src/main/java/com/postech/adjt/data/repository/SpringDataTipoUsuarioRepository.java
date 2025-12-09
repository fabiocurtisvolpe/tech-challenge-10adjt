package com.postech.adjt.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.postech.adjt.data.entidade.TipoUsuarioEntidade;

public interface SpringDataTipoUsuarioRepository extends JpaRepository<TipoUsuarioEntidade, Integer>,
                JpaSpecificationExecutor<TipoUsuarioEntidade> {

        Optional<TipoUsuarioEntidade> findByNome(String nome);
}
