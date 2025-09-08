package com.postech.adjt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.postech.adjt.model.Usuario;

/**
 * Repositório de acesso à entidade {@link Usuario}.
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
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>, JpaSpecificationExecutor<Usuario> {

    /**
     * Busca um {@link Usuario} pelo nome.
     *
     * @param nome Nome do usuário.
     * @return {@link Optional} contendo o usuário, se encontrado.
     */
    Optional<Usuario> findByNome(String nome);

    /**
     * Busca um {@link Usuario} pelo login.
     *
     * <p>
     * Esse método é utilizado principalmente no processo de autenticação.
     * </p>
     *
     * @param login Login do usuário.
     * @return {@link Optional} contendo o usuário, se encontrado.
     */
    Optional<Usuario> findByLogin(String login);
}
