package com.postech.adjt.domain.usecase.usuario;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

/**
 * Caso de Uso responsável por localizar e retornar um usuário através do e-mail.
 * <p>
 * Atua como uma operação de consulta na camada de domínio, garantindo que o
 * e-mail fornecido seja válido sintaticamente (não nulo/vazio) e que o registro
 * exista na base de dados.
 */
public class ObterUsuarioPorEmailUseCase {

    /** Porta de saída para operações de persistência e consulta de Usuário. */
    private final GenericRepositoryPort<Usuario> usuarioRepository;

    /**
     * Construtor privado para controle de instância.
     */
    private ObterUsuarioPorEmailUseCase(GenericRepositoryPort<Usuario> usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Método de fábrica (Factory Method) para criar uma instância do Caso de Uso.
     *
     * @param usuarioRepository Implementação da porta de repositório genérica.
     * @return Uma nova instância de {@link ObterUsuarioPorEmailUseCase}.
     */
    public static ObterUsuarioPorEmailUseCase create(GenericRepositoryPort<Usuario> usuarioRepository) {
        return new ObterUsuarioPorEmailUseCase(usuarioRepository);
    }

    /**
     * Executa a lógica de busca de usuário por e-mail.
     *
     * @param email O endereço de e-mail do usuário a ser buscado.
     * @return      A entidade {@link Usuario} caso seja encontrada.
     * @throws NotificacaoException Caso o e-mail informado seja nulo, vazio ou
     *                              não corresponda a nenhum usuário cadastrado.
     */
    public Usuario run(String email) {

        // 1. Validação de integridade do parâmetro de entrada
        if (email == null || email.trim().isEmpty()) {
            throw new NotificacaoException(MensagemUtil.EMAIL_NULO);
        }

        // 2. Consulta ao repositório através da porta (Port/Adapter)
        // Lança exceção de domínio caso o Optional retornado pelo repositório esteja vazio
        return this.usuarioRepository.obterPorEmail(email)
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO));
    }
}