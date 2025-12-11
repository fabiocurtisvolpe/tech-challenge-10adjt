package com.postech.adjt.data.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.postech.adjt.data.entidade.UsuarioEntidade;

public interface JpaDataUsuarioRepository extends JpaRepository<UsuarioEntidade, Integer>,
                JpaSpecificationExecutor<UsuarioEntidade> {

        Optional<UsuarioEntidade> findByEmail(String email);
        Optional<UsuarioEntidade> findByNome(String nome);
}
