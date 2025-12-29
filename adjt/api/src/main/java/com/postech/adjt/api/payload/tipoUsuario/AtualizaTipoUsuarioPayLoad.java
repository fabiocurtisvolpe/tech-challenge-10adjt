package com.postech.adjt.api.payload.tipoUsuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizaTipoUsuarioPayLoad {

    @NotNull(message = "O id do tipo de usuário não pode estar vazio")
    @Positive(message = "O id deve ser maior que zero")
    private Integer id;

    @NotBlank(message = "O nome não pode estar em branco")
    @Size(max = 50, message = "O nome deve ter até 50 caracteres")
    protected String nome;

    @Size(max = 1000, message = "A descrição deve ter até 1000 caracteres")
    protected String descricao;

    @NotNull(message = "O campo ativo não pode estar vazio")
    protected Boolean ativo;

    @NotNull(message = "O campo dono não pode estar vazio")
    protected Boolean isDono;

    @NotNull(message = "O id do restaurante não pode estar vazio")
    @Positive(message = "O id restaurante deve ser maior que zero")
    protected Integer idRestaurante;
}
