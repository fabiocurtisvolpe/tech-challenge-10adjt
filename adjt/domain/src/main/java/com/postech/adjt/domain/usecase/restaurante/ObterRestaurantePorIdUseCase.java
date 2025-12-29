package com.postech.adjt.domain.usecase.restaurante;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import com.postech.adjt.domain.usecase.util.UsuarioLogadoUtil;

/**
 * Caso de Uso responsável por obter os detalhes de um restaurante pelo seu ID.
 * <p>
 * Oferece suporte a dois fluxos:
 * 1. Consulta simples (pública): Apenas retorna os dados se o ID for válido.
 * 2. Consulta restrita (proprietário): Retorna os dados apenas se o usuário logado
 *    for o dono do restaurante consultado.
 */
public class ObterRestaurantePorIdUseCase {

    private final GenericRepositoryPort<Restaurante> restauranteRepository;
    private final GenericRepositoryPort<Usuario> usuarioRepository;

    /**
     * Construtor privado para assegurar o uso do método factory.
     */
    private ObterRestaurantePorIdUseCase(GenericRepositoryPort<Restaurante> restauranteRepository,
                                         GenericRepositoryPort<Usuario> usuarioRepository) {
        this.restauranteRepository = restauranteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Factory method para instanciar o Caso de Uso.
     *
     * @param restauranteRepository Porta para o repositório de restaurantes.
     * @param usuarioRepository      Porta para o repositório de usuários.
     * @return Uma nova instância de {@link ObterRestaurantePorIdUseCase}.
     */
    public static ObterRestaurantePorIdUseCase create(GenericRepositoryPort<Restaurante> restauranteRepository,
                                                      GenericRepositoryPort<Usuario> usuarioRepository) {
        return new ObterRestaurantePorIdUseCase(restauranteRepository, usuarioRepository);
    }

    /**
     * Fluxo Público: Busca um restaurante por ID sem validação de dono.
     * Útil para visualização de clientes ou listagens públicas.
     *
     * @param id Identificador único do restaurante.
     * @return   A entidade {@link Restaurante} encontrada.
     * @throws NotificacaoException Caso o ID seja inválido ou o restaurante não exista.
     */
    public Restaurante run(Integer id) {

        if (id == null || id <= 0) {
            throw new NotificacaoException(MensagemUtil.ID_NULO);
        }

        return this.restauranteRepository.obterPorId(id)
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.RESTAURANTE_NAO_ENCONTRADO));
    }

    /**
     * Fluxo Restrito: Busca um restaurante por ID validando se o solicitante é o proprietário.
     * Útil para fluxos de edição, exclusão ou visualização de dados sensíveis.
     *
     * @param id             Identificador único do restaurante.
     * @param usuarioLogado  E-mail do usuário autenticado solicitante.
     * @return               A entidade {@link Restaurante} encontrada.
     * @throws NotificacaoException Caso o restaurante não exista ou o solicitante
     *                              não seja o proprietário legal do estabelecimento.
     */
    public Restaurante run(Integer id, String usuarioLogado) {

        if (id == null || id <= 0) {
            throw new NotificacaoException(MensagemUtil.ID_NULO);
        }

        // Resolve o contexto do usuário que está logado
        final Usuario usrLogado = UsuarioLogadoUtil.usuarioLogado(usuarioRepository, usuarioLogado);

        // Localiza o restaurante alvo
        Restaurante restaurante = this.restauranteRepository.obterPorId(id)
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.RESTAURANTE_NAO_ENCONTRADO));

        // Regra de Segurança: Valida se o ID do solicitante coincide com o ID do dono do restaurante
        if (!restaurante.getDono().getId().equals(usrLogado.getId())) {
            throw new NotificacaoException(MensagemUtil.USUARIO_NAO_PERMITE_OPERACAO);
        }

        return restaurante;
    }

}