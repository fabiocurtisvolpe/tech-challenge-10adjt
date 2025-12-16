package com.postech.adjt.domain.validators;

import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.TipoUsuarioDonoRestaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.enums.TipoCozinhaEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestauranteValidatorTest {

    @Mock
    private Usuario donoMock;

    @Mock
    private TipoUsuarioDonoRestaurante tipoUsuarioDonoMock;

    @Mock
    private TipoUsuario tipoUsuarioComumMock;

    @Test
    @DisplayName("Deve validar restaurante com sucesso")
    void deveValidarComSucesso() {
        try (MockedStatic<HorarioRestauranteValidator> horarioValidator = Mockito.mockStatic(HorarioRestauranteValidator.class)) {
            Restaurante restaurante = Restaurante.builder()
                    .nome("Restaurante Teste")
                    .horarioFuncionamento("10:00-22:00")
                    .tipoCozinha(mock(TipoCozinhaEnum.class))
                    .dono(donoMock)
                    .build();

            when(donoMock.getId()).thenReturn(1);
            when(donoMock.getTipoUsuario()).thenReturn(tipoUsuarioDonoMock);

            assertThatCode(() -> RestauranteValidator.validar(restaurante, 1))
                    .doesNotThrowAnyException();
        }
    }

    @Test
    @DisplayName("Deve falhar quando restaurante for nulo")
    void deveFalharRestauranteNulo() {
        assertThatThrownBy(() -> RestauranteValidator.validar(null, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Deve falhar quando nome for nulo ou vazio")
    void deveFalharNomeVazio() {
        Restaurante restaurante = Restaurante.builder()
                .nome("")
                .build();

        assertThatThrownBy(() -> RestauranteValidator.validar(restaurante, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Deve falhar quando nome for muito curto")
    void deveFalharNomeCurto() {
        Restaurante restaurante = Restaurante.builder()
                .nome("A")
                .build();

        assertThatThrownBy(() -> RestauranteValidator.validar(restaurante, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Deve falhar quando nome for muito longo")
    void deveFalharNomeLongo() {
        String nomeLongo = "A".repeat(200);
        Restaurante restaurante = Restaurante.builder()
                .nome(nomeLongo)
                .build();

        assertThatThrownBy(() -> RestauranteValidator.validar(restaurante, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Deve falhar quando horário for nulo ou vazio")
    void deveFalharHorarioVazio() {
        Restaurante restaurante = Restaurante.builder()
                .nome("Restaurante Válido")
                .horarioFuncionamento("")
                .build();

        assertThatThrownBy(() -> RestauranteValidator.validar(restaurante, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Deve falhar quando tipo cozinha for nulo")
    void deveFalharTipoCozinhaNulo() {
        Restaurante restaurante = Restaurante.builder()
                .nome("Restaurante Válido")
                .horarioFuncionamento("10:00-22:00")
                .tipoCozinha(null)
                .build();

        assertThatThrownBy(() -> RestauranteValidator.validar(restaurante, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Deve falhar quando dono for nulo")
    void deveFalharDonoNulo() {
        Restaurante restaurante = Restaurante.builder()
                .nome("Restaurante Válido")
                .horarioFuncionamento("10:00-22:00")
                .tipoCozinha(mock(TipoCozinhaEnum.class))
                .dono(null)
                .build();

        assertThatThrownBy(() -> RestauranteValidator.validar(restaurante, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Deve falhar quando usuário logado não for o dono (ID diferente)")
    void deveFalharIdDiferente() {
        Restaurante restaurante = Restaurante.builder()
                .nome("Restaurante Válido")
                .horarioFuncionamento("10:00-22:00")
                .tipoCozinha(mock(TipoCozinhaEnum.class))
                .dono(donoMock)
                .build();

        when(donoMock.getId()).thenReturn(2); 

        assertThatThrownBy(() -> RestauranteValidator.validar(restaurante, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Deve falhar quando tipo de usuário não for DonoRestaurante")
    void deveFalharTipoUsuarioInvalido() {
        Restaurante restaurante = Restaurante.builder()
                .nome("Restaurante Válido")
                .horarioFuncionamento("10:00-22:00")
                .tipoCozinha(mock(TipoCozinhaEnum.class))
                .dono(donoMock)
                .build();

        when(donoMock.getId()).thenReturn(1);
        when(donoMock.getTipoUsuario()).thenReturn(tipoUsuarioComumMock);

        assertThatThrownBy(() -> RestauranteValidator.validar(restaurante, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Deve falhar quando descrição for muito longa")
    void deveFalharDescricaoLonga() {
        String descricaoLonga = "A".repeat(500);
        
        Restaurante restaurante = Restaurante.builder()
                .nome("Restaurante Válido")
                .horarioFuncionamento("10:00-22:00")
                .tipoCozinha(mock(TipoCozinhaEnum.class))
                .dono(donoMock)
                .descricao(descricaoLonga)
                .build();

        when(donoMock.getId()).thenReturn(1);
        when(donoMock.getTipoUsuario()).thenReturn(tipoUsuarioDonoMock);

        assertThatThrownBy(() -> RestauranteValidator.validar(restaurante, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
