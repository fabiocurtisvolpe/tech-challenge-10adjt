package com.postech.adjt.domain.usecase.tipoUsuario;

import java.util.Objects;
import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import com.postech.adjt.domain.usecase.util.UsuarioLogadoUtil;

/**
 * Caso de Uso responsável por buscar um Tipo de Usuário (Perfil) pelo seu ID.
 * <p>
 * Este componente implementa uma camada de segurança multitenant: se o Tipo de Usuário
 * estiver vinculado a um restaurante, apenas o dono desse restaurante tem permissão
 * para visualizar os detalhes do perfil.
 */
public class ObterTipoUsuarioPorIdUseCase {

    private final GenericRepositoryPort<TipoUsuario> tipoUsuariorepository;
    private final GenericRepositoryPort<Usuario> usuarioRepository;

    /**
     * Construtor privado para assegurar o uso do método factory.
     */
    private ObterTipoUsuarioPorIdUseCase(GenericRepositoryPort<TipoUsuario> tipoUsuariorepository,
                                         GenericRepositoryPort<Usuario> usuarioRepository) {
        this.tipoUsuariorepository = tipoUsuariorepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Factory method para instanciar o Caso de Uso.
     *
     * @param tipoUsuariorepository Porta para o repositório de tipos de usuário.
     * @param usuarioRepository      Porta para o repositório de usuários.
     * @return Uma nova instância de {@link ObterTipoUsuarioPorIdUseCase}.
     */
    public static ObterTipoUsuarioPorIdUseCase create(GenericRepositoryPort<TipoUsuario> tipoUsuariorepository,
                                                      GenericRepositoryPort<Usuario> usuarioRepository) {
        return new ObterTipoUsuarioPorIdUseCase(tipoUsuariorepository, usuarioRepository);
    }

    /**
     * Executa a busca do Tipo de Usuário com validação de propriedade.
     *
     * @param id             O identificador único do Tipo de Usuário.
     * @param usuarioLogado  O e-mail do usuário autenticado que realiza a consulta.
     * @return               A entidade {@link TipoUsuario} encontrada.
     * @throws NotificacaoException Caso o ID seja inválido, o perfil não exista ou
     *                              o solicitante não seja o dono do restaurante vinculado.
     */
    public TipoUsuario run(Integer id, String usuarioLogado) {

        // 1. Validação técnica do parâmetro ID
        if (id == null || id <= 0) {
            throw new NotificacaoException(MensagemUtil.ID_NULO);
        }

        // 2. Busca: Localiza o perfil alvo no repositório
        TipoUsuario tipoUsuario = this.tipoUsuariorepository.obterPorId(id)
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.TIPO_USUARIO_NAO_ENCONTRADO));

        // 3. Identificação: Resolve a entidade do usuário que está logado
        final Usuario usrLogado = UsuarioLogadoUtil.usuarioLogado(usuarioRepository, usuarioLogado);

        // 4. Regra de Segurança (Isolamento de Restaurante):
        // Se o TipoUsuario possuir um vínculo com restaurante, verifica se o usuário logado
        // é o dono do restaurante. Isso impede que donos de outros restaurantes vejam
        // configurações de perfis que não lhes pertencem.
        if (Objects.nonNull(tipoUsuario.getRestaurante())) {
            if (!tipoUsuario.getRestaurante().getDono().getId().equals(usrLogado.getId())) {
                throw new NotificacaoException(MensagemUtil.USUARIO_NAO_PERMITE_OPERACAO);
            }
        }

        return tipoUsuario;
    }
}