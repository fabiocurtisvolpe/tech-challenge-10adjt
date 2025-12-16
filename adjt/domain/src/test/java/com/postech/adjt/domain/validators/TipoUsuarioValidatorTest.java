package com.postech.adjt.domain.validators;

import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TipoUsuarioValidatorTest {

    @Mock
    private TipoUsuario tipoUsuarioMock;

    @Mock
    private Restaurante restauranteMock;

    @Mock
    private Usuario usuarioDonoMock;

    private static final Integer ID_USUARIO_LOGADO = 1;
    private static final Integer ID_OUTRO_USUARIO = 999;
    private static final String NOME_VALIDO = "Gerente";
    private static final String DESCRICAO_VALIDA = "Gerencia o restaurante";

    @Test
    @DisplayName("Deve validar com sucesso (Caminho Feliz)")
    void deveValidarComSucesso() {
        configurarCaminhoFeliz();

        assertThatCode(() -> TipoUsuarioValidator.validar(tipoUsuarioMock, ID_USUARIO_LOGADO))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Deve lançar exceção quando TipoUsuario for nulo")
    void deveFalharTipoUsuarioNulo() {
        assertThatThrownBy(() -> TipoUsuarioValidator.validar(null, ID_USUARIO_LOGADO))
                .isInstanceOf(NotificacaoException.class)
                .hasMessageContaining("Tipo de usuário"); // Valida parte da mensagem
    }

    @Test
    @DisplayName("Deve lançar exceção quando Nome for nulo ou vazio")
    void deveFalharNomeVazio() {
        when(tipoUsuarioMock.getNome()).thenReturn("");

        assertThatThrownBy(() -> TipoUsuarioValidator.validar(tipoUsuarioMock, ID_USUARIO_LOGADO))
                .isInstanceOf(NotificacaoException.class);
    }

    @Test
    @DisplayName("Deve lançar exceção quando Nome for muito longo")
    void deveFalharNomeLongo() {
        String nomeGigante = "a".repeat(200);
        when(tipoUsuarioMock.getNome()).thenReturn(nomeGigante);

        assertThatThrownBy(() -> TipoUsuarioValidator.validar(tipoUsuarioMock, ID_USUARIO_LOGADO))
                .isInstanceOf(NotificacaoException.class);
    }

    @Test
    @DisplayName("Deve lançar exceção quando Nome for muito curto")
    void deveFalharNomeCurto() {
        when(tipoUsuarioMock.getNome()).thenReturn("A");

        assertThatThrownBy(() -> TipoUsuarioValidator.validar(tipoUsuarioMock, ID_USUARIO_LOGADO))
                .isInstanceOf(NotificacaoException.class);
    }

    @Test
    @DisplayName("Deve lançar exceção quando Descrição for muito longa")
    void deveFalharDescricaoLonga() {
        
        when(tipoUsuarioMock.getNome()).thenReturn(NOME_VALIDO);
        
        String descricaoGigante = "a".repeat(1000); 
        when(tipoUsuarioMock.getDescricao()).thenReturn(descricaoGigante);

        assertThatThrownBy(() -> TipoUsuarioValidator.validar(tipoUsuarioMock, ID_USUARIO_LOGADO))
                .isInstanceOf(NotificacaoException.class);
    }

    @Test
    @DisplayName("Deve passar se Descrição for nula (opcional)")
    void devePassarDescricaoNula() {
        
        when(tipoUsuarioMock.getNome()).thenReturn(NOME_VALIDO);
        when(tipoUsuarioMock.getDescricao()).thenReturn(null); // Caso null
        when(tipoUsuarioMock.getRestaurante()).thenReturn(restauranteMock);
        when(restauranteMock.getDono()).thenReturn(usuarioDonoMock);
        when(usuarioDonoMock.getId()).thenReturn(ID_USUARIO_LOGADO);

        assertThatCode(() -> TipoUsuarioValidator.validar(tipoUsuarioMock, ID_USUARIO_LOGADO))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Deve lançar exceção quando Restaurante for nulo")
    void deveFalharRestauranteNulo() {

        when(tipoUsuarioMock.getNome()).thenReturn(NOME_VALIDO);
        when(tipoUsuarioMock.getDescricao()).thenReturn(DESCRICAO_VALIDA);
        when(tipoUsuarioMock.getRestaurante()).thenReturn(null);

        assertThatThrownBy(() -> TipoUsuarioValidator.validar(tipoUsuarioMock, ID_USUARIO_LOGADO))
                .isInstanceOf(NotificacaoException.class);
    }

    @Test
    @DisplayName("Deve lançar exceção quando Usuário Logado não for o dono")
    void deveFalharQuandoNaoForDono() {

        when(tipoUsuarioMock.getNome()).thenReturn(NOME_VALIDO);
        when(tipoUsuarioMock.getDescricao()).thenReturn(DESCRICAO_VALIDA);
        when(tipoUsuarioMock.getRestaurante()).thenReturn(restauranteMock);
        when(restauranteMock.getDono()).thenReturn(usuarioDonoMock);
        
        when(usuarioDonoMock.getId()).thenReturn(ID_OUTRO_USUARIO); 

        assertThatThrownBy(() -> TipoUsuarioValidator.validar(tipoUsuarioMock, ID_USUARIO_LOGADO))
                .isInstanceOf(NotificacaoException.class);
    }

    private void configurarCaminhoFeliz() {
        when(tipoUsuarioMock.getNome()).thenReturn(NOME_VALIDO);
        when(tipoUsuarioMock.getDescricao()).thenReturn(DESCRICAO_VALIDA);
        when(tipoUsuarioMock.getRestaurante()).thenReturn(restauranteMock);
        when(restauranteMock.getDono()).thenReturn(usuarioDonoMock);
        when(usuarioDonoMock.getId()).thenReturn(ID_USUARIO_LOGADO);
    }
}
