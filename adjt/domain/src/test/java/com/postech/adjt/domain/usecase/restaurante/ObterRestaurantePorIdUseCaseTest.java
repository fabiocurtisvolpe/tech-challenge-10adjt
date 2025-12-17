package com.postech.adjt.domain.usecase.restaurante;

import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObterRestaurantePorIdUseCaseTest {

    @Mock
    private GenericRepositoryPort<Restaurante> restauranteRepository;

    @Mock
    private GenericRepositoryPort<Usuario> usuarioRepository;

    @Mock
    private Restaurante restauranteMock;

    @Mock
    private Usuario usuarioLogadoMock;

    @Mock
    private Usuario donoRestauranteMock;

    private ObterRestaurantePorIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = ObterRestaurantePorIdUseCase.create(restauranteRepository, usuarioRepository);
    }

    @Test
    @DisplayName("Deve retornar restaurante por ID com sucesso (sem validação de dono)")
    void deveRetornarRestaurantePorIdSimples() {
        Integer id = 1;
        when(restauranteRepository.obterPorId(id)).thenReturn(Optional.of(restauranteMock));

        Optional<Restaurante> resultado = useCase.run(id);

        assertThat(resultado.get()).isEqualTo(restauranteMock);
        verify(restauranteRepository).obterPorId(id);
    }

    @Test
    @DisplayName("Deve falhar ao buscar por ID simples se ID for nulo")
    void deveFalharIdNuloSimples() {
        assertThatThrownBy(() -> useCase.run(null))
                .isInstanceOf(NotificacaoException.class);
        verify(restauranteRepository, never()).obterPorId(any());
    }

    @Test
    @DisplayName("Deve falhar ao buscar por ID simples se ID for zero")
    void deveFalharIdZeroSimples() {
        assertThatThrownBy(() -> useCase.run(0))
                .isInstanceOf(NotificacaoException.class);
        verify(restauranteRepository, never()).obterPorId(any());
    }

    @Test
    @DisplayName("Deve falhar ao buscar por ID simples se restaurante não existe")
    void deveFalharRestauranteNaoEncontradoSimples() {
        when(restauranteRepository.obterPorId(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(1))
                .isInstanceOf(NotificacaoException.class);
    }

    @Test
    @DisplayName("Deve retornar restaurante por ID com sucesso (validando dono)")
    void deveRetornarRestauranteValidandoDono() {
        Integer idRestaurante = 1;
        Integer idUsuario = 10;
        String email = "dono@teste.com";

        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.of(usuarioLogadoMock));
        when(restauranteRepository.obterPorId(idRestaurante)).thenReturn(Optional.of(restauranteMock));
        
        when(restauranteMock.getDono()).thenReturn(donoRestauranteMock);
        when(usuarioLogadoMock.getId()).thenReturn(idUsuario);
        when(donoRestauranteMock.getId()).thenReturn(idUsuario);

        Restaurante resultado = useCase.run(idRestaurante, email);

        assertThat(resultado).isEqualTo(restauranteMock);
    }

    @Test
    @DisplayName("Deve falhar ao buscar com validação se ID for nulo")
    void deveFalharIdNuloComValidacao() {
        assertThatThrownBy(() -> useCase.run(null, "email"))
                .isInstanceOf(NotificacaoException.class);
    }

    @Test
    @DisplayName("Deve falhar ao buscar com validação se usuário logado não encontrado")
    void deveFalharUsuarioNaoEncontrado() {
        String email = "inexistente@teste.com";
        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(1, email))
                .isInstanceOf(NotificacaoException.class);
        
        verify(restauranteRepository, never()).obterPorId(anyInt());
    }

    @Test
    @DisplayName("Deve falhar ao buscar com validação se restaurante não encontrado")
    void deveFalharRestauranteNaoEncontradoComValidacao() {
        String email = "user@teste.com";
        Integer idRestaurante = 1;

        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.of(usuarioLogadoMock));
        when(restauranteRepository.obterPorId(idRestaurante)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(idRestaurante, email))
                .isInstanceOf(NotificacaoException.class);
    }

    @Test
    @DisplayName("Deve falhar ao buscar com validação se usuário não for o dono")
    void deveFalharSeUsuarioNaoForDono() {
        Integer idRestaurante = 1;
        Integer idUsuarioIntruso = 99;
        Integer idDonoReal = 50;
        String email = "intruso@teste.com";

        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.of(usuarioLogadoMock));
        when(restauranteRepository.obterPorId(idRestaurante)).thenReturn(Optional.of(restauranteMock));

        when(restauranteMock.getDono()).thenReturn(donoRestauranteMock);
        when(usuarioLogadoMock.getId()).thenReturn(idUsuarioIntruso);
        when(donoRestauranteMock.getId()).thenReturn(idDonoReal);

        assertThatThrownBy(() -> useCase.run(idRestaurante, email))
                .isInstanceOf(NotificacaoException.class); // MensagemUtil.USUARIO_NAO_PERMITE_OPERACAO
    }
}