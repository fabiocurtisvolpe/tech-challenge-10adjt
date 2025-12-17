package com.postech.adjt.domain.usecase.cardapio;

import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.CardapioFactory;
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
class AtivarInativarCardapioUseCaseTest {

    @Mock
    private GenericRepositoryPort<Cardapio> cardapioRepositoryPort;

    @Mock
    private GenericRepositoryPort<Usuario> usuarioRepository;

    @Mock
    private Cardapio cardapioExistenteMock;

    @Mock
    private Cardapio cardapioAtualizadoMock;

    @Mock
    private Usuario usuarioLogadoMock;

    @Mock
    private Restaurante restauranteMock;

    private AtivarInativarCardapioUseCase useCase;
    private MockedStatic<CardapioFactory> factoryMockedStatic;

    @BeforeEach
    void setUp() {
        useCase = AtivarInativarCardapioUseCase.create(cardapioRepositoryPort, usuarioRepository);
        factoryMockedStatic = mockStatic(CardapioFactory.class);
    }

    @AfterEach
    void tearDown() {
        if (factoryMockedStatic != null) {
            factoryMockedStatic.close();
        }
    }

    @Test
    @DisplayName("Deve ativar/inativar cardápio com sucesso quando usuário é dono")
    void deveAtualizarComSucesso() {
        Integer idCardapio = 1;
        Integer idUsuario = 10;
        String email = "dono@teste.com";
        Boolean novoStatus = false;

        when(cardapioRepositoryPort.obterPorId(idCardapio)).thenReturn(Optional.of(cardapioExistenteMock));
        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.of(usuarioLogadoMock));

        when(usuarioLogadoMock.getId()).thenReturn(idUsuario);

        factoryMockedStatic.when(() -> CardapioFactory.cardapio(
                eq(idCardapio),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                eq(novoStatus),
                eq(idUsuario)
        )).thenReturn(cardapioAtualizadoMock);

        when(cardapioRepositoryPort.atualizar(cardapioAtualizadoMock)).thenReturn(cardapioAtualizadoMock);

        Cardapio resultado = useCase.run(novoStatus, idCardapio, email);

        assertThat(resultado).isEqualTo(cardapioAtualizadoMock);
        verify(cardapioRepositoryPort).atualizar(cardapioAtualizadoMock);
    }

    @Test
    @DisplayName("Deve falhar quando cardápio não encontrado")
    void deveFalharCardapioInexistente() {
        Integer idCardapio = 1;
        String email = "email@teste.com";

        when(cardapioRepositoryPort.obterPorId(idCardapio)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(true, idCardapio, email))
                .isInstanceOf(NotificacaoException.class);

        verify(usuarioRepository, never()).obterPorEmail(anyString());
    }

    @Test
    @DisplayName("Deve falhar quando usuário logado não encontrado")
    void deveFalharUsuarioInexistente() {
        Integer idCardapio = 1;
        String email = "inexistente@teste.com";

        when(cardapioRepositoryPort.obterPorId(idCardapio)).thenReturn(Optional.of(cardapioExistenteMock));
        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(true, idCardapio, email))
                .isInstanceOf(NotificacaoException.class);

        verify(cardapioRepositoryPort, never()).atualizar(any());
    }

    @Test
    @DisplayName("Deve propagar exceção da Factory se usuário não for dono")
    void deveFalharQuandoFactoryRejeitarUsuario() {
        Integer idCardapio = 1;
        Integer idUsuario = 99;
        String email = "naodono@teste.com";
        Boolean status = true;

        when(cardapioRepositoryPort.obterPorId(idCardapio)).thenReturn(Optional.of(cardapioExistenteMock));
        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.of(usuarioLogadoMock));

        when(usuarioLogadoMock.getId()).thenReturn(idUsuario);

        factoryMockedStatic.when(() -> CardapioFactory.cardapio(
                any(), any(), any(), any(), any(), any(), any(), anyBoolean(), eq(idUsuario)
        )).thenThrow(new IllegalArgumentException("Usuário não é dono"));

        assertThatThrownBy(() -> useCase.run(status, idCardapio, email))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Usuário não é dono");

        verify(cardapioRepositoryPort, never()).atualizar(any());
    }
}