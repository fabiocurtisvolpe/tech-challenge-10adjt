package com.postech.adjt.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.postech.adjt.data.entidade.UsuarioEntidade;

public interface SpringDataUsuarioRepository extends JpaRepository<UsuarioEntidade, Integer>,
                JpaSpecificationExecutor<UsuarioEntidade> {

        Optional<UsuarioEntidade> findByEmail(String email);
}
