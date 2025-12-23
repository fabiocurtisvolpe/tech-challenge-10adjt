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

/**
 * Caso de Uso responsável pelo cadastramento de novos usuários no sistema.
 * <p>
 * Este componente coordena a verificação de duplicidade, validação de dependências
 * (como o Tipo de Usuário) e a criação das entidades de domínio necessárias
 * (Usuário e Endereços).
 */
public class CadastrarUsuarioUseCase {

    private final GenericRepositoryPort<Usuario> usuarioRepository;
    private final GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository;

    /**
     * Construtor privado para assegurar o uso do método factory.
     */
    private CadastrarUsuarioUseCase(GenericRepositoryPort<Usuario> usuarioRepository,
                                    GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    /**
     * Factory method para instanciar o Caso de Uso.
     *
     * @param usuarioRepository     Porta para o repositório de usuários.
     * @param tipoUsuarioRepository Porta para o repositório de tipos de usuário.
     * @return Uma nova instância de {@link CadastrarUsuarioUseCase}.
     */
    public static CadastrarUsuarioUseCase create(GenericRepositoryPort<Usuario> usuarioRepository,
                                                 GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository) {
        return new CadastrarUsuarioUseCase(usuarioRepository, tipoUsuarioRepository);
    }

    /**
     * Executa o fluxo de negócio para cadastrar um novo usuário.
     *
     * @param dto Objeto de transferência contendo os dados do novo usuário.
     * @return    A entidade {@link Usuario} persistida.
     * @throws NotificacaoException Caso o e-mail já esteja cadastrado ou o
     *                              tipo de usuário informado não exista.
     */
    public Usuario run(UsuarioDTO dto) {

        // 1. Regra de Negócio: Impede cadastros duplicados com o mesmo e-mail
        if (this.usuarioRepository.obterPorEmail(dto.email()).isPresent()) {
            throw new NotificacaoException(MensagemUtil.USUARIO_EXISTENTE);
        }

        // 2. Integridade Referencial: Valida se o perfil (TipoUsuario) atribuído existe
        final TipoUsuario tipoUsuario = this.tipoUsuarioRepository.obterPorId(dto.tipoUsuario().id())
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.TIPO_USUARIO_NAO_ENCONTRADO));

        // 3. Transformação: Converte a lista de DTOs de endereço em entidades de domínio
        List<Endereco> enderecos = EnderecoFactory.toEnderecoList(dto.enderecos());

        // 4. Criação e Persistência: Utiliza a Factory de domínio para criar o objeto rico
        // e o envia para o repositório através da porta.
        return usuarioRepository.criar(UsuarioFactory.novo(dto.nome(),
                dto.email(), dto.senha(),
                tipoUsuario, enderecos));
    }

}