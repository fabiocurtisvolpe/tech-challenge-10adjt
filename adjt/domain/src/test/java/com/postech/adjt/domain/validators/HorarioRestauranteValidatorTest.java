package com.postech.adjt.domain.validators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HorarioRestauranteValidatorTest {

    @Test
    @DisplayName("Deve validar JSON correto com dias abertos e fechados")
    void deveValidarJsonCorreto() {
        String json = "{" +
                "\"seg\": {\"aberto\": true, \"inicio\": \"08:00\", \"fim\": \"18:00\"}," +
                "\"ter\": {\"aberto\": false, \"inicio\": null, \"fim\": null}" +
                "}";

        assertThatCode(() -> HorarioRestauranteValidator.validar(json))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Deve validar JSON correto apenas com um dia")
    void deveValidarUmDia() {
        String json = "{\"dom\": {\"aberto\": true, \"inicio\": \"10:00\", \"fim\": \"14:00\"}}";

        assertThatCode(() -> HorarioRestauranteValidator.validar(json))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Deve falhar se input for nulo")
    void deveFalharInputNulo() {
        assertThatThrownBy(() -> HorarioRestauranteValidator.validar(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("não pode estar vazio");
    }

    @Test
    @DisplayName("Deve falhar se input for vazio")
    void deveFalharInputVazio() {
        assertThatThrownBy(() -> HorarioRestauranteValidator.validar("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("não pode estar vazio");
    }

    @Test
    @DisplayName("Deve falhar se JSON estiver malformado")
    void deveFalharJsonInvalido() {
        String json = "{ \"seg\": { ... erro ... } }";

        assertThatThrownBy(() -> HorarioRestauranteValidator.validar(json))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("formato do JSON de horário é inválido");
    }

    @Test
    @DisplayName("Deve falhar se conter chave de dia inválida")
    void deveFalharDiaInvalido() {
        String json = "{\"feriado\": {\"aberto\": true, \"inicio\": \"08:00\", \"fim\": \"18:00\"}}";

        assertThatThrownBy(() -> HorarioRestauranteValidator.validar(json))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Dia da semana inválido");
    }

    @Test
    @DisplayName("Deve falhar se dia aberto não tiver horários")
    void deveFalharDiaAbertoSemHorario() {
        String json = "{\"seg\": {\"aberto\": true, \"inicio\": \"08:00\"}}"; 

        assertThatThrownBy(() -> HorarioRestauranteValidator.validar(json))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Horário de início e fim são obrigatórios");
    }

    @Test
    @DisplayName("Deve falhar se o formato da hora for inválido")
    void deveFalharFormatoHora() {
        String json = "{\"seg\": {\"aberto\": true, \"inicio\": \"25:00\", \"fim\": \"18:00\"}}";

        assertThatThrownBy(() -> HorarioRestauranteValidator.validar(json))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Formato de hora inválido");
    }

    @Test
    @DisplayName("Deve falhar se hora fim for menor que hora inicio")
    void deveFalharFimMenorQueInicio() {
        String json = "{\"seg\": {\"aberto\": true, \"inicio\": \"18:00\", \"fim\": \"08:00\"}}";

        assertThatThrownBy(() -> HorarioRestauranteValidator.validar(json))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("não pode ser menor que a inicial");
    }
    
    @Test
    @DisplayName("Deve validar se dia fechado tiver horarios preenchidos (ignora validação de tempo)")
    void deveIgnorarValidacaoSeFechado() {
        String json = "{\"seg\": {\"aberto\": false, \"inicio\": \"99:99\", \"fim\": \"00:00\"}}";

        assertThatCode(() -> HorarioRestauranteValidator.validar(json))
                .doesNotThrowAnyException();
    }
}