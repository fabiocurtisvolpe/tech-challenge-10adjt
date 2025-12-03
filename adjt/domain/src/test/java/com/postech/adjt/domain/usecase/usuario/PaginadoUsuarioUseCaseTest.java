package com.postech.adjt.domain.usecase.usuario;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FilterDTO;
import com.postech.adjt.domain.dto.filtro.SortDTO;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.enums.TipoUsuarioEnum;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.UsuarioRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PaginadoUsuarioUseCase - Testes Unitários")
class PaginadoUsuarioUseCaseTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;

    private PaginadoUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = PaginadoUsuarioUseCase.create(usuarioRepository);
    }

    @Test
    @DisplayName("Deve retornar usuários paginados com sucesso")
    void testListarUsuariosPaginadosComSucesso() {
        // Arrange
        int page = 0;
        int size = 10;
        List<FilterDTO> filters = new ArrayList<>();
        List<SortDTO> sorts = new ArrayList<>();

        List<Endereco> enderecos1 = new ArrayList<>();
        Endereco endereco1 = new Endereco("Rua A", "123", "Apt 101", "Bairro X", "Ponto Ref", "12345-678", "São Paulo", "SP", false, null);
        enderecos1.add(endereco1);

        Usuario usuario1 = new Usuario(
            "João Silva",
            "joao@email.com",
            "senha123",
            TipoUsuarioEnum.CLIENTE,
            enderecos1
        );
        usuario1.setId(1);

        List<Usuario> usuariosList = List.of(usuario1);
        ResultadoPaginacaoDTO<Usuario> resultadoPaginacao = new ResultadoPaginacaoDTO<>(usuariosList, page, size, 1L);

        when(usuarioRepository.listarPaginado(page, size, filters, sorts)).thenReturn(resultadoPaginacao);

        // Act
        ResultadoPaginacaoDTO<Usuario> resultado = useCase.run(page, size, filters, sorts);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        assertEquals(page, resultado.getPageNumber());
        assertEquals(size, resultado.getPageSize());
        assertEquals(1L, resultado.getTotalElements());
        assertEquals("João Silva", resultado.getContent().get(0).getNome());
        verify(usuarioRepository, times(1)).listarPaginado(page, size, filters, sorts);
    }

    @Test
    @DisplayName("Deve retornar página vazia com sucesso")
    void testRetornarPaginaVaziaComSucesso() {
        // Arrange
        int page = 0;
        int size = 10;
        List<FilterDTO> filters = new ArrayList<>();
        List<SortDTO> sorts = new ArrayList<>();

        List<Usuario> usuariosList = new ArrayList<>();
        ResultadoPaginacaoDTO<Usuario> resultadoPaginacao = new ResultadoPaginacaoDTO<>(usuariosList, page, size, 0L);

        when(usuarioRepository.listarPaginado(page, size, filters, sorts)).thenReturn(resultadoPaginacao);

        // Act
        ResultadoPaginacaoDTO<Usuario> resultado = useCase.run(page, size, filters, sorts);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.getContent().isEmpty());
        assertEquals(page, resultado.getPageNumber());
        assertEquals(size, resultado.getPageSize());
        assertEquals(0L, resultado.getTotalElements());
        verify(usuarioRepository, times(1)).listarPaginado(page, size, filters, sorts);
    }

    @Test
    @DisplayName("Deve retornar múltiplos usuários paginados")
    void testRetornarMultiplosUsuariosPaginados() {
        // Arrange
        int page = 0;
        int size = 20;
        List<FilterDTO> filters = new ArrayList<>();
        List<SortDTO> sorts = new ArrayList<>();

        List<Endereco> enderecos1 = new ArrayList<>();
        Endereco endereco1 = new Endereco("Rua A", "123", "Apt 101", "Bairro X", "Ponto Ref", "12345-678", "São Paulo", "SP", false, null);
        enderecos1.add(endereco1);

        Usuario usuario1 = new Usuario(
            "João Silva",
            "joao@email.com",
            "senha123",
            TipoUsuarioEnum.CLIENTE,
            enderecos1
        );
        usuario1.setId(1);

        List<Endereco> enderecos2 = new ArrayList<>();
        Endereco endereco2 = new Endereco("Rua B", "456", "Apt 202", "Bairro Y", "Ponto Ref", "23456-789", "Rio de Janeiro", "RJ", false, null);
        enderecos2.add(endereco2);

        Usuario usuario2 = new Usuario(
            "Maria Santos",
            "maria@email.com",
            "senha456",
            TipoUsuarioEnum.DONO_RESTAURANTE,
            enderecos2
        );
        usuario2.setId(2);

        List<Usuario> usuariosList = List.of(usuario1, usuario2);
        ResultadoPaginacaoDTO<Usuario> resultadoPaginacao = new ResultadoPaginacaoDTO<>(usuariosList, page, size, 2L);

        when(usuarioRepository.listarPaginado(page, size, filters, sorts)).thenReturn(resultadoPaginacao);

        // Act
        ResultadoPaginacaoDTO<Usuario> resultado = useCase.run(page, size, filters, sorts);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.getContent().size());
        assertEquals(page, resultado.getPageNumber());
        assertEquals(size, resultado.getPageSize());
        assertEquals(2L, resultado.getTotalElements());
        verify(usuarioRepository, times(1)).listarPaginado(page, size, filters, sorts);
    }

    @Test
    @DisplayName("Deve lançar exceção quando página é negativa")
    void testLancarExcecaoQuandoPaginaNegativa() {
        // Arrange
        int page = -1;
        int size = 10;
        List<FilterDTO> filters = new ArrayList<>();
        List<SortDTO> sorts = new ArrayList<>();

        // Act & Assert
        NotificacaoException exception = assertThrows(
            NotificacaoException.class,
            () -> useCase.run(page, size, filters, sorts)
        );

        assertEquals(MensagemUtil.PAGINA_SIZE_INVALIDA, exception.getMessage());
        verify(usuarioRepository, never()).listarPaginado(anyInt(), anyInt(), anyList(), anyList());
    }

    @Test
    @DisplayName("Deve lançar exceção quando size é zero")
    void testLancarExcecaoQuandoSizeEhZero() {
        // Arrange
        int page = 0;
        int size = 0;
        List<FilterDTO> filters = new ArrayList<>();
        List<SortDTO> sorts = new ArrayList<>();

        // Act & Assert
        NotificacaoException exception = assertThrows(
            NotificacaoException.class,
            () -> useCase.run(page, size, filters, sorts)
        );

        assertEquals(MensagemUtil.PAGINA_SIZE_INVALIDA, exception.getMessage());
        verify(usuarioRepository, never()).listarPaginado(anyInt(), anyInt(), anyList(), anyList());
    }

    @Test
    @DisplayName("Deve lançar exceção quando size é negativo")
    void testLancarExcecaoQuandoSizeNegativo() {
        // Arrange
        int page = 0;
        int size = -5;
        List<FilterDTO> filters = new ArrayList<>();
        List<SortDTO> sorts = new ArrayList<>();

        // Act & Assert
        NotificacaoException exception = assertThrows(
            NotificacaoException.class,
            () -> useCase.run(page, size, filters, sorts)
        );

        assertEquals(MensagemUtil.PAGINA_SIZE_INVALIDA, exception.getMessage());
        verify(usuarioRepository, never()).listarPaginado(anyInt(), anyInt(), anyList(), anyList());
    }

    @Test
    @DisplayName("Deve retornar usuários com filtros aplicados")
    void testRetornarUsuariosComFiltros() {
        // Arrange
        int page = 0;
        int size = 10;
        List<FilterDTO> filters = new ArrayList<>();
        FilterDTO filter = new FilterDTO("nome", "João");
        filters.add(filter);
        List<SortDTO> sorts = new ArrayList<>();

        List<Endereco> enderecos = new ArrayList<>();
        Endereco endereco = new Endereco("Rua A", "123", "Apt 101", "Bairro X", "Ponto Ref", "12345-678", "São Paulo", "SP", false, null);
        enderecos.add(endereco);

        Usuario usuario = new Usuario(
            "João Silva",
            "joao@email.com",
            "senha123",
            TipoUsuarioEnum.CLIENTE,
            enderecos
        );
        usuario.setId(1);

        List<Usuario> usuariosList = List.of(usuario);
        ResultadoPaginacaoDTO<Usuario> resultadoPaginacao = new ResultadoPaginacaoDTO<>(usuariosList, page, size, 1L);

        when(usuarioRepository.listarPaginado(eq(page), eq(size), argThat(list -> list.size() == 1), eq(sorts)))
            .thenReturn(resultadoPaginacao);

        // Act
        ResultadoPaginacaoDTO<Usuario> resultado = useCase.run(page, size, filters, sorts);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        assertEquals("João Silva", resultado.getContent().get(0).getNome());
        verify(usuarioRepository, times(1)).listarPaginado(eq(page), eq(size), argThat(list -> list.size() == 1), eq(sorts));
    }

    @Test
    @DisplayName("Deve retornar usuários com ordenação aplicada")
    void testRetornarUsuariosComOrdenacao() {
        // Arrange
        int page = 0;
        int size = 10;
        List<FilterDTO> filters = new ArrayList<>();
        List<SortDTO> sorts = new ArrayList<>();
        SortDTO sort = new SortDTO("nome", SortDTO.Direction.ASC);
        sorts.add(sort);

        List<Endereco> enderecos = new ArrayList<>();
        Endereco endereco = new Endereco("Rua A", "123", "Apt 101", "Bairro X", "Ponto Ref", "12345-678", "São Paulo", "SP", false, null);
        enderecos.add(endereco);

        Usuario usuario = new Usuario(
            "Ana Silva",
            "ana@email.com",
            "senha789",
            TipoUsuarioEnum.CLIENTE,
            enderecos
        );
        usuario.setId(1);

        List<Usuario> usuariosList = List.of(usuario);
        ResultadoPaginacaoDTO<Usuario> resultadoPaginacao = new ResultadoPaginacaoDTO<>(usuariosList, page, size, 1L);

        when(usuarioRepository.listarPaginado(eq(page), eq(size), eq(filters), argThat(list -> list.size() == 1)))
            .thenReturn(resultadoPaginacao);

        // Act
        ResultadoPaginacaoDTO<Usuario> resultado = useCase.run(page, size, filters, sorts);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        verify(usuarioRepository, times(1)).listarPaginado(eq(page), eq(size), eq(filters), argThat(list -> list.size() == 1));
    }

    @Test
    @DisplayName("Deve retornar usuários em página subsequente")
    void testRetornarUsuariosEmPaginaSubsequente() {
        // Arrange
        int page = 1;
        int size = 10;
        List<FilterDTO> filters = new ArrayList<>();
        List<SortDTO> sorts = new ArrayList<>();

        List<Endereco> enderecos = new ArrayList<>();
        Endereco endereco = new Endereco("Rua C", "789", "Apt 303", "Bairro Z", "Ponto Ref", "34567-890", "Brasília", "DF", false, null);
        enderecos.add(endereco);

        Usuario usuario = new Usuario(
            "Pedro Costa",
            "pedro@email.com",
            "senha456",
            TipoUsuarioEnum.FORNECEDOR,
            enderecos
        );
        usuario.setId(11);

        List<Usuario> usuariosList = List.of(usuario);
        ResultadoPaginacaoDTO<Usuario> resultadoPaginacao = new ResultadoPaginacaoDTO<>(usuariosList, page, size, 11L);

        when(usuarioRepository.listarPaginado(page, size, filters, sorts)).thenReturn(resultadoPaginacao);

        // Act
        ResultadoPaginacaoDTO<Usuario> resultado = useCase.run(page, size, filters, sorts);

        // Assert
        assertNotNull(resultado);
        assertEquals(page, resultado.getPageNumber());
        assertEquals(size, resultado.getPageSize());
        assertEquals(11L, resultado.getTotalElements());
        verify(usuarioRepository, times(1)).listarPaginado(page, size, filters, sorts);
    }
}
