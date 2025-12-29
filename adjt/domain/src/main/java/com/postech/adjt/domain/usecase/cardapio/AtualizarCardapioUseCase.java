package com.postech.adjt.domain.usecase.cardapio;

import java.util.Objects;
import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.CardapioDTO;
import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.CardapioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import com.postech.adjt.domain.usecase.util.UsuarioLogadoUtil;

/**
 * Caso de Uso responsável pela atualização de um item de cardápio existente.
 * <p>
 * Este componente valida a existência do item, verifica se o novo nome
 * conflita com outros itens do mesmo restaurante e coordena a atualização
 * de preços, descrições e disponibilidade.
 */
public class AtualizarCardapioUseCase {

    private final GenericRepositoryPort<Cardapio> cardapioRepository;
    private final GenericRepositoryPort<Usuario> usuarioRepository;

    /**
     * Construtor privado para assegurar o uso do método factory.
     */
    private AtualizarCardapioUseCase(GenericRepositoryPort<Cardapio> cardapioRepository,
                                     GenericRepositoryPort<Usuario> usuarioRepository) {
        this.cardapioRepository = cardapioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Factory method para instanciar o Caso de Uso.
     *
     * @param cardapioRepository Porta para o repositório de cardápios.
     * @param usuarioRepository  Porta para o repositório de usuários.
     * @return Uma nova instância de {@link AtualizarCardapioUseCase}.
     */
    public static AtualizarCardapioUseCase create(GenericRepositoryPort<Cardapio> cardapioRepository,
                                                  GenericRepositoryPort<Usuario> usuarioRepository) {
        return new AtualizarCardapioUseCase(cardapioRepository, usuarioRepository);
    }

    /**
     * Executa a lógica de negócio para atualizar um item de cardápio.
     *
     * @param dto            DTO contendo os novos dados do item (nome, preço, etc).
     * @param usuarioLogado  E-mail do usuário autenticado que realiza a operação.
     * @return               A entidade {@link Cardapio} atualizada e persistida.
     * @throws NotificacaoException Caso o ID seja nulo, o item não exista ou o
     *                              nome já esteja em uso por outro item no mesmo restaurante.
     */
    public Cardapio run(CardapioDTO dto, String usuarioLogado) {

        // 1. Validação de ID: O identificador é obrigatório para localizar o registro alvo
        if (Objects.isNull(dto.id())) {
            throw new NotificacaoException(MensagemUtil.ID_NULO);
        }

        // 2. Localização: Verifica se o item de cardápio existe no banco de dados
        final Cardapio cardapio = this.cardapioRepository.obterPorId(dto.id())
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.CARDAPIO_NAO_ENCONTRADO));

        // 3. Auditoria: Resolve o contexto do usuário que está realizando a edição
        final Usuario usrLogado = UsuarioLogadoUtil.usuarioLogado(usuarioRepository, usuarioLogado);

        // 4. Regra de Unicidade Contextual:
        // Verifica se o novo nome informado já pertence a OUTRO item de cardápio
        // dentro do mesmo restaurante.
        final Cardapio cardapioNome = this.cardapioRepository.obterPorNome(dto.nome()).orElse(null);
        if (cardapioNome != null
                && !cardapioNome.getId().equals(cardapio.getId()) // Se o ID for diferente, é outro registro
                && cardapio.getRestaurante().getId().equals(cardapioNome.getRestaurante().getId())) { // No mesmo restaurante

            throw new NotificacaoException(MensagemUtil.CARDAPIO_JA_CADASTRADO);
        }

        // 5. Atualização e Persistência:
        // Utiliza a Factory para reconstruir a entidade com os novos dados,
        // preservando o vínculo original com o restaurante e registrando o autor da alteração.
        return this.cardapioRepository.atualizar(CardapioFactory.cardapio(
                cardapio.getId(),
                dto.nome(),
                dto.descricao(),
                dto.preco(),
                dto.foto(),
                cardapio.getRestaurante(), // Mantém o restaurante original
                dto.disponivel(),
                true, // Mantém como ativo no sistema
                usrLogado.getId()));
    }
}