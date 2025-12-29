package com.postech.adjt.domain.usecase.usuario;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.TipoUsuarioDonoRestaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

/**
 * Caso de Uso responsável por obter os detalhes de um usuário pelo seu ID.
 * <p>
 * Este Caso de Uso implementa uma regra de segurança onde apenas usuários
 * com perfil de "Dono de Restaurante" podem consultar detalhes de outros usuários
 * via ID.
 */
public class ObterUsuarioPorIdUseCase {

    /** Porta de saída para operações de persistência de Usuário. */
    private final GenericRepositoryPort<Usuario> usuarioRepository;

    /**
     * Construtor privado para controle de instância.
     */
    private ObterUsuarioPorIdUseCase(GenericRepositoryPort<Usuario> usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Factory method para criação da instância do Caso de Uso.
     *
     * @param usuarioRepository Implementação da porta de repositório.
     * @return Uma nova instância de {@link ObterUsuarioPorIdUseCase}.
     */
    public static ObterUsuarioPorIdUseCase create(GenericRepositoryPort<Usuario> usuarioRepository) {
        return new ObterUsuarioPorIdUseCase(usuarioRepository);
    }

    /**
     * Executa a lógica de busca de usuário por ID com validação de permissão.
     *
     * @param id             O identificador único do usuário a ser buscado.
     * @param usuarioLogado  O e-mail do usuário que está realizando a consulta (contexto de segurança).
     * @return               A entidade {@link Usuario} encontrada.
     * @throws NotificacaoException Caso o ID seja inválido, o solicitante não tenha permissão,
     *                              ou o usuário alvo não seja encontrado.
     */
    public Usuario run(Integer id, String usuarioLogado) {

        // 1. Validação técnica do parâmetro ID
        if (id == null || id <= 0) {
            throw new NotificacaoException(MensagemUtil.ID_NULO);
        }

        // 2. Validação de segurança: Recupera quem está pedindo a informação
        Usuario usuarioDono = this.usuarioRepository.obterPorEmail(usuarioLogado)
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO));

        // 3. Regra de Negócio: Verifica se o solicitante possui perfil de "Dono de Restaurante"
        if (!(usuarioDono.getTipoUsuario() instanceof TipoUsuarioDonoRestaurante)) {
            throw new NotificacaoException(MensagemUtil.USUARIO_NAO_PERMITE_OPERACAO);
        }

        // 4. Realiza a busca final do usuário alvo pelo ID
        return this.usuarioRepository.obterPorId(id)
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO));
    }
}