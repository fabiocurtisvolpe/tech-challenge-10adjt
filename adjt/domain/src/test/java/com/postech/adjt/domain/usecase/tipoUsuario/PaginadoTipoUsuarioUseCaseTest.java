package com.postech.adjt.domain.usecase.tipoUsuario;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.TipoUsuarioRepositoryPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("PaginadoTipoUsuarioUseCase - Testes Unitários")
class PaginadoTipoUsuarioUseCaseTest {

    @Mock
    private TipoUsuarioRepositoryPort tipoUsuarioRepository;

    private PaginadoTipoUsuarioUseCase useCase;
    private List<TipoUsuario> tiposUsuario;

    @BeforeEach
    void setUp() {
        useCase = PaginadoTipoUsuarioUseCase.create(tipoUsuarioRepository);

        tiposUsuario = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            tiposUsuario.add(TipoUsuario.builder()
                    .id(i)
                    .nome("TIPO_" + i)
                    .descricao("Descrição " + i)
                    .build());
        }
    }

    @Test
    @DisplayName("Deve retornar tipos de usuário paginados com sucesso")
    void testObterTiposUsuarioPaginadosComSucesso() {
        ResultadoPaginacaoDTO<TipoUsuario> resultado = new ResultadoPaginacaoDTO<>(tiposUsuario, 0, 10, 3);
        when(tipoUsuarioRepository.listarPaginado(0, 10, new ArrayList<>(), new ArrayList<>()))
                .thenReturn(resultado);

        ResultadoPaginacaoDTO<TipoUsuario> resultadoObtido = useCase.run(0, 10, new ArrayList<>(), new ArrayList<>());

        assertNotNull(resultadoObtido);
        assertEquals(3, resultadoObtido.getTotalElements());
        assertEquals(tiposUsuario.size(), resultadoObtido.getContent().size());
    }

    @Test
    @DisplayName("Deve lançar exceção quando página é negativa")
    void testPaginaNegativa() {
        assertThrows(NotificacaoException.class, () -> {
            useCase.run(-1, 10, new ArrayList<>(), new ArrayList<>());
        });

        verify(tipoUsuarioRepository, never()).listarPaginado(anyInt(), anyInt(), any(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando tamanho é zero")
    void testTamanhoZero() {
        assertThrows(NotificacaoException.class, () -> {
            useCase.run(0, 0, new ArrayList<>(), new ArrayList<>());
        });

        verify(tipoUsuarioRepository, never()).listarPaginado(anyInt(), anyInt(), any(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando tamanho é negativo")
    void testTamanhoNegativo() {
        assertThrows(NotificacaoException.class, () -> {
            useCase.run(0, -1, new ArrayList<>(), new ArrayList<>());
        });

        verify(tipoUsuarioRepository, never()).listarPaginado(anyInt(), anyInt(), any(), any());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver tipos de usuário")
    void testRetornarListaVazia() {
        ResultadoPaginacaoDTO<TipoUsuario> resultado = new ResultadoPaginacaoDTO<>(new ArrayList<>(), 0, 10, 0);
        when(tipoUsuarioRepository.listarPaginado(0, 10, new ArrayList<>(), new ArrayList<>()))
                .thenReturn(resultado);

        ResultadoPaginacaoDTO<TipoUsuario> resultadoObtido = useCase.run(0, 10, new ArrayList<>(), new ArrayList<>());

        assertNotNull(resultadoObtido);
        assertEquals(0, resultadoObtido.getTotalElements());
        assertTrue(resultadoObtido.getContent().isEmpty());
    }

    @Test
    @DisplayName("Deve respeitar filtros fornecidos")
    void testRespeitarFiltros() {
        List<FilterDTO> filtros = new ArrayList<>();
        ResultadoPaginacaoDTO<TipoUsuario> resultado = new ResultadoPaginacaoDTO<>(tiposUsuario.subList(0, 1), 0, 10, 1);
        when(tipoUsuarioRepository.listarPaginado(0, 10, filtros, new ArrayList<>()))
                .thenReturn(resultado);

        useCase.run(0, 10, filtros, new ArrayList<>());

        verify(tipoUsuarioRepository, times(1)).listarPaginado(0, 10, filtros, new ArrayList<>());
    }

}
