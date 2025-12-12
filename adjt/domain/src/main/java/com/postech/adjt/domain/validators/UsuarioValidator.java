package com.postech.adjt.domain.validators;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.EmailValidator;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.constants.TamanhoUtil;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.Usuario;
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

    private static final String CEP_REGEX = "^\\d{5}-?\\d{3}$";
    private static final Pattern CEP_PATTERN = Pattern.compile(CEP_REGEX);

    /**
     * Valida usuário para criação
     * 
     * @param usuario Usuario a ser validado
     * @throws NotificacaoException se alguma validação falhar
     */
    public static void validarParaCriacao(Usuario usuario) throws NotificacaoException {

        if (usuario == null) {
            throw new NotificacaoException(MensagemUtil.USUARIO_NULO);
        }

        validarNome(usuario.getNome());
        validarFormatoEmail(usuario.getEmail());
        validarSenha(usuario.getSenha());
        validarTipoUsuario(usuario.getTipoUsuario());
        validarEndereco(usuario.getEnderecos());

        usuario.getEnderecos().forEach(endereco -> {
            EnderecoValidator.validarCep(endereco.getCep());
        });
    }

    /**
     * Valida usuário para atualização
     * 
     * @param usuario Usuario a ser validado
     * @throws NotificacaoException se alguma validação falhar
     */
    public static void validarParaAtualizacao(Usuario usuario) throws NotificacaoException {
        if (usuario == null) {
            throw new NotificacaoException(MensagemUtil.USUARIO_NULO);
        }

        validarId(usuario.getId());
        validarNome(usuario.getNome());
        validarFormatoEmail(usuario.getEmail());
        validarTipoUsuario(usuario.getTipoUsuario());
        validarEndereco(usuario.getEnderecos());

        usuario.getEnderecos().forEach(endereco -> {
            EnderecoValidator.validarCep(endereco.getCep());
        });
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

        if (nome.length() < TamanhoUtil.NOME_MINIMO_LENGTH) {
            throw new NotificacaoException(MensagemUtil.NOME_MINIMO_CARACTERES);
        }

        if (nome.length() > TamanhoUtil.NOME_MAXIMO_LENGTH) {
            throw new NotificacaoException(MensagemUtil.NOME_MAXIMO_CARACTERES);
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

        if (senha.length() < TamanhoUtil.SENHA_MINIMA_LENGTH) {
            throw new NotificacaoException(MensagemUtil.SENHA_MINIMO_CARACTERES);
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
    private static void validarTipoUsuario(TipoUsuario tipoUsuario) throws NotificacaoException {
        if (tipoUsuario == null) {
            throw new NotificacaoException(MensagemUtil.TIPO_USUARIO_NULO);
        }
    }

    private static boolean isStringVazia(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    private static void validarCep(String cep) throws NotificacaoException {
        if (isStringVazia(cep)) {
            throw new NotificacaoException("O CEP é obrigatório.");
        }

        if (!CEP_PATTERN.matcher(cep).matches()) {
            throw new NotificacaoException(
                    "O CEP informado (" + cep + ") é inválido. Formatos aceitos: 12345-678 ou 12345678.");
        }
    }

    private static void validarEndereco(List<Endereco> enderecos) {
        if (enderecos == null || enderecos.isEmpty()) {
            throw new IllegalArgumentException(MensagemUtil.ENDERECO_EM_BRANCO);
        }

        for (Endereco endereco : enderecos) {

            if (endereco == null) {
                throw new NotificacaoException("O endereço não pode ser nulo.");
            }

            if (isStringVazia(endereco.getLogradouro())) {
                throw new NotificacaoException("O logradouro é obrigatório.");
            }

            if (isStringVazia(endereco.getBairro())) {
                throw new NotificacaoException("O bairro é obrigatório.");
            }

            if (isStringVazia(endereco.getMunicipio())) {
                throw new NotificacaoException("O município é obrigatório.");
            }

            if (isStringVazia(endereco.getUf())) {
                throw new NotificacaoException("A UF é obrigatória.");
            }

            if (endereco.getUf().length() != 2) {
                throw new NotificacaoException("A UF deve conter exatamente 2 letras.");
            }

            if (endereco.getPrincipal() == null) {
                throw new NotificacaoException("A definição se o endereço é principal é obrigatória.");
            }

            validarCep(endereco.getCep());

        }

    }
}
