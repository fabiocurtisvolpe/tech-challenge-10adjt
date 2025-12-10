package com.postech.adjt.domain.usecase.tipoCozinha;

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

import com.postech.adjt.domain.entidade.TipoCozinha;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("ObterTipoCozinhaPorIdUseCase - Testes Unitários")
class ObterTipoCozinhaPorIdUseCaseTest {

    @Mock
    private GenericRepositoryPort<TipoCozinha> tipoCozinhaRepository;

    private ObterTipoCozinhaPorIdUseCase useCase;
    private TipoCozinha tipoCozinhaValida;

    @BeforeEach
    void setUp() {
        useCase = ObterTipoCozinhaPorIdUseCase.create(tipoCozinhaRepository);

        tipoCozinhaValida = TipoCozinha.builder()
                .id(1)
                .nome("ITALIANA")
                .descricao("Culinária italiana")
                .build();
    }

    @Test
    @DisplayName("Deve obter tipo de cozinha por ID com sucesso")
    void testObterTipoCozinhaPorIdComSucesso() {
        when(tipoCozinhaRepository.obterPorId(1)).thenReturn(Optional.of(tipoCozinhaValida));

        Optional<TipoCozinha> resultado = useCase.run(1);

        assertTrue(resultado.isPresent());
        assertEquals("ITALIANA", resultado.get().getNome());
        verify(tipoCozinhaRepository, times(1)).obterPorId(1);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID é nulo")
    void testObterTipoCozinhaComIDNulo() {
        assertThrows(NotificacaoException.class, () -> {
            useCase.run(null);
        });

        verify(tipoCozinhaRepository, never()).obterPorId(anyInt());
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID é inválido")
    void testObterTipoCozinhaComIDInvalido() {
        assertThrows(NotificacaoException.class, () -> {
            useCase.run(0);
        });

        assertThrows(NotificacaoException.class, () -> {
            useCase.run(-1);
        });

        verify(tipoCozinhaRepository, never()).obterPorId(anyInt());
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipo de cozinha não existe")
    void testObterTipoCozinhaNaoExistente() {
        when(tipoCozinhaRepository.obterPorId(999)).thenReturn(Optional.empty());

        assertThrows(NotificacaoException.class, () -> {
            useCase.run(999);
        });

        verify(tipoCozinhaRepository, times(1)).obterPorId(999);
    }

    @Test
    @DisplayName("Deve retornar tipo de cozinha com informações corretas")
    void testRetornarTipoCozinhaComInformacoesCorretas() {
        when(tipoCozinhaRepository.obterPorId(1)).thenReturn(Optional.of(tipoCozinhaValida));

        Optional<TipoCozinha> resultado = useCase.run(1);

        assertTrue(resultado.isPresent());
        assertEquals(1, resultado.get().getId());
        assertEquals("ITALIANA", resultado.get().getNome());
        assertEquals("Culinária italiana", resultado.get().getDescricao());
    }

}
