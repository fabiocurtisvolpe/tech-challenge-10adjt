package com.postech.adjt.domain.usecase.usuario;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.TipoUsuarioDonoRestaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.UsuarioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import com.postech.adjt.domain.usecase.util.UsuarioLogadoUtil;

/**
 * Caso de Uso responsável por ativar ou inativar um usuário no sistema.
 * <p>
 * Este componente orquestra a alteração do campo 'ativo', aplicando regras de
 * autorização que permitem a operação apenas se o solicitante for o próprio
 * usuário ou um administrador (Dono de Restaurante).
 */
public class AtivarInativarUsuarioUseCase {

    /** Porta de saída para operações de persistência de Usuário. */
    private final GenericRepositoryPort<Usuario> usuarioRepository;

    /**
     * Construtor privado para controle de instância via Factory Method.
     */
    private AtivarInativarUsuarioUseCase(GenericRepositoryPort<Usuario> usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Factory method para instanciar o Caso de Uso.
     *
     * @param usuarioRepository Implementação da porta de repositório.
     * @return Uma nova instância de {@link AtivarInativarUsuarioUseCase}.
     */
    public static AtivarInativarUsuarioUseCase create(GenericRepositoryPort<Usuario> usuarioRepository) {
        return new AtivarInativarUsuarioUseCase(usuarioRepository);
    }

    /**
     * Executa a lógica de ativação/inativação com verificação de permissões.
     *
     * @param ativar         Status booleano desejado (true para ativar, false para inativar).
     * @param id             O identificador único do usuário alvo da operação.
     * @param usuarioLogado  O e-mail do usuário que está solicitando a operação (contexto de segurança).
     * @return               A entidade {@link Usuario} atualizada.
     * @throws NotificacaoException Caso o usuário alvo ou o logado não existam, ou se o
     *                              solicitante não tiver permissão para a operação.
     */
    public Usuario run(Boolean ativar, Integer id, String usuarioLogado) {

        // 1. Localização: Busca o usuário alvo pelo ID
        final Usuario usuario = this.usuarioRepository.obterPorId(id)
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO));

        // 2. Contexto: Recupera a entidade do usuário que está operando o sistema
        final Usuario usrLogado = UsuarioLogadoUtil.usuarioLogado(usuarioRepository, usuarioLogado);

        // 3. Regra de Autorização:
        // A operação é permitida se:
        // - O solicitante é um Dono de Restaurante (perfil administrativo) OR
        // - O solicitante é o próprio dono da conta que está sendo alterada.
        if ((usrLogado.getTipoUsuario() instanceof TipoUsuarioDonoRestaurante)
                || (usrLogado.getId().equals(usuario.getId()))) {

            // 4. Reconstrução: Utiliza a Factory para criar uma nova instância da entidade
            // com os dados preservados, alterando apenas o campo 'ativo'.
            final Usuario usuarioAtualizado = UsuarioFactory.usuario(
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getSenha(),
                    usuario.getTipoUsuario(),
                    usuario.getEnderecos(),
                    ativar
            );

            // 5. Persistência: Envia a entidade atualizada para o adaptador de banco de dados.
            return usuarioRepository.atualizar(usuarioAtualizado);
        }

        // Caso não atenda aos requisitos de segurança, lança exceção de permissão.
        throw new NotificacaoException(MensagemUtil.USUARIO_NAO_PERMITE_OPERACAO);
    }
}