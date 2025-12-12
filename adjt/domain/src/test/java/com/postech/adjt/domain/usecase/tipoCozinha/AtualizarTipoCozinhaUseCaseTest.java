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

import com.postech.adjt.domain.dto.TipoCozinhaDTO;
import com.postech.adjt.domain.entidade.TipoCozinha;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("AtualizarTipoCozinhaUseCase - Testes Unitários")
class AtualizarTipoCozinhaUseCaseTest {

    @Mock
    private GenericRepositoryPort<TipoCozinha> tipoCozinhaRepository;

    private AtualizarTipoCozinhaUseCase useCase;
    private TipoCozinhaDTO tipoCozinhaAtualizadoDTO;
    private TipoCozinha tipoCozinhaExistente;

    @BeforeEach
    void setUp() {
        useCase = AtualizarTipoCozinhaUseCase.create(tipoCozinhaRepository);

        tipoCozinhaExistente = TipoCozinha.builder()
                .id(1)
                .nome("ITALIANA")
                .descricao("Culinária italiana")
                .build();

        tipoCozinhaAtualizadoDTO = new TipoCozinhaDTO(1, "ITALIANA_MODERNA", "Culinária italiana moderna", true);
    }

    @Test
    @DisplayName("Deve atualizar tipo de cozinha com sucesso")
    void testAtualizarTipoCozinhaComSucesso() {
        when(tipoCozinhaRepository.obterPorId(1)).thenReturn(Optional.of(tipoCozinhaExistente));

        TipoCozinha tipoCozinhaAtualizada = TipoCozinha.builder()
                .id(1)
                .nome("ITALIANA_MODERNA")
                .descricao("Culinária italiana moderna")
                .ativo(true)
                .build();

        when(tipoCozinhaRepository.atualizar(any(TipoCozinha.class)))
                .thenReturn(tipoCozinhaAtualizada);

        TipoCozinha resultado = useCase.run(tipoCozinhaAtualizadoDTO);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        verify(tipoCozinhaRepository, times(1)).atualizar(any(TipoCozinha.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipo de cozinha não existe")
    void testAtualizarTipoCozinhaNaoExistente() {
        when(tipoCozinhaRepository.obterPorId(1)).thenReturn(Optional.empty());

        assertThrows(NotificacaoException.class, () -> {
            useCase.run(tipoCozinhaAtualizadoDTO);
        });

        verify(tipoCozinhaRepository, never()).atualizar(any(TipoCozinha.class));
    }

    @Test
    @DisplayName("Deve manter ID ao atualizar tipo de cozinha")
    void testMantendoIDTipoCozinha() {
        when(tipoCozinhaRepository.obterPorId(1)).thenReturn(Optional.of(tipoCozinhaExistente));

        TipoCozinha tipoCozinhaAtualizada = TipoCozinha.builder()
                .id(1)
                .nome("ITALIANA_MODERNA")
                .descricao("Culinária italiana moderna")
                .ativo(true)
                .build();

        when(tipoCozinhaRepository.atualizar(any(TipoCozinha.class)))
                .thenReturn(tipoCozinhaAtualizada);

        TipoCozinha resultado = useCase.run(tipoCozinhaAtualizadoDTO);

        assertEquals(1, resultado.getId());
    }

    @Test
    @DisplayName("Deve chamar repositório atualizar apenas uma vez")
    void testRepositorioAtualizarChamadoApenasUmaVez() {
        when(tipoCozinhaRepository.obterPorId(1)).thenReturn(Optional.of(tipoCozinhaExistente));

        TipoCozinha tipoCozinhaAtualizada = TipoCozinha.builder()
                .id(1)
                .nome("ITALIANA_MODERNA")
                .descricao("Culinária italiana moderna")
                .ativo(true)
                .build();

        when(tipoCozinhaRepository.atualizar(any(TipoCozinha.class)))
                .thenReturn(tipoCozinhaAtualizada);

        useCase.run(tipoCozinhaAtualizadoDTO);

        verify(tipoCozinhaRepository, times(1)).atualizar(any(TipoCozinha.class));
    }

}
