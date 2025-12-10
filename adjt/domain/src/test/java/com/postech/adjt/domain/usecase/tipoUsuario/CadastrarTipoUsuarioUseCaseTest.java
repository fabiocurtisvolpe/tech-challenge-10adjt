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

import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("CadastrarTipoUsuarioUseCase - Testes Unitários")
class CadastrarTipoUsuarioUseCaseTest {

    @Mock
    private GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository;

    private CadastrarTipoUsuarioUseCase useCase;
    private TipoUsuario novoTipoUsuario;

    @BeforeEach
    void setUp() {
        useCase = CadastrarTipoUsuarioUseCase.create(tipoUsuarioRepository);

        novoTipoUsuario = TipoUsuario.builder()
                .nome("NOVO_TIPO")
                .descricao("Descrição do novo tipo")
                .build();
    }

    @Test
    @DisplayName("Deve cadastrar novo tipo de usuário com sucesso")
    void testCadastrarNovoTipoUsuarioComSucesso() {
        when(tipoUsuarioRepository.obterPorId(null)).thenReturn(Optional.empty());

        TipoUsuario tipoUsuarioCriado = TipoUsuario.builder()
                .id(1)
                .nome("NOVO_TIPO")
                .descricao("Descrição do novo tipo")
                .build();

        when(tipoUsuarioRepository.criar(any(TipoUsuario.class)))
                .thenReturn(tipoUsuarioCriado);

        TipoUsuario resultado = useCase.run(novoTipoUsuario);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("NOVO_TIPO", resultado.getNome());
        verify(tipoUsuarioRepository, times(1)).criar(any(TipoUsuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipo de usuário já existe")
    void testCadastrarComTipoUsuarioExistente() {
        TipoUsuario tipoUsuarioExistente = TipoUsuario.builder()
                .id(1)
                .nome("NOVO_TIPO")
                .descricao("Descrição do novo tipo")
                .build();

        when(tipoUsuarioRepository.obterPorId(null)).thenReturn(Optional.of(tipoUsuarioExistente));

        assertThrows(NotificacaoException.class, () -> {
            useCase.run(novoTipoUsuario);
        });

        verify(tipoUsuarioRepository, never()).criar(any(TipoUsuario.class));
    }

    @Test
    @DisplayName("Deve retornar tipo de usuário com ID após criação")
    void testTipoUsuarioComIDAposCriacao() {
        when(tipoUsuarioRepository.obterPorId(null)).thenReturn(Optional.empty());

        TipoUsuario tipoUsuarioCriado = TipoUsuario.builder()
                .id(999)
                .nome("NOVO_TIPO")
                .descricao("Descrição do novo tipo")
                .build();

        when(tipoUsuarioRepository.criar(any(TipoUsuario.class)))
                .thenReturn(tipoUsuarioCriado);

        TipoUsuario resultado = useCase.run(novoTipoUsuario);

        assertNotNull(resultado.getId());
        assertEquals(999, resultado.getId());
    }

    @Test
    @DisplayName("Deve chamar repositório criar apenas uma vez")
    void testRepositorioCriarChamadoApenasUmaVez() {
        when(tipoUsuarioRepository.obterPorId(null)).thenReturn(Optional.empty());

        TipoUsuario tipoUsuarioCriado = TipoUsuario.builder()
                .id(1)
                .nome("NOVO_TIPO")
                .descricao("Descrição do novo tipo")
                .build();

        when(tipoUsuarioRepository.criar(any(TipoUsuario.class)))
                .thenReturn(tipoUsuarioCriado);

        useCase.run(novoTipoUsuario);

        verify(tipoUsuarioRepository, times(1)).criar(any(TipoUsuario.class));
    }

}
