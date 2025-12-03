package com.postech.adjt.domain.validators;

import java.util.Arrays;

import org.apache.commons.validator.routines.EmailValidator;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.enums.TipoUsuarioEnum;
import com.postech.adjt.domain.exception.NotificacaoException;

/**
 * Validador de regras de negócio para usuários
 * 
 * Centraliza todas as validações de usuário para
 * facilitar reutilização e testes
 * 
 * @author Fabio
 * @since 2025-12-03
 */
public class UsuarioValidator {
    
    private static final int NOME_MINIMO_LENGTH = 3;
    private static final int SENHA_MINIMA_LENGTH = 6;

    /**
     * Valida usuário para criação
     * 
     * @param usuario Usuario a ser validado
     * @throws NotificacaoException se alguma validação falhar
     */
    public static void validarParaCriacao(Usuario usuario) throws NotificacaoException {
        if (usuario == null) {
            throw new NotificacaoException("Usuário não pode ser nulo");
        }

        validarNome(usuario.getNome());
        validarFormatoEmail(usuario.getEmail());
        validarSenha(usuario.getSenha());
        validarTipoUsuario(usuario.getTipoUsuario());
    }

    /**
     * Valida usuário para atualização
     * 
     * @param usuario Usuario a ser validado
     * @throws NotificacaoException se alguma validação falhar
     */
    public static void validarParaAtualizacao(Usuario usuario) throws NotificacaoException {
        if (usuario == null) {
            throw new NotificacaoException("Usuário não pode ser nulo");
        }

        validarId(usuario.getId());
        validarNome(usuario.getNome());
        validarFormatoEmail(usuario.getEmail());
    }

    /**
     * Valida ID do usuário
     * 
     * @param id ID a ser validado
     * @throws NotificacaoException se ID for nulo ou menor/igual a zero
     */
    private static void validarId(Integer id) throws NotificacaoException {
        if (id == null) {
            throw new NotificacaoException("ID do usuário não pode ser nulo");
        }

        if (id <= 0) {
            throw new NotificacaoException("ID do usuário deve ser maior que zero");
        }
    }

    /**
     * Valida nome do usuário
     * 
     * @param nome Nome a ser validado
     * @throws NotificacaoException se nome for nulo, vazio ou menor que o mínimo
     */
    private static void validarNome(String nome) throws NotificacaoException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new NotificacaoException(MensagemUtil.NOME_EM_BRANCO);
        }

        if (nome.trim().length() < NOME_MINIMO_LENGTH) {
            throw new NotificacaoException("Nome deve ter no mínimo " + NOME_MINIMO_LENGTH + " caracteres");
        }
    }

    /**
     * Valida senha do usuário
     * 
     * @param senha Senha a ser validada
     * @throws NotificacaoException se senha for nula, vazia ou menor que o mínimo
     */
    private static void validarSenha(String senha) throws NotificacaoException {
        if (senha == null || senha.isEmpty()) {
            throw new NotificacaoException(MensagemUtil.SENHA_EM_BRANCO);
        }

        if (senha.length() < SENHA_MINIMA_LENGTH) {
            throw new NotificacaoException("Senha deve ter no mínimo " + SENHA_MINIMA_LENGTH + " caracteres");
        }
    }

    /**
     * Valida formato do email
     * 
     * @param email Email a ser validado
     * @throws NotificacaoException se email for nulo, vazio ou em formato inválido
     */
    private static void validarFormatoEmail(String email) throws NotificacaoException {
        if (email == null || email.trim().isEmpty()) {
            throw new NotificacaoException(MensagemUtil.EMAIL_NULO);
        }

        EmailValidator emailValidator = EmailValidator.getInstance();

        if (!emailValidator.isValid(email)) {
            throw new NotificacaoException(MensagemUtil.EMAIL_INVALIDO);
        }
    }

    /**
     * Valida tipo de usuário
     * 
     * @param tipoUsuario Tipo de usuário a ser validado
     * @throws NotificacaoException se tipo for nulo ou inválido
     */
    private static void validarTipoUsuario(TipoUsuarioEnum tipoUsuario) throws NotificacaoException {
        if (tipoUsuario == null) {
            throw new NotificacaoException(MensagemUtil.TIPO_USUARIO_NULO);
        }

        boolean existe = Arrays.stream(TipoUsuarioEnum.values())
                .anyMatch(tp -> tp.equals(tipoUsuario));

        if (!existe) {
            throw new NotificacaoException(MensagemUtil.TIPO_USUARIO_NAO_ENCONTRADO);
        }
    }
}
