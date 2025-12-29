package com.postech.adjt.domain.usecase.util;

import java.util.Objects;
import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

/**
 * Classe utilitária para operações relacionadas ao contexto do usuário logado.
 * <p>
 * Esta classe auxilia os Casos de Uso a resolverem a identidade do usuário
 * a partir de informações de autenticação (como o e-mail extraído do token JWT).
 */
public class UsuarioLogadoUtil {

    /**
     * Recupera a entidade do usuário logado e valida sua existência.
     * <p>
     * Este método é comumente utilizado em fluxos de atualização ou criação
     * para garantir que o autor da requisição é um usuário válido e ativo no banco de dados.
     *
     * @param usuarioRepository Porta de acesso ao repositório de usuários.
     * @param email             E-mail do usuário autenticado (identificador único).
     * @return                  A entidade {@link Usuario} correspondente ao e-mail fornecido.
     * @throws NotificacaoException Caso o e-mail seja nulo ou o usuário não seja encontrado no sistema.
     */
    public static Usuario usuarioLogado(GenericRepositoryPort<Usuario> usuarioRepository, String email) {

        // 1. Validação de integridade do parâmetro de entrada
        if (Objects.isNull(email)) {
            throw new NotificacaoException(MensagemUtil.EMAIL_NULO);
        }

        // 2. Busca no repositório através da porta (Port/Adapter)
        // Lança exceção de domínio se o identificador não corresponder a um registro real
        return usuarioRepository.obterPorEmail(email)
                .orElseThrow(() -> new NotificacaoException(MensagemUtil.USUARIO_NAO_ENCONTRADO));
    }
}