package com.postech.adjt.domain.usecase.cardapio;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.CardapioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import com.postech.adjt.domain.usecase.util.UsuarioLogadoUtil;

/**
 * Caso de Uso responsável por ativar ou inativar um item de cardápio.
 * <p>
 * Este componente gerencia o ciclo de vida do item no sistema, permitindo
 * habilitar ou desabilitar o registro (exclusão lógica) enquanto mantém o
 * rastreio de qual usuário realizou a alteração.
 */
public class AtivarInativarCardapioUseCase {

    private final GenericRepositoryPort<Cardapio> cardapioRepositoryPort;
    private final GenericRepositoryPort<Usuario> usuarioRepository;

    /**
     * Construtor privado para assegurar o uso do método factory.
     */
    private AtivarInativarCardapioUseCase(GenericRepositoryPort<Cardapio> cardapioRepositoryPort,
                                          GenericRepositoryPort<Usuario> usuarioRepository) {
        this.cardapioRepositoryPort = cardapioRepositoryPort;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Factory method para instanciar o Caso de Uso.
     *
     * @param cardapioRepositoryPort Porta para o repositório de cardápios.
     * @param usuarioRepository      Porta para o repositório de usuários.
     * @return Uma nova instância de {@link AtivarInativarCardapioUseCase}.
     */
    public static AtivarInativarCardapioUseCase create(GenericRepositoryPort<Cardapio> cardapioRepositoryPort,
                                                       GenericRepositoryPort<Usuario> usuarioRepository) {
        return new AtivarInativarCardapioUseCase(cardapioRepositoryPort, usuarioRepository);
    }

    /**
     * Executa a lógica de ativação ou inativação do item de cardápio.
     *
     * @param ativo          Status desejado (true para ativo, false para inativo).
     * @param id             Identificador único do item de cardápio.
     * @param usuarioLogado  E-mail do usuário autenticado que realiza a operação.
     * @return               A entidade {@link Cardapio} com o status atualizado.
     * @throws NotificacaoException Caso o item não seja encontrado ou o usuário logado seja inválido.
     */
    public Cardapio run(Boolean ativo, Integer id, String usuarioLogado) throws IllegalArgumentException {

        // 1. Localização: Busca o item de cardápio alvo pelo ID
        final Cardapio cardapio = this.cardapioRepositoryPort.obterPorId(id)
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.CARDAPIO_NAO_ENCONTRADO));

        // 2. Auditoria: Resolve o contexto do usuário que está operando a ação
        final Usuario usrLogado = UsuarioLogadoUtil.usuarioLogado(usuarioRepository, usuarioLogado);

        // 3. Reconstrução e Persistência:
        // Utiliza a Factory para criar uma nova representação do item, preservando todos
        // os dados originais (nome, preço, restaurante) e alterando apenas o status
        // de ativação e registrando o ID do editor.
        return this.cardapioRepositoryPort.atualizar(CardapioFactory.cardapio(
                id,
                cardapio.getNome(),
                cardapio.getDescricao(),
                cardapio.getPreco(),
                cardapio.getFoto(),
                cardapio.getRestaurante(),
                cardapio.getDisponivel(),
                ativo, // Novo status de ativação
                usrLogado.getId())); // ID do editor para auditoria
    }
}