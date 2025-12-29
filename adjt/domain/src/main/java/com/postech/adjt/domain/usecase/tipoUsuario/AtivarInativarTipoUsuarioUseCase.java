package com.postech.adjt.domain.usecase.tipoUsuario;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.TipoUsuarioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import com.postech.adjt.domain.usecase.util.UsuarioLogadoUtil;

/**
 * Caso de Uso responsável por ativar ou inativar um Tipo de Usuário (Perfil).
 * <p>
 * Este componente gerencia o ciclo de vida dos perfis, garantindo que perfis
 * marcados como "não editáveis" (geralmente perfis mestres do sistema)
 * tenham seu status preservado.
 */
public class AtivarInativarTipoUsuarioUseCase {

    private final GenericRepositoryPort<TipoUsuario> tipoUsuariorepository;
    private final GenericRepositoryPort<Usuario> usuarioRepository;

    /**
     * Construtor privado para controle de instância.
     */
    private AtivarInativarTipoUsuarioUseCase(GenericRepositoryPort<TipoUsuario> tipoUsuariorepository,
                                             GenericRepositoryPort<Usuario> usuarioRepository) {
        this.tipoUsuariorepository = tipoUsuariorepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Factory method para criar a instância do Caso de Uso.
     *
     * @param tipoUsuariorepository Porta para o repositório de perfis.
     * @param usuarioRepository      Porta para o repositório de usuários.
     * @return Uma nova instância de {@link AtivarInativarTipoUsuarioUseCase}.
     */
    public static AtivarInativarTipoUsuarioUseCase create(GenericRepositoryPort<TipoUsuario> tipoUsuariorepository,
                                                          GenericRepositoryPort<Usuario> usuarioRepository) {
        return new AtivarInativarTipoUsuarioUseCase(tipoUsuariorepository, usuarioRepository);
    }

    /**
     * Executa a lógica de ativação ou inativação de um perfil.
     *
     * @param ativar         Status desejado (true para ativo, false para inativo).
     * @param id             ID do perfil a ser modificado.
     * @param usuarioLogado  E-mail do usuário autenticado solicitante.
     * @return               A entidade {@link TipoUsuario} com o status atualizado.
     * @throws NotificacaoException Caso o perfil não exista ou seja um perfil de sistema não editável.
     */
    public TipoUsuario run(Boolean ativar, Integer id, String usuarioLogado) {

        // 1. Localização: Busca o perfil alvo pelo identificador
        final TipoUsuario tipoUsuario = this.tipoUsuariorepository.obterPorId(id)
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.TIPO_USUARIO_NAO_ENCONTRADO));

        // 2. Proteção de Sistema: Verifica se o perfil permite edição.
        // Perfis configurados como 'isEditavel = false' são protegidos contra desativação.
        if (!tipoUsuario.getIsEditavel()) {
            throw new NotificacaoException(MensagemUtil.NAO_FOI_POSSIVEL_EXECUTAR_OPERACAO);
        }

        // 3. Identificação: Resolve a entidade do usuário que está operando o sistema
        final Usuario usrLogado = UsuarioLogadoUtil.usuarioLogado(usuarioRepository, usuarioLogado);

        // 4. Atualização: Reconstrói a entidade via Factory preservando os dados
        // e aplicando o novo status de ativação.
        return this.tipoUsuariorepository.atualizar(TipoUsuarioFactory.tipoUsuario(
                tipoUsuario.getId(),
                tipoUsuario.getNome(),
                tipoUsuario.getDescricao(),
                ativar, // Novo status
                tipoUsuario.getIsDono(),
                tipoUsuario.getRestaurante(),
                usrLogado.getId()));
    }
}