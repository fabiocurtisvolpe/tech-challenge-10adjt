package com.postech.adjt.domain.usecase.usuario;

import java.util.List;
import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.UsuarioDTO;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.EnderecoFactory;
import com.postech.adjt.domain.factory.UsuarioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import com.postech.adjt.domain.usecase.util.UsuarioLogadoUtil;

/**
 * Caso de Uso responsável pela atualização dos dados de um usuário existente.
 * <p>
 * Este componente orquestra a validação do usuário logado, a verificação de
 * integridade do novo Tipo de Usuário e a atualização das informações cadastrais
 * e de localização.
 */
public class AtualizarUsuarioUseCase {

    private final GenericRepositoryPort<Usuario> usuarioRepository;
    private final GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository;

    /**
     * Construtor privado para assegurar o uso do método factory.
     */
    private AtualizarUsuarioUseCase(GenericRepositoryPort<Usuario> usuarioRepository,
                                    GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    /**
     * Factory method para instanciar o Caso de Uso.
     *
     * @param usuarioRepository     Porta para o repositório de usuários.
     * @param tipoUsuarioRepository Porta para o repositório de tipos de usuário.
     * @return Uma nova instância de {@link AtualizarUsuarioUseCase}.
     */
    public static AtualizarUsuarioUseCase create(GenericRepositoryPort<Usuario> usuarioRepository,
                                                 GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository) {
        return new AtualizarUsuarioUseCase(usuarioRepository, tipoUsuarioRepository);
    }

    /**
     * Executa o fluxo de negócio para atualizar os dados de um usuário.
     *
     * @param dto            Objeto de transferência com os novos dados do usuário.
     * @param usuarioLogado  E-mail do usuário autenticado que está realizando a operação.
     * @return               A entidade {@link Usuario} atualizada e persistida.
     * @throws NotificacaoException Caso o usuário logado não exista, o tipo de usuário seja inválido
     *                              ou ocorra tentativa de violação de identidade.
     */
    public Usuario run(UsuarioDTO dto, String usuarioLogado) {

        // 1. Identificação: Recupera a entidade do usuário que está tentando realizar a alteração
        final Usuario usuarioExistente = UsuarioLogadoUtil.usuarioLogado(usuarioRepository, usuarioLogado);

        // 2. Integridade: Valida se o novo Perfil (TipoUsuario) informado existe no sistema
        final TipoUsuario tipoUsuario = this.tipoUsuarioRepository.obterPorId(dto.tipoUsuario().id())
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.TIPO_USUARIO_NAO_ENCONTRADO));

        // 3. Segurança: Se houver tentativa de alterar o e-mail, valida se a operação é permitida
        if (!dto.email().equals(usuarioLogado)) {
            this.usuarioRepository.obterPorEmail(dto.email())
                    .orElseThrow(() -> new NotificacaoException(MensagemUtil.USUARIO_NAO_PERMITE_OPERACAO));
        }

        // 4. Transformação: Converte a lista de DTOs de endereço para entidades de domínio
        List<Endereco> enderecos = EnderecoFactory.toEnderecoList(dto.enderecos());

        // 5. Atualização: Utiliza a Factory para reconstruir a entidade com os dados atualizados
        // preservando o ID original e a senha atual.
        return usuarioRepository.atualizar(UsuarioFactory.usuario(usuarioExistente.getId(), dto.nome(),
                dto.email(), usuarioExistente.getSenha(),
                tipoUsuario, enderecos, dto.ativo()));
    }
}