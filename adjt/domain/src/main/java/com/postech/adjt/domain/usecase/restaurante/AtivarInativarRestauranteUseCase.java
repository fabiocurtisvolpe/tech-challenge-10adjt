package com.postech.adjt.domain.usecase.restaurante;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.RestauranteFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import com.postech.adjt.domain.usecase.util.UsuarioLogadoUtil;

/**
 * Caso de Uso responsável por ativar ou inativar um restaurante.
 * <p>
 * Este componente gerencia o ciclo de vida operacional do restaurante,
 * permitindo habilitar ou desabilitar o estabelecimento no sistema,
 * preservando toda a sua configuração e histórico.
 */
public class AtivarInativarRestauranteUseCase {

    private final GenericRepositoryPort<Restaurante> restauranteRepository;
    private final GenericRepositoryPort<Usuario> usuarioRepository;

    /**
     * Construtor privado para assegurar o uso do método factory.
     */
    private AtivarInativarRestauranteUseCase(GenericRepositoryPort<Restaurante> restauranteRepository,
                                             GenericRepositoryPort<Usuario> usuarioRepository) {
        this.restauranteRepository = restauranteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Factory method para instanciar o Caso de Uso.
     *
     * @param restauranteRepository Porta para o repositório de restaurantes.
     * @param usuarioRepository      Porta para o repositório de usuários.
     * @return Uma nova instância de {@link AtivarInativarRestauranteUseCase}.
     */
    public static AtivarInativarRestauranteUseCase create(GenericRepositoryPort<Restaurante> restauranteRepository,
                                                          GenericRepositoryPort<Usuario> usuarioRepository) {
        return new AtivarInativarRestauranteUseCase(restauranteRepository, usuarioRepository);
    }

    /**
     * Executa a lógica de ativação ou inativação de um restaurante.
     *
     * @param ativar         Status desejado (true para ativo, false para inativo).
     * @param id             Identificador único do restaurante alvo.
     * @param usuarioLogado  E-mail do usuário autenticado que realiza a operação.
     * @return               A entidade {@link Restaurante} com o status atualizado.
     * @throws NotificacaoException Caso o restaurante não exista ou o usuário logado não seja encontrado.
     */
    public Restaurante run(Boolean ativar, Integer id, String usuarioLogado) {

        // 1. Localização: Busca o restaurante alvo pelo identificador único
        final Restaurante restaurante = this.restauranteRepository.obterPorId(id)
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.RESTAURANTE_NAO_ENCONTRADO));

        // 2. Auditoria: Resolve o contexto do usuário que está operando a ação
        final Usuario usrLogado = UsuarioLogadoUtil.usuarioLogado(usuarioRepository, usuarioLogado);

        // 3. Reconstrução e Persistência:
        // Utiliza a Factory para criar uma nova representação do restaurante,
        // mantendo todos os dados originais (nome, endereço, dono) e alterando apenas
        // o campo 'ativo' com o novo status e o ID do editor.
        return restauranteRepository.atualizar(RestauranteFactory.restaurante(restaurante.getId(),
                restaurante.getNome(),
                restaurante.getDescricao(),
                restaurante.getHorarioFuncionamento(),
                restaurante.getTipoCozinha(),
                restaurante.getEndereco(),
                restaurante.getDono(),
                ativar,
                usrLogado.getId()));
    }
}