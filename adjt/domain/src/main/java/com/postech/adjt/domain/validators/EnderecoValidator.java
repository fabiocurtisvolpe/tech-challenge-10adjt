package com.postech.adjt.domain.validators;

import org.apache.commons.validator.routines.RegexValidator;

public class EnderecoValidator {

    private static final RegexValidator CEP_VALIDATOR = new RegexValidator("^[0-9]{5}-?[0-9]{3}$");

    public static void validarCep(String cep) {
        if (cep == null || !CEP_VALIDATOR.isValid(cep)) {
            throw new IllegalArgumentException("CEP inválido");
        }
    }
}
