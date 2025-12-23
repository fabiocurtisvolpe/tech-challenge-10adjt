package com.postech.adjt.domain.usecase.tipoUsuario;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.TipoUsuarioDTO;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.TipoUsuarioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import com.postech.adjt.domain.usecase.util.UsuarioLogadoUtil;

/**
 * Caso de Uso responsável pelo cadastramento de novos Tipos de Usuário (Perfis).
 * <p>
 * Este componente orquestra a criação de perfis personalizados para um restaurante,
 * garantindo que o nome do perfil seja único dentro do contexto daquele estabelecimento.
 */
public class CadastrarTipoUsuarioUseCase {

    private final GenericRepositoryPort<TipoUsuario> tipoUsuariorepository;
    private final GenericRepositoryPort<Restaurante> restauranteRepository;
    private final GenericRepositoryPort<Usuario> usuarioRepository;

    /**
     * Construtor privado para assegurar o uso do método factory.
     */
    private CadastrarTipoUsuarioUseCase(GenericRepositoryPort<TipoUsuario> tipoUsuariorepository,
                                        GenericRepositoryPort<Usuario> usuarioRepository,
                                        GenericRepositoryPort<Restaurante> restauranteRepository) {
        this.tipoUsuariorepository = tipoUsuariorepository;
        this.usuarioRepository = usuarioRepository;
        this.restauranteRepository = restauranteRepository;
    }

    /**
     * Factory method para instanciar o Caso de Uso com suas dependências.
     *
     * @param tipoUsuariorepository  Porta para o repositório de perfis.
     * @param usuarioRepository      Porta para o repositório de usuários.
     * @param restauranteRepository  Porta para o repositório de restaurantes.
     * @return Uma nova instância de {@link CadastrarTipoUsuarioUseCase}.
     */
    public static CadastrarTipoUsuarioUseCase create(GenericRepositoryPort<TipoUsuario> tipoUsuariorepository,
                                                     GenericRepositoryPort<Usuario> usuarioRepository,
                                                     GenericRepositoryPort<Restaurante> restauranteRepository) {
        return new CadastrarTipoUsuarioUseCase(tipoUsuariorepository, usuarioRepository, restauranteRepository);
    }

    /**
     * Executa a lógica de negócio para cadastrar um novo perfil de usuário.
     *
     * @param dto            Objeto de transferência com os dados do novo perfil.
     * @param usuarioLogado  E-mail do usuário autenticado que realiza a operação.
     * @return               A entidade {@link TipoUsuario} criada e persistida.
     * @throws NotificacaoException Caso o restaurante não exista ou o nome do perfil
     *                              já esteja em uso para o mesmo restaurante.
     */
    public TipoUsuario run(TipoUsuarioDTO dto, String usuarioLogado) {

        // 1. Busca prévia: Verifica se já existe um perfil com o nome informado
        final TipoUsuario tipoUsuarioExistente = this.tipoUsuariorepository.obterPorNome(dto.nome()).orElse(null);

        // 2. Contexto: Resolve o usuário logado que está operando o sistema
        final Usuario usrLogado = UsuarioLogadoUtil.usuarioLogado(usuarioRepository, usuarioLogado);

        // 3. Integridade: Valida se o restaurante ao qual o perfil será vinculado existe
        final Restaurante restaurante = this.restauranteRepository.obterPorId(dto.restaurante().id())
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.RESTAURANTE_NAO_ENCONTRADO));

        // 4. Regra de Negócio (Unicidade Contextual):
        // Impede que o mesmo restaurante tenha dois perfis com o mesmo nome.
        if (tipoUsuarioExistente != null && tipoUsuarioExistente.getRestaurante().getId().equals(restaurante.getId())) {
            throw new NotificacaoException(MensagemUtil.TIPO_USUARIO_JA_CADASTRADO);
        }

        // 5. Criação: Utiliza a Factory de domínio para montar o objeto e persiste via repositório.
        // O id do usuário logado é passado para fins de auditoria/validação na factory.
        return this.tipoUsuariorepository.criar(TipoUsuarioFactory.novo(
                dto.nome(),
                dto.descricao(),
                dto.isDono(),
                restaurante,
                usrLogado.getId()));
    }

}