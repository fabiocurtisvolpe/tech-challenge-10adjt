package com.postech.adjt.domain.usecase.restaurante;

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
 * Caso de Uso responsável pelo cadastramento de um novo restaurante.
 * <p>
 * Este componente coordena a validação de unicidade do nome, a identificação do
 * proprietário logado, a conversão do endereço e a criação da entidade de domínio
 * através de fábricas especializadas.
 */
public class CadastrarRestauranteUseCase {

    private final GenericRepositoryPort<Restaurante> restauranteRepository;
    private final GenericRepositoryPort<Usuario> usuarioRepository;

    /**
     * Construtor privado para assegurar o uso do método factory.
     */
    private CadastrarRestauranteUseCase(GenericRepositoryPort<Restaurante> restauranteRepository,
                                        GenericRepositoryPort<Usuario> usuarioRepository) {
        this.restauranteRepository = restauranteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Factory method para instanciar o Caso de Uso.
     *
     * @param restauranteRepository Porta para o repositório de restaurantes.
     * @param usuarioRepository      Porta para o repositório de usuários.
     * @return Uma nova instância de {@link CadastrarRestauranteUseCase}.
     */
    public static CadastrarRestauranteUseCase create(GenericRepositoryPort<Restaurante> restauranteRepository,
                                                     GenericRepositoryPort<Usuario> usuarioRepository) {
        return new CadastrarRestauranteUseCase(restauranteRepository, usuarioRepository);
    }

    /**
     * Executa o fluxo de negócio para cadastrar um novo restaurante.
     *
     * @param dto            Objeto de transferência contendo os dados do restaurante.
     * @param usuarioLogado  E-mail do usuário autenticado (proprietário).
     * @return               A entidade {@link Restaurante} criada e persistida.
     * @throws NotificacaoException Caso o nome do restaurante já exista ou o
     *                              usuário logado não seja encontrado.
     */
    public Restaurante run(RestauranteDTO dto, String usuarioLogado) {

        // 1. Unicidade: Verifica se já existe um restaurante cadastrado com o mesmo nome
        final Restaurante restaurante = this.restauranteRepository.obterPorNome(dto.nome()).orElse(null);
        if (restaurante != null) {
            throw new NotificacaoException(MensagemUtil.RESTAURANTE_JA_CADASTRADO);
        }

        // 2. Contexto: Obtém a entidade do proprietário logado a partir do e-mail (Token)
        final Usuario usrLogado = UsuarioLogadoUtil.usuarioLogado(usuarioRepository, usuarioLogado);

        // 3. Transformação: Converte o DTO de endereço em uma entidade de domínio
        final Endereco endereco = EnderecoFactory.toEndereco(dto.endereco());

        // 4. Criação e Persistência: Utiliza a Factory para montar o objeto rico e
        // o envia para o adaptador de banco de dados através da porta.
        return restauranteRepository.criar(RestauranteFactory.criar(
                dto.nome(),
                dto.descricao(),
                dto.horarioFuncionamento(),
                dto.tipoCozinha(),
                endereco,
                usrLogado));
    }
}