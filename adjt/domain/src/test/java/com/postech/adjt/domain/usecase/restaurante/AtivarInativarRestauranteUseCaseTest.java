package com.postech.adjt.domain.usecase.restaurante;

import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.enums.TipoCozinhaEnum;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.RestauranteFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtivarInativarRestauranteUseCaseTest {

    @Mock
    private GenericRepositoryPort<Restaurante> restauranteRepository;

    @Mock
    private GenericRepositoryPort<Usuario> usuarioRepository;

    @Mock
    private Restaurante restauranteMock;

    @Mock
    private Restaurante restauranteAtualizadoMock;

    @Mock
    private Usuario usuarioLogadoMock;

    private AtivarInativarRestauranteUseCase useCase;
    private MockedStatic<RestauranteFactory> factoryMockedStatic;

    @BeforeEach
    void setUp() {
        useCase = AtivarInativarRestauranteUseCase.create(restauranteRepository, usuarioRepository);
        factoryMockedStatic = mockStatic(RestauranteFactory.class);
    }

    @AfterEach
    void tearDown() {
        if (factoryMockedStatic != null) {
            factoryMockedStatic.close();
        }
    }

    @Test
    @DisplayName("Deve ativar restaurante com sucesso")
    void deveAtivarRestauranteComSucesso() {
        Integer idRestaurante = 1;
        Integer idUsuario = 10;
        String email = "dono@teste.com";
        Boolean novoStatus = true;

        when(restauranteRepository.obterPorId(idRestaurante)).thenReturn(Optional.of(restauranteMock));
        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.of(usuarioLogadoMock));
        
        when(restauranteMock.getId()).thenReturn(idRestaurante);
        when(restauranteMock.getNome()).thenReturn("Restaurante Teste");
        when(restauranteMock.getDescricao()).thenReturn("Desc");
        when(restauranteMock.getHorarioFuncionamento()).thenReturn("10:00-22:00");
        when(restauranteMock.getTipoCozinha()).thenReturn(TipoCozinhaEnum.BRASILEIRA);
        when(restauranteMock.getEndereco()).thenReturn(null);
        when(restauranteMock.getDono()).thenReturn(usuarioLogadoMock);
        
        when(usuarioLogadoMock.getId()).thenReturn(idUsuario);

        factoryMockedStatic.when(() -> RestauranteFactory.restaurante(
                eq(idRestaurante), anyString(), anyString(), anyString(), any(), any(), any(), eq(novoStatus), eq(idUsuario)
        )).thenReturn(restauranteAtualizadoMock);

        when(restauranteRepository.atualizar(restauranteAtualizadoMock)).thenReturn(restauranteAtualizadoMock);

        Restaurante resultado = useCase.run(novoStatus, idRestaurante, email);

        assertThat(resultado).isEqualTo(restauranteAtualizadoMock);
        verify(restauranteRepository).atualizar(restauranteAtualizadoMock);
    }

    @Test
    @DisplayName("Deve falhar quando restaurante não for encontrado")
    void deveFalharRestauranteNaoEncontrado() {
        Integer idRestaurante = 1;
        String email = "email@teste.com";

        when(restauranteRepository.obterPorId(idRestaurante)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(true, idRestaurante, email))
                .isInstanceOf(NotificacaoException.class);

        verify(usuarioRepository, never()).obterPorEmail(anyString());
        verify(restauranteRepository, never()).atualizar(any());
    }

    @Test
    @DisplayName("Deve falhar quando usuário logado não for encontrado")
    void deveFalharUsuarioNaoEncontrado() {
        Integer idRestaurante = 1;
        String email = "inexistente@teste.com";

        when(restauranteRepository.obterPorId(idRestaurante)).thenReturn(Optional.of(restauranteMock));
        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(true, idRestaurante, email))
                .isInstanceOf(NotificacaoException.class);

        verify(restauranteRepository, never()).atualizar(any());
    }
}