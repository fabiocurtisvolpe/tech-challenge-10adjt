package com.postech.adjt.domain.usecase.tipoUsuario;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
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
import com.postech.adjt.domain.ports.TipoUsuarioRepositoryPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("ObterTipoUsuarioPorIdUseCase - Testes Unitários")
class ObterTipoUsuarioPorIdUseCaseTest {

    @Mock
    private TipoUsuarioRepositoryPort tipoUsuarioRepository;

    private ObterTipoUsuarioPorIdUseCase useCase;
    private TipoUsuario tipoUsuarioValido;

    @BeforeEach
    void setUp() {
        useCase = ObterTipoUsuarioPorIdUseCase.create(tipoUsuarioRepository);

        tipoUsuarioValido = TipoUsuario.builder()
                .id(1)
                .nome("TIPO_USUARIO")
                .descricao("Descrição do tipo")
                .build();
    }

    @Test
    @DisplayName("Deve obter tipo de usuário por ID com sucesso")
    void testObterTipoUsuarioPorIdComSucesso() {
        when(tipoUsuarioRepository.obterPorId(1)).thenReturn(Optional.of(tipoUsuarioValido));

        Optional<TipoUsuario> resultado = useCase.run(1);

        assertTrue(resultado.isPresent());
        assertEquals("TIPO_USUARIO", resultado.get().getNome());
        verify(tipoUsuarioRepository, times(1)).obterPorId(1);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID é nulo")
    void testObterTipoUsuarioComIDNulo() {
        assertThrows(NotificacaoException.class, () -> {
            useCase.run(null);
        });

        verify(tipoUsuarioRepository, never()).obterPorId(anyInt());
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID é inválido")
    void testObterTipoUsuarioComIDInvalido() {
        assertThrows(NotificacaoException.class, () -> {
            useCase.run(0);
        });

        assertThrows(NotificacaoException.class, () -> {
            useCase.run(-1);
        });

        verify(tipoUsuarioRepository, never()).obterPorId(anyInt());
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipo de usuário não existe")
    void testObterTipoUsuarioNaoExistente() {
        when(tipoUsuarioRepository.obterPorId(999)).thenReturn(Optional.empty());

        assertThrows(NotificacaoException.class, () -> {
            useCase.run(999);
        });

        verify(tipoUsuarioRepository, times(1)).obterPorId(999);
    }

    @Test
    @DisplayName("Deve retornar tipo de usuário com informações corretas")
    void testRetornarTipoUsuarioComInformacoesCorretas() {
        when(tipoUsuarioRepository.obterPorId(1)).thenReturn(Optional.of(tipoUsuarioValido));

        Optional<TipoUsuario> resultado = useCase.run(1);

        assertTrue(resultado.isPresent());
        assertEquals(1, resultado.get().getId());
        assertEquals("TIPO_USUARIO", resultado.get().getNome());
        assertEquals("Descrição do tipo", resultado.get().getDescricao());
    }

}
