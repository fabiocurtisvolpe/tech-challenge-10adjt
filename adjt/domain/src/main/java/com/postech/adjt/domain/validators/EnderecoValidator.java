package com.postech.adjt.domain.validators;

import java.util.List;

import org.apache.commons.validator.routines.RegexValidator;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.exception.NotificacaoException;

public class EnderecoValidator {

    private static final RegexValidator CEP_VALIDATOR = new RegexValidator("^[0-9]{5}-?[0-9]{3}$");

    private static boolean isStringVazia(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    protected static void validarCep(String cep) {
        if (cep == null || !CEP_VALIDATOR.isValid(cep)) {
            throw new IllegalArgumentException("CEP inválido");
        }
    }

    public static void validarEnderecoList(List<Endereco> enderecos) {
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
