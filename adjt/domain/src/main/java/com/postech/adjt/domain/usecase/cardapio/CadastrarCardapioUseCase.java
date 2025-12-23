package com.postech.adjt.domain.usecase.cardapio;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.CardapioDTO;
import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.CardapioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import com.postech.adjt.domain.usecase.util.UsuarioLogadoUtil;

import java.util.Objects;

/**
 * Caso de Uso responsável pelo cadastramento de novos itens no cardápio.
 * <p>
 * Este componente orquestra a validação do restaurante, a identificação do usuário
 * logado e garante que não existam itens duplicados com o mesmo nome dentro de um
 * restaurante específico.
 */
public class CadastrarCardapioUseCase {

    private final GenericRepositoryPort<Cardapio> cardapioRepositoryPort;
    private final GenericRepositoryPort<Usuario> usuarioRepository;
    private final GenericRepositoryPort<Restaurante> restauranteRepository;

    /**
     * Construtor privado para assegurar o uso do método factory.
     */
    private CadastrarCardapioUseCase(GenericRepositoryPort<Cardapio> cardapioRepositoryPort,
                                     GenericRepositoryPort<Usuario> usuarioRepository,
                                     GenericRepositoryPort<Restaurante> restauranteRepository) {
        this.cardapioRepositoryPort = cardapioRepositoryPort;
        this.usuarioRepository = usuarioRepository;
        this.restauranteRepository = restauranteRepository;
    }

    /**
     * Factory method para instanciar o Caso de Uso com suas dependências.
     *
     * @param cardapioRepositoryPort Porta para o repositório de cardápios.
     * @param usuarioRepository      Porta para o repositório de usuários.
     * @param restauranteRepository  Porta para o repositório de restaurantes.
     * @return Uma nova instância de {@link CadastrarCardapioUseCase}.
     */
    public static CadastrarCardapioUseCase create(GenericRepositoryPort<Cardapio> cardapioRepositoryPort,
                                                  GenericRepositoryPort<Usuario> usuarioRepository,
                                                  GenericRepositoryPort<Restaurante> restauranteRepository) {
        return new CadastrarCardapioUseCase(cardapioRepositoryPort, usuarioRepository, restauranteRepository);
    }

    /**
     * Executa o fluxo de negócio para cadastrar um novo item no cardápio.
     *
     * @param dto            DTO contendo os dados do item (nome, preço, restaurante pai, etc).
     * @param usuarioLogado  E-mail do usuário autenticado que realiza a operação.
     * @return               A entidade {@link Cardapio} criada e persistida.
     * @throws NotificacaoException Caso o restaurante não seja informado, não exista, ou
     *                              o item já esteja cadastrado para aquele restaurante.
     */
    public Cardapio run(CardapioDTO dto, String usuarioLogado) {

        // 1. Validação de Vínculo: O item de cardápio deve pertencer obrigatoriamente a um restaurante
        if (Objects.isNull(dto.restaurante()) || Objects.isNull(dto.restaurante().id())) {
            throw new NotificacaoException(MensagemUtil.RESTAURANTE_OBRIGATORIO);
        }

        // 2. Busca prévia: Verifica se já existe um item com este nome
        final Cardapio cardapioExistente = this.cardapioRepositoryPort.obterPorNome(dto.nome()).orElse(null);

        // 3. Contexto: Resolve o usuário logado que está operando o sistema
        final Usuario usrLogado = UsuarioLogadoUtil.usuarioLogado(usuarioRepository, usuarioLogado);

        // 4. Integridade: Valida se o restaurante de destino existe na base
        final Restaurante restaurante = this.restauranteRepository.obterPorId(dto.restaurante().id())
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.RESTAURANTE_NAO_ENCONTRADO));

        // 5. Regra de Unicidade Contextual:
        // Verifica se o item com o mesmo nome pertence ao mesmo restaurante.
        // É permitido ter itens com o mesmo nome em restaurantes diferentes, mas não no mesmo.
        if (cardapioExistente != null && cardapioExistente.getRestaurante().getId().equals(restaurante.getId())) {
            throw new NotificacaoException(MensagemUtil.CARDAPIO_JA_CADASTRADO);
        }

        // 6. Criação e Persistência: Utiliza a Factory para montar o objeto e salva no repositório.
        // O id do usuário logado é passado para auditoria/validação interna na factory.
        return this.cardapioRepositoryPort.criar(CardapioFactory.criar(
                dto.nome(),
                dto.descricao(),
                dto.preco(),
                dto.foto(),
                dto.disponivel(),
                restaurante,
                usrLogado.getId()));
    }
}