package com.postech.adjt.domain.usecase.usuario;

import static org.junit.jupiter.api.Assertions.*;
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
import com.postech.adjt.domain.dto.filtro.SortDTO;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.enums.TipoUsuarioEnum;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.UsuarioRepositoryPort;

/**
 * Testes unitários para PaginadoUsuarioUseCase
 * 
 * Testa a listagem paginada de usuários
 * 
 * @author Fabio
 * @since 2025-12-05
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PaginadoUsuarioUseCase - Testes Unitários")
class PaginadoUsuarioUseCaseTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;

    private PaginadoUsuarioUseCase useCase;

    private List<Usuario> usuarios;
    private List<Endereco> enderecos;

    @BeforeEach
    void setUp() {
        useCase = PaginadoUsuarioUseCase.create(usuarioRepository);

        // Preparar endereços
        enderecos = new ArrayList<>();
        enderecos.add(Endereco.builder()
            .logradouro("Rua Teste")
            .numero("123")
            .complemento("Apto 101")
            .bairro("Centro")
            .pontoReferencia("Perto da praça")
            .cep("12345-678")
            .municipio("São Paulo")
            .uf("SP")
            .principal(true)
            .build());

        // Preparar lista de usuários
        usuarios = new ArrayList<>();
        usuarios.add(Usuario.builder()
            .id(1)
            .nome("João Silva")
            .email("joao@email.com")
            .senha("senha123")
            .tipoUsuario(TipoUsuarioEnum.CLIENTE)
            .enderecos(enderecos)
            .ativo(true)
            .build());

        usuarios.add(Usuario.builder()
            .id(2)
            .nome("Maria Silva")
            .email("maria@email.com")
            .senha("senha456")
            .tipoUsuario(TipoUsuarioEnum.FORNECEDOR)
            .enderecos(enderecos)
            .ativo(true)
            .build());
    }

    @Test
    @DisplayName("Deve listar usuários com paginação padrão")
    void testListarComPaginacaoPadrao() {
        // Arrange
        ResultadoPaginacaoDTO<Usuario> resultado = new ResultadoPaginacaoDTO<>(
            usuarios,
            0,
            10,
            2
        );

        when(usuarioRepository.listarPaginado(0, 10, null, null))
            .thenReturn(resultado);

        // Act
        ResultadoPaginacaoDTO<Usuario> resposta = useCase.run(0, 10, null, null);

        // Assert
        assertNotNull(resposta);
        assertEquals(2, resposta.getContent().size());
        assertEquals(0, resposta.getPageNumber());
        assertEquals(10, resposta.getPageSize());
        verify(usuarioRepository, times(1)).listarPaginado(0, 10, null, null);
    }

    @Test
    @DisplayName("Deve lançar exceção quando página é negativa")
    void testListarComPaginaNegativa() {
        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            useCase.run(-1, 10, null, null);
        });

        verify(usuarioRepository, never()).listarPaginado(anyInt(), anyInt(), any(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando size é zero")
    void testListarComSizeZero() {
        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            useCase.run(0, 0, null, null);
        });

        verify(usuarioRepository, never()).listarPaginado(anyInt(), anyInt(), any(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando size é negativo")
    void testListarComSizeNegativo() {
        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            useCase.run(0, -1, null, null);
        });

        verify(usuarioRepository, never()).listarPaginado(anyInt(), anyInt(), any(), any());
    }

    @Test
    @DisplayName("Deve listar com filtros")
    void testListarComFiltros() {
        // Arrange
        List<FilterDTO> filtros = new ArrayList<>();
        filtros.add(new FilterDTO("tipoUsuario", "CLIENTE"));

        ResultadoPaginacaoDTO<Usuario> resultado = new ResultadoPaginacaoDTO<>(
            usuarios.stream().filter(u -> u.getTipoUsuario() == TipoUsuarioEnum.CLIENTE).toList(),
            0,
            10,
            1
        );

        when(usuarioRepository.listarPaginado(0, 10, filtros, null))
            .thenReturn(resultado);

        // Act
        ResultadoPaginacaoDTO<Usuario> resposta = useCase.run(0, 10, filtros, null);

        // Assert
        assertNotNull(resposta);
        verify(usuarioRepository, times(1)).listarPaginado(0, 10, filtros, null);
    }

    @Test
    @DisplayName("Deve listar com ordenação")
    void testListarComOrdenacao() {
        // Arrange
        List<SortDTO> sorts = new ArrayList<>();
        sorts.add(new SortDTO("nome", SortDTO.Direction.ASC));

        ResultadoPaginacaoDTO<Usuario> resultado = new ResultadoPaginacaoDTO<>(
            usuarios,
            0,
            10,
            2
        );

        when(usuarioRepository.listarPaginado(0, 10, null, sorts))
            .thenReturn(resultado);

        // Act
        ResultadoPaginacaoDTO<Usuario> resposta = useCase.run(0, 10, null, sorts);

        // Assert
        assertNotNull(resposta);
        verify(usuarioRepository, times(1)).listarPaginado(0, 10, null, sorts);
    }

    @Test
    @DisplayName("Deve listar com filtros e ordenação")
    void testListarComFiltrosEOrdenacao() {
        // Arrange
        List<FilterDTO> filtros = new ArrayList<>();
        filtros.add(new FilterDTO("ativo", "true"));

        List<SortDTO> sorts = new ArrayList<>();
        sorts.add(new SortDTO("email", SortDTO.Direction.ASC));

        ResultadoPaginacaoDTO<Usuario> resultado = new ResultadoPaginacaoDTO<>(
            usuarios,
            0,
            10,
            2
        );

        when(usuarioRepository.listarPaginado(0, 10, filtros, sorts))
            .thenReturn(resultado);

        // Act
        ResultadoPaginacaoDTO<Usuario> resposta = useCase.run(0, 10, filtros, sorts);

        // Assert
        assertNotNull(resposta);
        verify(usuarioRepository, times(1)).listarPaginado(0, 10, filtros, sorts);
    }

    @Test
    @DisplayName("Deve listar segunda página")
    void testListarSegundaPagina() {
        // Arrange
        ResultadoPaginacaoDTO<Usuario> resultado = new ResultadoPaginacaoDTO<>(
            new ArrayList<>(),
            1,
            10,
            0
        );

        when(usuarioRepository.listarPaginado(1, 10, null, null))
            .thenReturn(resultado);

        // Act
        ResultadoPaginacaoDTO<Usuario> resposta = useCase.run(1, 10, null, null);

        // Assert
        assertNotNull(resposta);
        assertEquals(1, resposta.getPageNumber());
        assertEquals(0, resposta.getContent().size());
    }

    @Test
    @DisplayName("Deve listar com tamanho customizado")
    void testListarComTamanhCustomizado() {
        // Arrange
        ResultadoPaginacaoDTO<Usuario> resultado = new ResultadoPaginacaoDTO<>(
            usuarios,
            0,
            5,
            2
        );

        when(usuarioRepository.listarPaginado(0, 5, null, null))
            .thenReturn(resultado);

        // Act
        ResultadoPaginacaoDTO<Usuario> resposta = useCase.run(0, 5, null, null);

        // Assert
        assertNotNull(resposta);
        assertEquals(5, resposta.getPageSize());
    }

    @Test
    @DisplayName("Deve validar parâmetros antes de chamar repositório")
    void testValidarParametrosAntesDeChamarRepositorio() {
        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            useCase.run(-1, 10, null, null);
        });

        verify(usuarioRepository, never()).listarPaginado(anyInt(), anyInt(), any(), any());
    }

    @Test
    @DisplayName("Deve retornar resultado com informações de paginação")
    void testRetornarResultadoComInfoPaginacao() {
        // Arrange
        ResultadoPaginacaoDTO<Usuario> resultado = new ResultadoPaginacaoDTO<>(
            usuarios,
            0,
            10,
            2
        );

        when(usuarioRepository.listarPaginado(0, 10, null, null))
            .thenReturn(resultado);

        // Act
        ResultadoPaginacaoDTO<Usuario> resposta = useCase.run(0, 10, null, null);

        // Assert
        assertNotNull(resposta);
        assertEquals(0, resposta.getPageNumber());
        assertEquals(10, resposta.getPageSize());
        assertEquals(2, resposta.getTotalElements());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nenhum resultado")
    void testRetornarListaVaziaQuandoNenhumResultado() {
        // Arrange
        ResultadoPaginacaoDTO<Usuario> resultado = new ResultadoPaginacaoDTO<>(
            new ArrayList<>(),
            0,
            10,
            0
        );

        when(usuarioRepository.listarPaginado(0, 10, null, null))
            .thenReturn(resultado);

        // Act
        ResultadoPaginacaoDTO<Usuario> resposta = useCase.run(0, 10, null, null);

        // Assert
        assertNotNull(resposta);
        assertTrue(resposta.getContent().isEmpty());
        assertEquals(0, resposta.getTotalElements());
    }

}
