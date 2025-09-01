package com.postech.adjt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.postech.adjt.model.TipoUsuario;
import com.postech.adjt.model.Usuario;


@Repository
public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, Integer>, JpaSpecificationExecutor<TipoUsuario> {

    Optional<TipoUsuario> findByNome(String nome);
    
    boolean existsByNome(String nome);
    List<Usuario> findByUsuariosId(Integer id);
}
