package com.postech.adjt.domain.usecase.cardapio;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("ObterCardapioPorIdUseCase - Testes Unitários")
class ObterCardapioPorIdUseCaseTest {

    @Mock
    private GenericRepositoryPort<Cardapio> cardapioRepository;

    private ObterCardapioPorIdUseCase useCase;
    private Cardapio cardapioValido;

    @BeforeEach
    void setUp() {
        useCase = ObterCardapioPorIdUseCase.create(cardapioRepository);

        Usuario dono = Usuario.builder()
                .id(1)
                .nome("Dono")
                .email("dono@test.com")
                .build();

        Restaurante restaurante = Restaurante.builder()
                .id(1)
                .nome("Restaurante Test")
                .dono(dono)
                .build();

        cardapioValido = Cardapio.builder()
                .id(1)
                .nome("Pizza")
                .descricao("Pizza deliciosa")
                .preco(25.0)
                .foto("url_foto")
                .disponivel(true)
                .restaurante(restaurante)
                .build();
    }

    @Test
    @DisplayName("Deve obter cardápio por ID com sucesso")
    void testObterCardapioPorIdComSucesso() {
        when(cardapioRepository.obterPorId(1)).thenReturn(Optional.of(cardapioValido));

        Optional<Cardapio> resultado = useCase.run(1);

        assertTrue(resultado.isPresent());
        assertEquals("Pizza", resultado.get().getNome());
        verify(cardapioRepository, times(1)).obterPorId(1);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID é nulo")
    void testObterCardapioComIDNulo() {
        assertThrows(NotificacaoException.class, () -> {
            useCase.run(null);
        });

        verify(cardapioRepository, never()).obterPorId(anyInt());
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID é inválido")
    void testObterCardapioComIDInvalido() {
        assertThrows(NotificacaoException.class, () -> {
            useCase.run(0);
        });

        assertThrows(NotificacaoException.class, () -> {
            useCase.run(-1);
        });

        verify(cardapioRepository, never()).obterPorId(anyInt());
    }

    @Test
    @DisplayName("Deve lançar exceção quando cardápio não existe")
    void testObterCardapioNaoExistente() {
        when(cardapioRepository.obterPorId(999)).thenReturn(Optional.empty());

        assertThrows(NotificacaoException.class, () -> {
            useCase.run(999);
        });

        verify(cardapioRepository, times(1)).obterPorId(999);
    }

    @Test
    @DisplayName("Deve retornar cardápio com informações corretas")
    void testRetornarCardapioComInformacoesCorretas() {
        when(cardapioRepository.obterPorId(1)).thenReturn(Optional.of(cardapioValido));

        Optional<Cardapio> resultado = useCase.run(1);

        assertTrue(resultado.isPresent());
        assertEquals(1, resultado.get().getId());
        assertEquals("Pizza", resultado.get().getNome());
        assertEquals(25.0, resultado.get().getPreco());
    }

}
