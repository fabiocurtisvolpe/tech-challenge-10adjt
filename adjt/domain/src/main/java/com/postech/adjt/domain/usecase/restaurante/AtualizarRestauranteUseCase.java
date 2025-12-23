package com.postech.adjt.domain.usecase.restaurante;

import java.util.Objects;
import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.RestauranteDTO;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.EnderecoFactory;
import com.postech.adjt.domain.factory.RestauranteFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import com.postech.adjt.domain.usecase.util.UsuarioLogadoUtil;

/**
 * Caso de Uso responsável pela atualização de um restaurante existente.
 * <p>
 * Este componente valida a existência do registro, garante que o novo nome
 * não esteja em uso por outro restaurante e coordena a atualização das
 * informações de funcionamento e endereço.
 */
public class AtualizarRestauranteUseCase {

    private final GenericRepositoryPort<Restaurante> restauranteRepository;
    private final GenericRepositoryPort<Usuario> usuarioRepository;

    /**
     * Construtor privado para assegurar o uso do método factory.
     */
    private AtualizarRestauranteUseCase(GenericRepositoryPort<Restaurante> restauranteRepository,
                                        GenericRepositoryPort<Usuario> usuarioRepository) {
        this.restauranteRepository = restauranteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Factory method para instanciar o Caso de Uso.
     *
     * @param restauranteRepository Porta para o repositório de restaurantes.
     * @param usuarioRepository      Porta para o repositório de usuários.
     * @return Uma nova instância de {@link AtualizarRestauranteUseCase}.
     */
    public static AtualizarRestauranteUseCase create(GenericRepositoryPort<Restaurante> restauranteRepository,
                                                     GenericRepositoryPort<Usuario> usuarioRepository) {
        return new AtualizarRestauranteUseCase(restauranteRepository, usuarioRepository);
    }

    /**
     * Executa a lógica de negócio para atualizar os dados de um restaurante.
     *
     * @param dto            DTO contendo os novos dados do restaurante.
     * @param usuarioLogado  E-mail do usuário autenticado que realiza a operação.
     * @return               A entidade {@link Restaurante} atualizada e persistida.
     * @throws NotificacaoException Caso o ID seja nulo, o restaurante não exista,
     *                              ou o novo nome já esteja em uso por outro estabelecimento.
     */
    public Restaurante run(RestauranteDTO dto, String usuarioLogado) {

        // 1. Validação de ID: O identificador é obrigatório para atualização
        if (Objects.isNull(dto.id())) {
            throw new NotificacaoException(MensagemUtil.ID_NULO);
        }

        // 2. Localização: Verifica se o restaurante alvo existe no banco de dados
        final Restaurante restauranteExistente = this.restauranteRepository.obterPorId(dto.id())
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.RESTAURANTE_NAO_ENCONTRADO));

        // 3. Regra de Unicidade: Verifica se o novo nome já pertence a OUTRO restaurante.
        // Se o nome existir e o ID for diferente do atual, bloqueia a operação.
        final Restaurante restauranteNome = this.restauranteRepository.obterPorNome(dto.nome()).orElse(null);
        if ((restauranteNome != null) && (!restauranteExistente.getId().equals(restauranteNome.getId()))) {
            throw new NotificacaoException(MensagemUtil.RESTAURANTE_JA_CADASTRADO);
        }

        // 4. Contexto: Resolve o usuário logado para fins de auditoria
        final Usuario usrLogado = UsuarioLogadoUtil.usuarioLogado(usuarioRepository, usuarioLogado);

        // 5. Transformação: Converte o DTO de endereço em entidade de domínio
        final Endereco endereco = EnderecoFactory.toEndereco(dto.endereco());

        // 6. Persistência: Reconstrói a entidade via Factory preservando o Dono original
        // e atualiza através do adaptador de repositório.
        return restauranteRepository.atualizar(RestauranteFactory.restaurante(
                restauranteExistente.getId(),
                dto.nome(),
                dto.descricao(),
                dto.horarioFuncionamento(),
                dto.tipoCozinha(),
                endereco,
                restauranteExistente.getDono(), // O dono original é mantido
                true,
                usrLogado.getId())); // ID do editor registrado na factory
    }
}