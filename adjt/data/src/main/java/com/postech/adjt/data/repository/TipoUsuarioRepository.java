package com.postech.adjt.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.postech.adjt.data.entity.TipoUsuarioEntity;
import com.postech.adjt.data.entity.UsuarioEntity;

/**
 * Repositório de acesso à entidade {@link TipoUsuarioEntity}.
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
        extends JpaRepository<TipoUsuarioEntity, Integer>, JpaSpecificationExecutor<TipoUsuarioEntity> {

    /**
     * Busca um {@link TipoUsuarioEntity} pelo nome.
     *
     * @param nome Nome do tipo de usuário.
     * @return {@link Optional} contendo o tipo de usuário, se encontrado.
     */
    Optional<TipoUsuarioEntity> findByNome(String nome);

    /**
     * Verifica se existe um {@link TipoUsuarioEntity} com o nome informado.
     *
     * @param nome Nome do tipo de usuário.
     * @return {@code true} se existir, {@code false} caso contrário.
     */
    boolean existsByNome(String nome);

    /**
     * Retorna a lista de usuários associados a um {@link TipoUsuarioEntity} com
     * base no
     * ID do usuário.
     *
     * <p>
     * Esse método depende de uma relação mapeada entre {@link TipoUsuarioEntity} e
     * {@link UsuarioEntity}
     * chamada {@code usuarios}.
     * </p>
     *
     * @param id ID do usuário.
     * @return Lista de {@link UsuarioEntity} associados.
     */
    List<UsuarioEntity> findByUsuariosId(Integer id);
}
