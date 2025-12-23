package com.postech.adjt.domain.validators;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.constants.TamanhoUtil;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.exception.NotificacaoException;

/**
 * Validator de domínio para a entidade {@link TipoUsuario}.
 * <p>
 * Centraliza as regras de validação para a criação e atualização de tipos de usuários,
 * incluindo restrições de tamanho de campos e validação de permissão de acesso baseada
 * na propriedade do restaurante.
 */
public class TipoUsuarioValidator {

    /**
     * Valida os atributos da entidade TipoUsuario e verifica a autorização do usuário logado.
     *
     * @param tipoUsuario      A entidade TipoUsuario a ser validada.
     * @param idUsuarioLogado  O ‘ID’ do utilizador que está a tentar executar a ação.
     * @throws NotificacaoException Caso alguma regra de negócio, limite de caracteres
     *                              ou validação de segurança seja violada.
     */
    public static void validar(TipoUsuario tipoUsuario, Integer idUsuarioLogado) throws NotificacaoException {

        // 1. Validação de existência do objeto
        if (tipoUsuario == null) {
            throw new NotificacaoException(MensagemUtil.TIPO_USUARIO_NULO_VALIDACAO);
        }

        // 2. Validação do Nome (Obrigatoriedade e Comprimento)
        if (tipoUsuario.getNome() == null || tipoUsuario.getNome().trim().isEmpty()) {
            throw new NotificacaoException(MensagemUtil.NOME_EM_BRANCO);
        }

        if (tipoUsuario.getNome().length() > TamanhoUtil.NOME_MAXIMO_LENGTH) {
            throw new NotificacaoException(MensagemUtil.NOME_MAXIMO_CARACTERES);
        }

        if (tipoUsuario.getNome().length() < TamanhoUtil.NOME_MINIMO_LENGTH) {
            throw new NotificacaoException(MensagemUtil.NOME_MINIMO_CARACTERES);
        }

        // 3. Validação da Descrição (Opcional, com limite de tamanho)
        if (tipoUsuario.getDescricao() != null && tipoUsuario.getDescricao().length() > TamanhoUtil.DESCRICAO_MAXIMA_LENGTH) {
            throw new NotificacaoException(MensagemUtil.DESCRICAO_MAXIMO_CARACTERES);
        }

        // 4. Validação de Vínculo com Restaurante
        if (tipoUsuario.getRestaurante() == null) {
            throw new NotificacaoException(MensagemUtil.RESTAURANTE_NULO);
        }

        // 5. Validação de Autorização (Regra de Negócio)
        // Somente o dono do restaurante vinculado ao TipoUsuario pode gerenciar este perfil.
        if (!tipoUsuario.getRestaurante().getDono().getId().equals(idUsuarioLogado)) {
            throw new NotificacaoException(MensagemUtil.USUARIO_NAO_PERMITE_OPERACAO);
        }
    }
}