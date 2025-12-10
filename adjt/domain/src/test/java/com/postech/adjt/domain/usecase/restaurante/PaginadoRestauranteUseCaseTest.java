package com.postech.adjt.domain.usecase.restaurante;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.TipoCozinha;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("PaginadoRestauranteUseCase - Testes Unitários")
class PaginadoRestauranteUseCaseTest {

    @Mock
    private GenericRepositoryPort<Restaurante> restauranteRepository;

    private PaginadoRestauranteUseCase useCase;
    private List<Restaurante> restaurantes;

    @BeforeEach
    void setUp() {
        useCase = PaginadoRestauranteUseCase.create(restauranteRepository);

        Usuario dono = Usuario.builder()
                .id(1)
                .nome("Dono")
                .email("dono@test.com")
                .build();

        TipoCozinha tipoCozinha = TipoCozinha.builder()
                .id(1)
                .nome("ITALIANA")
                .build();

        Endereco endereco = Endereco.builder()
                .id(1)
                .logradouro("Rua A")
                .numero("100")
                .build();

        restaurantes = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            restaurantes.add(Restaurante.builder()
                    .id(i)
                    .nome("Restaurante_" + i)
                    .descricao("Descrição " + i)
                    .horarioFuncionamento("11:00 - 22:00")
                    .tipoCozinha(tipoCozinha)
                    .endereco(endereco)
                    .dono(dono)
                    .ativo(true)
                    .build());
        }
    }

    @Test
    @DisplayName("Deve retornar restaurantes paginados com sucesso")
    void testObterRestaurantesPaginadosComSucesso() {
        ResultadoPaginacaoDTO<Restaurante> resultado = new ResultadoPaginacaoDTO<>(restaurantes, 0, 10, 3);
        when(restauranteRepository.listarPaginado(0, 10, new ArrayList<>(), new ArrayList<>()))
                .thenReturn(resultado);

        ResultadoPaginacaoDTO<Restaurante> resultadoObtido = useCase.run(0, 10, new ArrayList<>(), new ArrayList<>());

        assertNotNull(resultadoObtido);
        assertEquals(3, resultadoObtido.getTotalElements());
        assertEquals(restaurantes.size(), resultadoObtido.getContent().size());
    }

    @Test
    @DisplayName("Deve lançar exceção quando página é negativa")
    void testPaginaNegativa() {
        assertThrows(NotificacaoException.class, () -> {
            useCase.run(-1, 10, new ArrayList<>(), new ArrayList<>());
        });

        verify(restauranteRepository, never()).listarPaginado(anyInt(), anyInt(), any(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando tamanho é zero")
    void testTamanhoZero() {
        assertThrows(NotificacaoException.class, () -> {
            useCase.run(0, 0, new ArrayList<>(), new ArrayList<>());
        });

        verify(restauranteRepository, never()).listarPaginado(anyInt(), anyInt(), any(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando tamanho é negativo")
    void testTamanhoNegativo() {
        assertThrows(NotificacaoException.class, () -> {
            useCase.run(0, -1, new ArrayList<>(), new ArrayList<>());
        });

        verify(restauranteRepository, never()).listarPaginado(anyInt(), anyInt(), any(), any());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver restaurantes")
    void testRetornarListaVazia() {
        ResultadoPaginacaoDTO<Restaurante> resultado = new ResultadoPaginacaoDTO<>(new ArrayList<>(), 0, 10, 0);
        when(restauranteRepository.listarPaginado(0, 10, new ArrayList<>(), new ArrayList<>()))
                .thenReturn(resultado);

        ResultadoPaginacaoDTO<Restaurante> resultadoObtido = useCase.run(0, 10, new ArrayList<>(), new ArrayList<>());

        assertNotNull(resultadoObtido);
        assertEquals(0, resultadoObtido.getTotalElements());
        assertTrue(resultadoObtido.getContent().isEmpty());
    }

    @Test
    @DisplayName("Deve respeitar filtros fornecidos")
    void testRespeitarFiltros() {
        List<FilterDTO> filtros = new ArrayList<>();
        ResultadoPaginacaoDTO<Restaurante> resultado = new ResultadoPaginacaoDTO<>(restaurantes.subList(0, 1), 0, 10, 1);
        when(restauranteRepository.listarPaginado(0, 10, filtros, new ArrayList<>()))
                .thenReturn(resultado);

        useCase.run(0, 10, filtros, new ArrayList<>());

        verify(restauranteRepository, times(1)).listarPaginado(0, 10, filtros, new ArrayList<>());
    }

}
