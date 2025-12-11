package com.postech.adjt.domain.usecase.tipoUsuario;

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

import com.postech.adjt.domain.dto.TipoUsuarioDTO;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.TipoUsuarioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("AtualizarTipoUsuarioUseCase - Testes Unitários")
class AtualizarTipoUsuarioUseCaseTest {

    @Mock
    private GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository;

    private AtualizarTipoUsuarioUseCase useCase;
    private TipoUsuarioDTO tipoUsuarioAtualizadoDTO;
    private TipoUsuario tipoUsuarioExistente;

    @BeforeEach
    void setUp() {

        useCase = AtualizarTipoUsuarioUseCase.create(tipoUsuarioRepository);

        tipoUsuarioExistente = TipoUsuarioFactory.atualizar(1, "TIPO_ANTIGO", "Descrição antiga",
                true, true);

        tipoUsuarioAtualizadoDTO = new TipoUsuarioDTO(1, "TIPO_NOVO", "Descrição nova", true, true);
    }

    @Test
    @DisplayName("Deve atualizar tipo de usuário com sucesso")
    void testAtualizarTipoUsuarioComSucesso() {
        when(tipoUsuarioRepository.obterPorId(1)).thenReturn(Optional.of(tipoUsuarioExistente));

        TipoUsuario tipoUsuarioAtualizado = TipoUsuarioFactory.atualizar(1, "TIPO_ANTIGO", "Descrição antiga",
                true, true);

        when(tipoUsuarioRepository.atualizar(any(TipoUsuario.class)))
                .thenReturn(tipoUsuarioAtualizado);

        TipoUsuario resultado = useCase.run(tipoUsuarioAtualizadoDTO);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        verify(tipoUsuarioRepository, times(1)).atualizar(any(TipoUsuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipo de usuário não existe")
    void testAtualizarTipoUsuarioNaoExistente() {
        when(tipoUsuarioRepository.obterPorId(1)).thenReturn(Optional.empty());

        assertThrows(NotificacaoException.class, () -> {
            useCase.run(tipoUsuarioAtualizadoDTO);
        });

        verify(tipoUsuarioRepository, never()).atualizar(any(TipoUsuario.class));
    }

    @Test
    @DisplayName("Deve manter ID ao atualizar tipo de usuário")
    void testMantendoIDTipoUsuario() {
        when(tipoUsuarioRepository.obterPorId(1)).thenReturn(Optional.of(tipoUsuarioExistente));

        TipoUsuario tipoUsuarioAtualizado = TipoUsuarioFactory.atualizar(1, "TIPO_ANTIGO", "Descrição antiga",
                true, true);

        when(tipoUsuarioRepository.atualizar(any(TipoUsuario.class)))
                .thenReturn(tipoUsuarioAtualizado);

        TipoUsuario resultado = useCase.run(tipoUsuarioAtualizadoDTO);

        assertEquals(1, resultado.getId());
    }

    @Test
    @DisplayName("Deve chamar repositório atualizar apenas uma vez")
    void testRepositorioAtualizarChamadoApenasUmaVez() {
        when(tipoUsuarioRepository.obterPorId(1)).thenReturn(Optional.of(tipoUsuarioExistente));

        TipoUsuario tipoUsuarioAtualizado = TipoUsuarioFactory.atualizar(1, "TIPO_ANTIGO", "Descrição antiga",
                true, true);

        when(tipoUsuarioRepository.atualizar(any(TipoUsuario.class)))
                .thenReturn(tipoUsuarioAtualizado);

        useCase.run(tipoUsuarioAtualizadoDTO);

        verify(tipoUsuarioRepository, times(1)).atualizar(any(TipoUsuario.class));
    }

}
