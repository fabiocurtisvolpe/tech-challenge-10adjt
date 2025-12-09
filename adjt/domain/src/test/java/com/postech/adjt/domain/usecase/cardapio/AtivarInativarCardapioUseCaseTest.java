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

import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.CardapioRepositoryPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("AtivarInativarCardapioUseCase - Testes Unitários")
class AtivarInativarCardapioUseCaseTest {

    @Mock
    private CardapioRepositoryPort cardapioRepository;

    private AtivarInativarCardapioUseCase useCase;
    private Cardapio cardapioExistente;

    @BeforeEach
    void setUp() {
        useCase = AtivarInativarCardapioUseCase.create(cardapioRepository);

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

        cardapioExistente = Cardapio.builder()
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
    @DisplayName("Deve ativar cardápio com sucesso")
    void testAtivarCardapioComSucesso() {
        when(cardapioRepository.obterPorId(1)).thenReturn(Optional.of(cardapioExistente));

        Cardapio cardapioAtivado = Cardapio.builder()
                .id(1)
                .nome("Pizza")
                .descricao("Pizza deliciosa")
                .preco(25.0)
                .foto("url_foto")
                .disponivel(true)
                .restaurante(cardapioExistente.getRestaurante())
                .build();

        when(cardapioRepository.atualizar(any(Cardapio.class)))
                .thenReturn(cardapioAtivado);

        Cardapio resultado = useCase.run(1, true, 1);

        assertNotNull(resultado);
        assertTrue(resultado.getDisponivel());
        verify(cardapioRepository, times(1)).atualizar(any(Cardapio.class));
    }

    @Test
    @DisplayName("Deve inativar cardápio com sucesso")
    void testInativarCardapioComSucesso() {
        when(cardapioRepository.obterPorId(1)).thenReturn(Optional.of(cardapioExistente));

        Cardapio cardapioInativado = Cardapio.builder()
                .id(1)
                .nome("Pizza")
                .descricao("Pizza deliciosa")
                .preco(25.0)
                .foto("url_foto")
                .disponivel(false)
                .restaurante(cardapioExistente.getRestaurante())
                .build();

        when(cardapioRepository.atualizar(any(Cardapio.class)))
                .thenReturn(cardapioInativado);

        Cardapio resultado = useCase.run(1, false, 1);

        assertNotNull(resultado);
        assertFalse(resultado.getDisponivel());
        verify(cardapioRepository, times(1)).atualizar(any(Cardapio.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando cardápio não existe")
    void testAtivarCardapioNaoExistente() {
        when(cardapioRepository.obterPorId(999)).thenReturn(Optional.empty());

        assertThrows(NotificacaoException.class, () -> {
            useCase.run(999, true, 1);
        });

        verify(cardapioRepository, never()).atualizar(any(Cardapio.class));
    }

    @Test
    @DisplayName("Deve manter informações do cardápio ao inativar")
    void testMantendoInformacoesAoInativar() {
        when(cardapioRepository.obterPorId(1)).thenReturn(Optional.of(cardapioExistente));

        Cardapio cardapioInativado = Cardapio.builder()
                .id(1)
                .nome("Pizza")
                .descricao("Pizza deliciosa")
                .preco(25.0)
                .foto("url_foto")
                .disponivel(false)
                .restaurante(cardapioExistente.getRestaurante())
                .build();

        when(cardapioRepository.atualizar(any(Cardapio.class)))
                .thenReturn(cardapioInativado);

        Cardapio resultado = useCase.run(1, false, 1);

        assertEquals("Pizza", resultado.getNome());
        assertEquals(25.0, resultado.getPreco());
    }

    @Test
    @DisplayName("Deve chamar repositório atualizar apenas uma vez")
    void testRepositorioAtualizarChamadoApenasUmaVez() {
        when(cardapioRepository.obterPorId(1)).thenReturn(Optional.of(cardapioExistente));

        Cardapio cardapioAtivado = Cardapio.builder()
                .id(1)
                .nome("Pizza")
                .descricao("Pizza deliciosa")
                .preco(25.0)
                .foto("url_foto")
                .disponivel(true)
                .restaurante(cardapioExistente.getRestaurante())
                .build();

        when(cardapioRepository.atualizar(any(Cardapio.class)))
                .thenReturn(cardapioAtivado);

        useCase.run(1, true, 1);

        verify(cardapioRepository, times(1)).atualizar(any(Cardapio.class));
    }

}
