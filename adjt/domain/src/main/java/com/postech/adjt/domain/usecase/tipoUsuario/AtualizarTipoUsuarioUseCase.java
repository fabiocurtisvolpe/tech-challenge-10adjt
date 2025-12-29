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

import java.util.Objects;

/**
 * Caso de Uso responsável pela atualização de um Tipo de Usuário (Perfil) existente.
 * <p>
 * Este componente valida a existência do perfil, a permissão de edição (perfis editáveis),
 * a integridade do vínculo com o restaurante e evita a duplicidade de nomes no mesmo contexto.
 */
public class AtualizarTipoUsuarioUseCase {

    private final GenericRepositoryPort<TipoUsuario> tipoUsuariorepository;
    private final GenericRepositoryPort<Restaurante> restauranteRepository;
    private final GenericRepositoryPort<Usuario> usuarioRepository;

    /**
     * Construtor privado para assegurar o uso do método factory.
     */
    private AtualizarTipoUsuarioUseCase(GenericRepositoryPort<TipoUsuario> tipoUsuariorepository,
                                        GenericRepositoryPort<Usuario> usuarioRepository,
                                        GenericRepositoryPort<Restaurante> restauranteRepository) {
        this.tipoUsuariorepository = tipoUsuariorepository;
        this.usuarioRepository = usuarioRepository;
        this.restauranteRepository = restauranteRepository;
    }

    /**
     * Factory method para instanciar o Caso de Uso.
     *
     * @param tipoUsuariorepository  Porta para o repositório de tipos de usuário.
     * @param usuarioRepository      Porta para o repositório de usuários.
     * @param restauranteRepository  Porta para o repositório de restaurantes.
     * @return Uma nova instância de {@link AtualizarTipoUsuarioUseCase}.
     */
    public static AtualizarTipoUsuarioUseCase create(GenericRepositoryPort<TipoUsuario> tipoUsuariorepository,
                                                     GenericRepositoryPort<Usuario> usuarioRepository,
                                                     GenericRepositoryPort<Restaurante> restauranteRepository) {
        return new AtualizarTipoUsuarioUseCase(tipoUsuariorepository, usuarioRepository, restauranteRepository);
    }

    /**
     * Executa o fluxo de atualização do perfil.
     *
     * @param dto            DTO contendo os novos dados do perfil.
     * @param usuarioLogado  E-mail do usuário autenticado solicitante.
     * @return               A entidade {@link TipoUsuario} atualizada e persistida.
     * @throws NotificacaoException Caso o perfil não exista, não seja editável,
     *                              ou o novo nome já pertença a outro perfil do mesmo restaurante.
     */
    public TipoUsuario run(TipoUsuarioDTO dto, String usuarioLogado) {

        // 1. Validação de ID
        if (Objects.isNull(dto.id())) {
            throw new NotificacaoException(MensagemUtil.ID_NULO);
        }

        // 2. Busca o perfil atual
        final TipoUsuario tipoUsuario = this.tipoUsuariorepository.obterPorId(dto.id())
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.TIPO_USUARIO_NAO_ENCONTRADO));

        // 3. Resolve identidade do solicitante
        final Usuario usrLogado = UsuarioLogadoUtil.usuarioLogado(usuarioRepository, usuarioLogado);

        // 4. Validação de vínculo com Restaurante
        if (Objects.isNull(dto.restaurante()) || Objects.isNull(dto.restaurante().id())) {
            throw new NotificacaoException(MensagemUtil.RESTAURANTE_OBRIGATORIO);
        }

        final Restaurante restaurante = this.restauranteRepository.obterPorId(dto.restaurante().id())
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.RESTAURANTE_NAO_ENCONTRADO));

        // 5. Regra de Segurança: Verifica se o perfil é editável (Ex: perfis de sistema são bloqueados)
        if (!tipoUsuario.getIsEditavel()) {
            throw new NotificacaoException(MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        }

        // 6. Regra de Unicidade: Verifica se o novo nome já existe para OUTRO ID no mesmo restaurante
        final TipoUsuario tipoUsuarioNome = this.tipoUsuariorepository.obterPorNome(dto.nome()).orElse(null);
        if (tipoUsuarioNome != null
                && !tipoUsuarioNome.getId().equals(tipoUsuario.getId())
                && tipoUsuario.getRestaurante().getId().equals(tipoUsuarioNome.getRestaurante().getId())) {

            throw new NotificacaoException(MensagemUtil.TIPO_USUARIO_JA_CADASTRADO);
        }

        // 7. Atualização via Factory e Repositório
        return this.tipoUsuariorepository.atualizar(
                TipoUsuarioFactory.tipoUsuario(tipoUsuario.getId(),
                        tipoUsuario.getNome(), tipoUsuario.getDescricao(),
                        true, dto.isDono(), restaurante, usrLogado.getId()));
    }
}