package com.postech.adjt.domain.usecase.cardapio;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.postech.adjt.domain.dto.CardapioDTO;
import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("AtualizarCardapioUseCase - Testes Unitários")
class AtualizarCardapioUseCaseTest {

    @Mock
    private GenericRepositoryPort<Cardapio> cardapioRepository;

    private AtualizarCardapioUseCase useCase;
    private CardapioDTO cardapioAtualizadoDTO;
    private Cardapio cardapioExistente;
    private Usuario dono;
    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        useCase = AtualizarCardapioUseCase.create(cardapioRepository);

        dono = Usuario.builder()
                .id(1)
                .nome("Dono")
                .email("dono@test.com")
                .build();

        restaurante = Restaurante.builder()
                .id(1)
                .nome("Restaurante Test")
                .dono(dono)
                .build();

        cardapioExistente = Cardapio.builder()
                .id(1)
                .nome("Pizza")
                .descricao("Pizza deliciosa")
                .preco(25.0)
                .restaurante(restaurante)
                .build();

        cardapioAtualizadoDTO = new CardapioDTO(1, "Pizza Premium", "Pizza premium", 35.0, 
                "url_foto_nova", true, restaurante, 1);
    }

    @Test
    @DisplayName("Deve atualizar cardápio com sucesso")
    void testAtualizarCardapioComSucesso() {
        when(cardapioRepository.obterPorId(1)).thenReturn(Optional.of(cardapioExistente));

        Cardapio cardapioAtualizado = Cardapio.builder()
                .id(1)
                .nome("Pizza Premium")
                .descricao("Pizza premium")
                .preco(35.0)
                .foto("url_foto_nova")
                .disponivel(true)
                .restaurante(restaurante)
                .build();

        when(cardapioRepository.atualizar(any(Cardapio.class)))
                .thenReturn(cardapioAtualizado);

        Cardapio resultado = useCase.run(cardapioAtualizadoDTO);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        verify(cardapioRepository, times(1)).atualizar(any(Cardapio.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando cardápio não existe")
    void testAtualizarCardapioNaoExistente() {
        when(cardapioRepository.obterPorId(1)).thenReturn(Optional.empty());

        assertThrows(NotificacaoException.class, () -> {
            useCase.run(cardapioAtualizadoDTO);
        });

        verify(cardapioRepository, never()).atualizar(any(Cardapio.class));
    }

    @Test
    @DisplayName("Deve manter ID ao atualizar cardápio")
    void testMantendoIDCardapio() {
        when(cardapioRepository.obterPorId(1)).thenReturn(Optional.of(cardapioExistente));

        Cardapio cardapioAtualizado = Cardapio.builder()
                .id(1)
                .nome("Pizza Premium")
                .descricao("Pizza premium")
                .preco(35.0)
                .foto("url_foto_nova")
                .disponivel(true)
                .restaurante(restaurante)
                .build();

        when(cardapioRepository.atualizar(any(Cardapio.class)))
                .thenReturn(cardapioAtualizado);

        Cardapio resultado = useCase.run(cardapioAtualizadoDTO);

        assertEquals(1, resultado.getId());
    }

    @Test
    @DisplayName("Deve chamar repositório atualizar apenas uma vez")
    void testRepositorioAtualizarChamadoApenasUmaVez() {
        when(cardapioRepository.obterPorId(1)).thenReturn(Optional.of(cardapioExistente));

        Cardapio cardapioAtualizado = Cardapio.builder()
                .id(1)
                .nome("Pizza Premium")
                .descricao("Pizza premium")
                .preco(35.0)
                .foto("url_foto_nova")
                .disponivel(true)
                .restaurante(restaurante)
                .build();

        when(cardapioRepository.atualizar(any(Cardapio.class)))
                .thenReturn(cardapioAtualizado);

        useCase.run(cardapioAtualizadoDTO);

        verify(cardapioRepository, times(1)).atualizar(any(Cardapio.class));
    }

}
