package com.postech.adjt.api.jwt.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados de autenticação do usuário")
public record LoginRequest(
        @Schema(description = "Login do usuário (pode ser o e-mail)", example = "usuario@email.com.br", requiredMode = Schema.RequiredMode.REQUIRED) @JsonProperty("login") @NotNull(message = "Login não pode ser nulo") @NotBlank(message = "Login é obrigatório") @Email(message = "E-mail inválido") @Size(max = 50, message = "Login deve no máximo 50 caracteres") String login,
        @Schema(description = "Senha do usuário", example = "minhaSenh@123", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 50) @JsonProperty("senha") @NotNull(message = "Senha não pode ser nula") @NotBlank(message = "Senha é obrigatória") @Size(max = 50, message = "Senha deve ter no máximo 50 caracteres") String sehha) {
}