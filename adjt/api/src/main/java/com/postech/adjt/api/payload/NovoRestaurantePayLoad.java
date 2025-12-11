package com.postech.adjt.api.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NovoRestaurantePayLoad {

    @NotBlank(message = "O nome não pode estar em branco")
    @Size(max = 50, message = "O nome deve ter até 50 caracteres")
    protected String nome;

    @Size(max = 1000, message = "A descrição deve ter até 1000 caracteres")
    protected String descricao;

    @NotBlank(message = "O horário de funcionamento não pode estar em branco")
    protected String horarioFuncionamento;

    @NotNull(message = "O tipo de cozinha não pode ser nulo")
    protected TipoCozinhaPayLoad tipoCozinha;
    
    @NotNull(message = "O endereço não pode ser nulo")
    protected EnderecoPayLoad endereco;
}
