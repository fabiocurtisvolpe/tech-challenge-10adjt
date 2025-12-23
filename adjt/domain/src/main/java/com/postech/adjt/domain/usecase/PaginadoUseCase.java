package com.postech.adjt.domain.usecase;

import java.util.List;
import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

/**
 * Caso de Uso Genérico para operações de listagem paginada.
 * <p>
 * Esta classe permite realizar buscas em qualquer repositório que implemente
 * {@link GenericRepositoryPort}, suportando filtros dinâmicos e ordenação.
 *
 * @param <T> O tipo da entidade de domínio que será paginada.
 */
public class PaginadoUseCase<T> {

    /** Porta de saída para comunicação com a camada de dados. */
    private final GenericRepositoryPort<T> repository;

    /**
     * Construtor privado para forçar o uso do método factory {@link #create}.
     */
    private PaginadoUseCase(GenericRepositoryPort<T> repository) {
        this.repository = repository;
    }

    /**
     * Método estático de fábrica para instanciar o caso de uso.
     *
     * @param <T>        Tipo da entidade.
     * @param repository Implementação do repositório genérico.
     * @return Uma nova instância de PaginadoUseCase.
     */
    public static <T> PaginadoUseCase<T> create(GenericRepositoryPort<T> repository) {
        return new PaginadoUseCase<>(repository);
    }

    /**
     * Executa a lógica de busca paginada com validações básicas de parâmetros.
     *
     * @param page    Número da página desejada (inicia em 0).
     * @param size    Quantidade de registros por página.
     * @param filters Lista de filtros dinâmicos (campo, operador, valor).
     * @param sorts   Lista de ordenações (campo, direção).
     * @return {@link ResultadoPaginacaoDTO} contendo os itens e metadados da paginação.
     * @throws NotificacaoException Caso os parâmetros de página ou tamanho sejam inválidos.
     */
    public ResultadoPaginacaoDTO<T> run(int page, int size, List<FilterDTO> filters, List<SortDTO> sorts) {

        // Validação técnica de parâmetros de paginação
        if (page < 0 || size <= 0) {
            throw new NotificacaoException(MensagemUtil.PAGINA_SIZE_INVALIDA);
        }

        // Delegação da busca para o adaptador de persistência através da porta (port)
        return repository.listarPaginado(page, size, filters, sorts);
    }
}