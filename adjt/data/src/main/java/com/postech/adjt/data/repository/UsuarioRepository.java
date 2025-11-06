package com.postech.adjt.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.postech.adjt.data.entity.UsuarioEntity;

/**
 * Repositório de acesso à entidade {@link UsuarioEntity}.
 *
 * <p>
 * Estende {@link JpaRepository} para operações CRUD e
 * {@link JpaSpecificationExecutor}
 * para consultas dinâmicas com {@code Specification}.
 * </p>
 *
 * <p>
 * Inclui métodos personalizados para buscar usuários por nome ou login.
 * </p>
 *
 * <p>
 * Utilizado principalmente na autenticação e gerenciamento de usuários.
 * </p>
 *
 * @author Fabio
 * @since 2025-09-08
 */
@Repository
public interface UsuarioRepository
        extends JpaRepository<UsuarioEntity, Integer>, JpaSpecificationExecutor<UsuarioEntity> {

    /**
     * Busca um {@link UsuarioEntity} pelo nome.
     *
     * @param nome Nome do usuário.
     * @return {@link Optional} contendo o usuário, se encontrado.
     */
    Optional<UsuarioEntity> findByNome(String nome);

    /**
     * Busca um {@link UsuarioEntity} pelo e-mail.
     *
     * <p>
     * Esse método é utilizado principalmente no processo de autenticação.
     * </p>
     *
     * @param email E-mail do usuário.
     * @return {@link Optional} contendo o usuário, se encontrado.
     */
    Optional<UsuarioEntity> findByEmail(String email);
}
