package com.postech.adjt.api.payload.restaurante;

import java.util.Map;

import com.postech.adjt.api.payload.EnderecoPayLoad;
import com.postech.adjt.domain.enums.TipoCozinhaEnum;

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

    @NotNull(message = "O horário de funcionamento não pode estar em branco")
    protected Map<String, DiaFuncionamentoPayload> horarioFuncionamento;

    @NotNull(message = "O tipo de cozinha não pode ser nulo")
    protected TipoCozinhaEnum tipoCozinha;
    
    @NotNull(message = "O endereço não pode ser nulo")
    protected EnderecoPayLoad endereco;
}
