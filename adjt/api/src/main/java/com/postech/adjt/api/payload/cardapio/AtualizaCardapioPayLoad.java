package com.postech.adjt.api.payload.cardapio;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AtualizaCardapioPayLoad {

    @NotNull(message = "O id do cardápio não pode estar vazio")
    @Positive(message = "O id deve ser maior que zero")
    private Integer id;

    @NotBlank(message = "O nome não pode estar em branco")
    @Size(max = 50, message = "O nome deve ter até 50 caracteres")
    protected String nome;

    @Size(max = 1000, message = "A descrição deve ter até 1000 caracteres")
    protected String descricao;

    @NotNull(message = "O preço não pode ser vazio/nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que zero")
    protected BigDecimal preco;

    @NotBlank(message = "A foto não pode estar em branco")
    protected String foto;

    @NotNull(message = "O disponível não pode ser vazio/nulo")
    protected Boolean disponivel;

    @NotNull(message = "O id do restaurante não pode estar vazio")
    @Positive(message = "O id restaurante deve ser maior que zero")
    protected Integer idRestaurante;
}
