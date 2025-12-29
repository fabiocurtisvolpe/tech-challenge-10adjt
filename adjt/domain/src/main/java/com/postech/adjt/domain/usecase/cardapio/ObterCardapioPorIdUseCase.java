package com.postech.adjt.domain.usecase.cardapio;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

/**
 * Caso de Uso responsável por buscar um item do Cardápio pelo seu ID.
 * <p>
 * Atua como uma operação de consulta simples na camada de domínio, garantindo
 * que o identificador fornecido seja válido e o ‘item’ exista na base de dados.
 */
public class ObterCardapioPorIdUseCase {

    /** Porta de saída para operações de persistência e consulta de Cardápio. */
    private final GenericRepositoryPort<Cardapio> cardapioRepositoryPort;

    /**
     * Construtor privado para assegurar o uso do método factory.
     */
    private ObterCardapioPorIdUseCase(GenericRepositoryPort<Cardapio> cardapioRepositoryPort) {
        this.cardapioRepositoryPort = cardapioRepositoryPort;
    }

    /**
     * Factory method para instanciar o Caso de Uso.
     *
     * @param cardapioRepositoryPort Implementação da porta de repositório de cardápio.
     * @return Uma nova instância de {@link ObterCardapioPorIdUseCase}.
     */
    public static ObterCardapioPorIdUseCase create(GenericRepositoryPort<Cardapio> cardapioRepositoryPort) {
        return new ObterCardapioPorIdUseCase(cardapioRepositoryPort);
    }

    /**
     * Executa a busca de um item de cardápio pelo ID.
     *
     * @param id Identificador único do item de cardápio.
     * @return   A entidade {@link Cardapio} encontrada.
     * @throws NotificacaoException Caso o ID seja nulo, menor ou igual a zero,
     *                              ou o item não seja encontrado.
     */
    public Cardapio run(Integer id) {

        // 1. Validação técnica do parâmetro de entrada
        if (id == null || id <= 0) {
            throw new NotificacaoException(MensagemUtil.ID_NULO);
        }

        // 2. Consulta ao repositório através da porta (Port/Adapter)
        // Converte o Optional em resultado ou lança exceção de negócio
        return this.cardapioRepositoryPort.obterPorId(id)
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.CARDAPIO_NAO_ENCONTRADO));
    }
}