package com.postech.adjt.domain.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.TipoUsuarioFactory;
import com.postech.adjt.domain.factory.UsuarioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("PaginadoUseCase - Testes Unitários")
class PaginadoUseCaseTest {

    @Mock
    private GenericRepositoryPort<Cardapio> cardapioRepository;

    private PaginadoUseCase<Cardapio> useCase;
    private List<Cardapio> cardapios;

    @BeforeEach
    void setUp() {
        useCase = PaginadoUseCase.create(cardapioRepository);

        Usuario dono = UsuarioFactory.usuario(
                1,
                "Dono",
                "dono@test.com",
                "senha123",
                TipoUsuarioFactory.tipoUsuario(1, "CLIENTE", "CLIENTE", true, false),
                new ArrayList<>(),
                true
        );

        Restaurante restaurante = Restaurante.builder()
                .id(1)
                .nome("Restaurante Test")
                .dono(dono)
                .build();

        cardapios = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            cardapios.add(Cardapio.builder()
                    .id(i)
                    .nome("Prato_" + i)
                    .descricao("Descrição " + i)
                    .preco(20.0 + i)
                    .disponivel(true)
                    .restaurante(restaurante)
                    .build());
        }
    }

    @Test
    @DisplayName("Deve retornar cardápios paginados com sucesso")
    void testObterCardapiosPaginadosComSucesso() {
        ResultadoPaginacaoDTO<Cardapio> resultado = new ResultadoPaginacaoDTO<>(cardapios, 0, 10, 3);
        when(cardapioRepository.listarPaginado(0, 10, new ArrayList<>(), new ArrayList<>()))
                .thenReturn(resultado);

        ResultadoPaginacaoDTO<Cardapio> resultadoObtido = useCase.run(0, 10, new ArrayList<>(), new ArrayList<>());

        assertNotNull(resultadoObtido);
        assertEquals(3, resultadoObtido.getTotalElements());
        assertEquals(cardapios.size(), resultadoObtido.getContent().size());
    }

    @Test
    @DisplayName("Deve lançar exceção quando página é negativa")
    void testPaginaNegativa() {
        assertThrows(NotificacaoException.class, () -> {
            useCase.run(-1, 10, new ArrayList<>(), new ArrayList<>());
        });

        verify(cardapioRepository, never()).listarPaginado(anyInt(), anyInt(), any(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando tamanho é zero")
    void testTamanhoZero() {
        assertThrows(NotificacaoException.class, () -> {
            useCase.run(0, 0, new ArrayList<>(), new ArrayList<>());
        });

        verify(cardapioRepository, never()).listarPaginado(anyInt(), anyInt(), any(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando tamanho é negativo")
    void testTamanhoNegativo() {
        assertThrows(NotificacaoException.class, () -> {
            useCase.run(0, -1, new ArrayList<>(), new ArrayList<>());
        });

        verify(cardapioRepository, never()).listarPaginado(anyInt(), anyInt(), any(), any());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver cardápios")
    void testRetornarListaVazia() {
        ResultadoPaginacaoDTO<Cardapio> resultado = new ResultadoPaginacaoDTO<>(new ArrayList<>(), 0, 10, 0);
        when(cardapioRepository.listarPaginado(0, 10, new ArrayList<>(), new ArrayList<>()))
                .thenReturn(resultado);

        ResultadoPaginacaoDTO<Cardapio> resultadoObtido = useCase.run(0, 10, new ArrayList<>(), new ArrayList<>());

        assertNotNull(resultadoObtido);
        assertEquals(0, resultadoObtido.getTotalElements());
        assertTrue(resultadoObtido.getContent().isEmpty());
    }

    @Test
    @DisplayName("Deve respeitar filtros fornecidos")
    void testRespeitarFiltros() {
        List<FilterDTO> filtros = new ArrayList<>();
        ResultadoPaginacaoDTO<Cardapio> resultado = new ResultadoPaginacaoDTO<>(cardapios.subList(0, 1), 0, 10, 1);
        when(cardapioRepository.listarPaginado(0, 10, filtros, new ArrayList<>()))
                .thenReturn(resultado);

        useCase.run(0, 10, filtros, new ArrayList<>());

        verify(cardapioRepository, times(1)).listarPaginado(0, 10, filtros, new ArrayList<>());
    }
}

