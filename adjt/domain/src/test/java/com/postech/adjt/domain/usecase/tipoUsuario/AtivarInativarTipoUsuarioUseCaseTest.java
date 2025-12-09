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
import com.postech.adjt.domain.ports.TipoUsuarioRepositoryPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("AtivarInativarTipoUsuarioUseCase - Testes Unitários")
class AtivarInativarTipoUsuarioUseCaseTest {

    @Mock
    private TipoUsuarioRepositoryPort tipoUsuarioRepository;

    private AtivarInativarTipoUsuarioUseCase useCase;
    private TipoUsuario tipoUsuarioExistente;

    @BeforeEach
    void setUp() {
        useCase = AtivarInativarTipoUsuarioUseCase.create(tipoUsuarioRepository);

        tipoUsuarioExistente = TipoUsuario.builder()
                .id(1)
                .nome("TIPO_USUARIO")
                .descricao("Descrição do tipo")
                .build();
    }

    @Test
    @DisplayName("Deve ativar tipo de usuário com sucesso")
    void testAtivarTipoUsuarioComSucesso() {
        when(tipoUsuarioRepository.obterPorId(1)).thenReturn(Optional.of(tipoUsuarioExistente));

        TipoUsuario tipoUsuarioAtivado = TipoUsuario.builder()
                .id(1)
                .nome("TIPO_USUARIO")
                .descricao("Descrição do tipo")
                .ativo(true)
                .build();

        when(tipoUsuarioRepository.atualizar(any(TipoUsuario.class)))
                .thenReturn(tipoUsuarioAtivado);

        TipoUsuario resultado = useCase.run(1, true);

        assertNotNull(resultado);
        assertTrue(resultado.getAtivo());
        verify(tipoUsuarioRepository, times(1)).atualizar(any(TipoUsuario.class));
    }

    @Test
    @DisplayName("Deve inativar tipo de usuário com sucesso")
    void testInativarTipoUsuarioComSucesso() {
        when(tipoUsuarioRepository.obterPorId(1)).thenReturn(Optional.of(tipoUsuarioExistente));

        TipoUsuario tipoUsuarioInativado = TipoUsuario.builder()
                .id(1)
                .nome("TIPO_USUARIO")
                .descricao("Descrição do tipo")
                .ativo(false)
                .build();

        when(tipoUsuarioRepository.atualizar(any(TipoUsuario.class)))
                .thenReturn(tipoUsuarioInativado);

        TipoUsuario resultado = useCase.run(1, false);

        assertNotNull(resultado);
        assertFalse(resultado.getAtivo());
        verify(tipoUsuarioRepository, times(1)).atualizar(any(TipoUsuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipo de usuário não existe")
    void testAtivarTipoUsuarioNaoExistente() {
        when(tipoUsuarioRepository.obterPorId(999)).thenReturn(Optional.empty());

        assertThrows(NotificacaoException.class, () -> {
            useCase.run(999, true);
        });

        verify(tipoUsuarioRepository, never()).atualizar(any(TipoUsuario.class));
    }

    @Test
    @DisplayName("Deve manter informações do tipo de usuário ao inativar")
    void testMantendoInformacoesAoInativar() {
        when(tipoUsuarioRepository.obterPorId(1)).thenReturn(Optional.of(tipoUsuarioExistente));

        TipoUsuario tipoUsuarioInativado = TipoUsuario.builder()
                .id(1)
                .nome("TIPO_USUARIO")
                .descricao("Descrição do tipo")
                .ativo(false)
                .build();

        when(tipoUsuarioRepository.atualizar(any(TipoUsuario.class)))
                .thenReturn(tipoUsuarioInativado);

        TipoUsuario resultado = useCase.run(1, false);

        assertEquals("TIPO_USUARIO", resultado.getNome());
        assertEquals("Descrição do tipo", resultado.getDescricao());
    }

    @Test
    @DisplayName("Deve chamar repositório atualizar apenas uma vez")
    void testRepositorioAtualizarChamadoApenasUmaVez() {
        when(tipoUsuarioRepository.obterPorId(1)).thenReturn(Optional.of(tipoUsuarioExistente));

        TipoUsuario tipoUsuarioAtivado = TipoUsuario.builder()
                .id(1)
                .nome("TIPO_USUARIO")
                .descricao("Descrição do tipo")
                .ativo(true)
                .build();

        when(tipoUsuarioRepository.atualizar(any(TipoUsuario.class)))
                .thenReturn(tipoUsuarioAtivado);

        useCase.run(1, true);

        verify(tipoUsuarioRepository, times(1)).atualizar(any(TipoUsuario.class));
    }

}
