package com.postech.adjt.domain.validators;

import java.util.List;
import org.apache.commons.validator.routines.RegexValidator;
import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.exception.NotificacaoException;

/**
 * Validador de domínio para a entidade {@link Endereco}.
 * <p>
 * Garante que endereços individuais ou listas de endereços estejam em conformidade
 * com as regras de negócio antes de persistência, ou processamento.
 */
public class EnderecoValidator {

    /**
     * Regex para CEP brasileiro: suporta "00000000" ou "00000-000".
     */
    private static final RegexValidator CEP_VALIDATOR = new RegexValidator("^[0-9]{5}-?[0-9]{3}$");

    /**
     * Verifica se uma String é nula ou composta apenas por espaços em branco.
     */
    private static boolean isStringVazia(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    /**
     * Valida o formato do CEP.
     *
     * @param cep String representando o código postal.
     * @throws IllegalArgumentException se o formato for inválido ou nulo.
     */
    protected static void validarCep(String cep) {
        if (cep == null || !CEP_VALIDATOR.isValid(cep)) {
            throw new IllegalArgumentException("CEP inválido");
        }
    }

    /**
     * Valida uma lista de endereços. Útil para cadastros de usuários.
     *
     * @param enderecos Lista de objetos {@link Endereco}.
     * @throws IllegalArgumentException se a lista for nula ou vazia.
     */
    public static void validarEnderecoList(List<Endereco> enderecos) {
        if (enderecos == null || enderecos.isEmpty()) {
            throw new IllegalArgumentException(MensagemUtil.ENDERECO_EM_BRANCO);
        }

        for (Endereco endereco : enderecos) {
            validarEndereco(endereco);
        }
    }

    /**
     * Realiza a validação de todos os campos obrigatórios de um endereço único.
     *
     * @param endereco O objeto a ser validado.
     * @throws NotificacaoException se campos obrigatórios estiverem ausentes.
     * @throws IllegalArgumentException se o formato do CEP for inválido.
     */
    public static void validarEndereco(Endereco endereco) {

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