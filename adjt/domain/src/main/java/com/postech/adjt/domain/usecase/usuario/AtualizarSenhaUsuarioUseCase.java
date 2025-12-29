package com.postech.adjt.domain.usecase.usuario;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.TrocarSenhaUsuarioDTO;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.UsuarioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

/**
 * Caso de Uso responsável pela alteração da senha de acesso do usuário.
 * <p>
 * Este componente isola a lógica de troca de credenciais, garantindo que
 * apenas a senha seja modificada, preservando todos os outros dados cadastrais
 * e relacionamentos do usuário.
 */
public class AtualizarSenhaUsuarioUseCase {

    /** Porta de saída para operações de persistência de Usuário. */
    private final GenericRepositoryPort<Usuario> usuarioRepository;

    /**
     * Construtor privado para assegurar o uso do método factory.
     */
    private AtualizarSenhaUsuarioUseCase(GenericRepositoryPort<Usuario> usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Factory method para instanciar o Caso de Uso.
     *
     * @param usuarioRepository Implementação da porta de repositório de usuários.
     * @return Uma nova instância de {@link AtualizarSenhaUsuarioUseCase}.
     */
    public static AtualizarSenhaUsuarioUseCase create(GenericRepositoryPort<Usuario> usuarioRepository) {
        return new AtualizarSenhaUsuarioUseCase(usuarioRepository);
    }

    /**
     * Executa o fluxo de negócio para atualizar a senha do usuário.
     *
     * @param dto Objeto de transferência contendo o e-mail do usuário e a nova senha já codificada.
     * @return    A entidade {@link Usuario} com a nova credencial persistida.
     * @throws NotificacaoException Caso o usuário associado ao e-mail não seja encontrado.
     */
    public Usuario run(TrocarSenhaUsuarioDTO dto) {

        // 1. Busca: Localiza o usuário alvo pelo e-mail fornecido no DTO
        final Usuario usuario = this.usuarioRepository.obterPorEmail(dto.email())
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO));

        // 2. Atualização: Utiliza a Factory de domínio para reconstruir a entidade
        // injetando a nova senha codificada e mantendo os dados originais (ID, Nome, Tipo, Endereços).
        // Força o status 'ativo' como true.
        return usuarioRepository.atualizar(UsuarioFactory.usuario(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                dto.senhaCodificada(),
                usuario.getTipoUsuario(),
                usuario.getEnderecos(),
                true));
    }
}