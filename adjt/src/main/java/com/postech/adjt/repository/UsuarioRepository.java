package com.postech.adjt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.postech.adjt.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>, JpaSpecificationExecutor<Usuario> {

    Optional<Usuario> findByNome(String nome);

    Optional<Usuario> findByLogin(String login);
}
