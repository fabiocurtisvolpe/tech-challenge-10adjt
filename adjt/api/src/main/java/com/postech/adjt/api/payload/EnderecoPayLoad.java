package com.postech.adjt.api.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoPayLoad {

    @NotBlank(message = "Logradouro não pode estar em branco")
    private String logradouro;

    @Size(max = 5, message = "Número deve ter até 5 caracteres")
    private String numero;

    private String complemento;

    @NotBlank(message = "Bairro não pode estar em branco")
    private String bairro;

    private String pontoReferencia;

    @NotBlank(message = "CEP não pode estar em branco")
    private String cep;

    @NotBlank(message = "Município não pode estar em branco")
    private String municipio;

    @NotBlank(message = "UF não pode estar em branco")
    @Size(min = 2, max = 2, message = "UF deve ter 2 caracteres")
    private String uf;

    private Boolean principal = false;
}
