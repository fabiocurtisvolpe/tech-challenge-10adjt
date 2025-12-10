package com.postech.adjt.domain.usecase.tipoCozinha;

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

import com.postech.adjt.domain.entidade.TipoCozinha;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("AtivarInativarTipoCozinhaUseCase - Testes Unitários")
class AtivarInativarTipoCozinhaUseCaseTest {

    @Mock
    private GenericRepositoryPort<TipoCozinha> tipoCozinhaRepository;

    private AtivarInativarTipoCozinhaUseCase useCase;
    private TipoCozinha tipoCozinhaExistente;

    @BeforeEach
    void setUp() {
        useCase = AtivarInativarTipoCozinhaUseCase.create(tipoCozinhaRepository);

        tipoCozinhaExistente = TipoCozinha.builder()
                .id(1)
                .nome("ITALIANA")
                .descricao("Culinária italiana")
                .build();
    }

    @Test
    @DisplayName("Deve ativar tipo de cozinha com sucesso")
    void testAtivarTipoCozinhaComSucesso() {
        when(tipoCozinhaRepository.obterPorId(1)).thenReturn(Optional.of(tipoCozinhaExistente));

        TipoCozinha tipoCozinhaAtivada = TipoCozinha.builder()
                .id(1)
                .nome("ITALIANA")
                .descricao("Culinária italiana")
                .ativo(true)
                .build();

        when(tipoCozinhaRepository.atualizar(any(TipoCozinha.class)))
                .thenReturn(tipoCozinhaAtivada);

        TipoCozinha resultado = useCase.run(1, true);

        assertNotNull(resultado);
        assertTrue(resultado.getAtivo());
        verify(tipoCozinhaRepository, times(1)).atualizar(any(TipoCozinha.class));
    }

    @Test
    @DisplayName("Deve inativar tipo de cozinha com sucesso")
    void testInativarTipoCozinhaComSucesso() {
        when(tipoCozinhaRepository.obterPorId(1)).thenReturn(Optional.of(tipoCozinhaExistente));

        TipoCozinha tipoCozinhaInativada = TipoCozinha.builder()
                .id(1)
                .nome("ITALIANA")
                .descricao("Culinária italiana")
                .ativo(false)
                .build();

        when(tipoCozinhaRepository.atualizar(any(TipoCozinha.class)))
                .thenReturn(tipoCozinhaInativada);

        TipoCozinha resultado = useCase.run(1, false);

        assertNotNull(resultado);
        assertFalse(resultado.getAtivo());
        verify(tipoCozinhaRepository, times(1)).atualizar(any(TipoCozinha.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipo de cozinha não existe")
    void testAtivarTipoCozinhaNaoExistente() {
        when(tipoCozinhaRepository.obterPorId(999)).thenReturn(Optional.empty());

        assertThrows(NotificacaoException.class, () -> {
            useCase.run(999, true);
        });

        verify(tipoCozinhaRepository, never()).atualizar(any(TipoCozinha.class));
    }

    @Test
    @DisplayName("Deve manter informações do tipo de cozinha ao inativar")
    void testMantendoInformacoesAoInativar() {
        when(tipoCozinhaRepository.obterPorId(1)).thenReturn(Optional.of(tipoCozinhaExistente));

        TipoCozinha tipoCozinhaInativada = TipoCozinha.builder()
                .id(1)
                .nome("ITALIANA")
                .descricao("Culinária italiana")
                .ativo(false)
                .build();

        when(tipoCozinhaRepository.atualizar(any(TipoCozinha.class)))
                .thenReturn(tipoCozinhaInativada);

        TipoCozinha resultado = useCase.run(1, false);

        assertEquals("ITALIANA", resultado.getNome());
        assertEquals("Culinária italiana", resultado.getDescricao());
    }

    @Test
    @DisplayName("Deve chamar repositório atualizar apenas uma vez")
    void testRepositorioAtualizarChamadoApenasUmaVez() {
        when(tipoCozinhaRepository.obterPorId(1)).thenReturn(Optional.of(tipoCozinhaExistente));

        TipoCozinha tipoCozinhaAtivada = TipoCozinha.builder()
                .id(1)
                .nome("ITALIANA")
                .descricao("Culinária italiana")
                .ativo(true)
                .build();

        when(tipoCozinhaRepository.atualizar(any(TipoCozinha.class)))
                .thenReturn(tipoCozinhaAtivada);

        useCase.run(1, true);

        verify(tipoCozinhaRepository, times(1)).atualizar(any(TipoCozinha.class));
    }

}
