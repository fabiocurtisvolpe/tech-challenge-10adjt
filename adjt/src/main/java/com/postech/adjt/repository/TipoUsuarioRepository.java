package com.postech.adjt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.postech.adjt.model.TipoUsuario;
import com.postech.adjt.model.Usuario;

/**
 * Repositório de acesso à entidade {@link TipoUsuario}.
 *
 * <p>
 * Estende {@link JpaRepository} para operações CRUD e
 * {@link JpaSpecificationExecutor}
 * para consultas dinâmicas com {@code Specification}.
 * </p>
 *
 * <p>
 * Inclui métodos personalizados para buscar por nome e verificar existência,
 * além de recuperar usuários associados a um tipo de usuário.
 * </p>
 *
 * @author Fabio
 * @since 2025-09-08
 */
@Repository
public interface TipoUsuarioRepository
        extends JpaRepository<TipoUsuario, Integer>, JpaSpecificationExecutor<TipoUsuario> {

    /**
     * Busca um {@link TipoUsuario} pelo nome.
     *
     * @param nome Nome do tipo de usuário.
     * @return {@link Optional} contendo o tipo de usuário, se encontrado.
     */
    Optional<TipoUsuario> findByNome(String nome);

    /**
     * Verifica se existe um {@link TipoUsuario} com o nome informado.
     *
     * @param nome Nome do tipo de usuário.
     * @return {@code true} se existir, {@code false} caso contrário.
     */
    boolean existsByNome(String nome);

    /**
     * Retorna a lista de usuários associados a um {@link TipoUsuario} com base no
     * ID do usuário.
     *
     * <p>
     * Esse método depende de uma relação mapeada entre {@link TipoUsuario} e
     * {@link Usuario}
     * chamada {@code usuarios}.
     * </p>
     *
     * @param id ID do usuário.
     * @return Lista de {@link Usuario} associados.
     */
    List<Usuario> findByUsuariosId(Integer id);
}
