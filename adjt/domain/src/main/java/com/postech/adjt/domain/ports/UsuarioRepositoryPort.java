package com.postech.adjt.domain.ports;

import java.util.List;
import java.util.Optional;

import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;
import com.postech.adjt.domain.entidade.Usuario;

/**
 * Port (Interface de Contrato) para o repositório de usuários.
 * 
 * Define as operações que podem ser realizadas com usuários,
 * isolando a lógica de negócio da implementação de persistência.
 * 
 * Esta é uma dependência da camada de negócio (domain) que será
 * implementada na camada de dados (data).
 * 
 * @author Fabio
 * @since 2025-11-24
 */
/**
 * Porta (port) de repositório que define as operações de persistência para a entidade
 * Usuario. Esta interface abstrai os detalhes de armazenamento e expõe as operações
 * necessárias ao domínio e à camada de aplicação:
 *
 * - criar(Usuario): persiste um novo usuário e retorna a instância criada.
 * - obterPorId(Integer): busca um usuário pelo identificador, retornando Optional vazio
 *   quando não encontrado.
 * - obterPorEmail(String): busca um usuário pelo e-mail, retornando Optional vazio
 *   quando não encontrado.
 * - atualizar(Usuario): atualiza um usuário existente e retorna a entidade atualizada.
 * - listarPaginado(int, int, List<FilterDTO>, List<SortDTO>): lista usuários de forma
 *   paginada, aplicando filtros e ordenações, retornando um ResultadoPaginacaoDTO.
 * - desativar(Integer): realiza um soft delete (marca o usuário como inativo), preservando
 *   histórico e referências.
 * - ativar(Integer): reativa um usuário previamente desativado.
 *
 * Implementações concretas devem garantir conformidade com as regras de domínio
 * (por exemplo, unicidade de e-mail), tratar consistência e transações conforme necessário,
 * e mapear/expor erros apropriados para a camada superior.
 */
public interface UsuarioRepositoryPort {

    /**
     * Cria um novo usuário.
     *
     * @param usuario O usuário a ser criado
     * @return O usuário criado
     */
    Usuario criar(Usuario usuario);

    /**
     * Busca um usuário pelo ID.
     *
     * @param id O ID do usuário
     * @return Optional contendo o usuário se encontrado
     */
    Optional<Usuario> obterPorId(Integer id);

    /**
     * Busca um usuário pelo email.
     *
     * @param email O email do usuário
     * @return Optional contendo o usuário se encontrado
     */
    Optional<Usuario> obterPorEmail(String email);

    /**
     * Atualiza um usuário existente.
     *
     * @param usuario O usuário com dados atualizados
     * @return O usuário atualizado
     */
    Usuario atualizar(Usuario usuario);

    /**
     * Lista usuários de forma paginada, aplicando filtros e ordenações.
     *
     * @param page Número da página (0-based)
     * @param size Tamanho da página
     * @param filters Lista de filtros a serem aplicados
     * @param sorts Lista de ordenações a serem aplicadas
     * @return ResultadoPaginacaoDTO contendo os usuários paginados
     */    
    ResultadoPaginacaoDTO<Usuario> listarPaginado(int page, int size, List<FilterDTO> filters, List<SortDTO> sorts);

    /**
     * Desativa um usuário (soft delete).
     *
     * @param id O ID do usuário a desativar
     */
    void desativar(Integer id);

    /**
     * Ativa um usuário.
     *
     * @param id O ID do usuário a ativar
     */
    void ativar(Integer id);
}
