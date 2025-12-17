package com.postech.adjt.domain.usecase.cardapio;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.Cardapio;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObterCardapioPorIdUseCaseTest {

    @Mock
    private GenericRepositoryPort<Cardapio> cardapioRepositoryPort;

    @Mock
    private Cardapio cardapioMock;

    private ObterCardapioPorIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = ObterCardapioPorIdUseCase.create(cardapioRepositoryPort);
    }

    @Test
    @DisplayName("Deve retornar cardápio com sucesso quando ID existe")
    void deveRetornarCardapioComSucesso() {
        Integer id = 1;
        when(cardapioRepositoryPort.obterPorId(id)).thenReturn(Optional.of(cardapioMock));

        Cardapio resultado = useCase.run(id);

        assertThat(resultado).isEqualTo(cardapioMock);
        verify(cardapioRepositoryPort).obterPorId(id);
    }

    @Test
    @DisplayName("Deve falhar quando ID for nulo")
    void deveFalharIdNulo() {
        assertThatThrownBy(() -> useCase.run(null))
                .isInstanceOf(NotificacaoException.class)
                .hasMessage(MensagemUtil.ID_NULO);

        verify(cardapioRepositoryPort, never()).obterPorId(any());
    }

    @Test
    @DisplayName("Deve falhar quando ID for zero ou negativo")
    void deveFalharIdInvalido() {
        assertThatThrownBy(() -> useCase.run(0))
                .isInstanceOf(NotificacaoException.class)
                .hasMessage(MensagemUtil.ID_NULO);

        verify(cardapioRepositoryPort, never()).obterPorId(any());
    }

    @Test
    @DisplayName("Deve falhar quando cardápio não for encontrado")
    void deveFalharCardapioNaoEncontrado() {
        Integer id = 1;
        when(cardapioRepositoryPort.obterPorId(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(id))
                .isInstanceOf(NotificacaoException.class)
                .hasMessage(MensagemUtil.CARDAPIO_NAO_ENCONTRADO);
    }
}