package com.postech.adjt.domain.usecase.tipoUsuario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
@DisplayName("CadastrarTipoUsuarioUseCase - Testes Unitários")
class CadastrarTipoUsuarioUseCaseTest {

    @Mock
    private GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository;

    private CadastrarTipoUsuarioUseCase useCase;
    private TipoUsuarioDTO novoTipoUsuarioDTO;

    @BeforeEach
    void setUp() {
        useCase = CadastrarTipoUsuarioUseCase.create(tipoUsuarioRepository);
        novoTipoUsuarioDTO = new TipoUsuarioDTO(null, "NOVO_TIPO", "Descrição do novo tipo", true, false);
    }

    @Test
    @DisplayName("Deve cadastrar novo tipo de usuário com sucesso")
    void testCadastrarNovoTipoUsuarioComSucesso() {
        when(tipoUsuarioRepository.obterPorId(null)).thenReturn(Optional.empty());

        TipoUsuario tipoUsuarioCriado = TipoUsuarioFactory.novo("NOVO_TIPO", "Descrição do novo tipo",
                true);

        when(tipoUsuarioRepository.criar(any(TipoUsuario.class)))
                .thenReturn(tipoUsuarioCriado);

        TipoUsuario resultado = useCase.run(novoTipoUsuarioDTO);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("NOVO_TIPO", resultado.getNome());
        verify(tipoUsuarioRepository, times(1)).criar(any(TipoUsuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipo de usuário já existe")
    void testCadastrarComTipoUsuarioExistente() {
        TipoUsuario tipoUsuarioExistente = TipoUsuarioFactory.novo("NOVO_TIPO", "Descrição do novo tipo",
                true);

        when(tipoUsuarioRepository.obterPorNome("NOVO_TIPO")).thenReturn(Optional.of(tipoUsuarioExistente));

        assertThrows(NotificacaoException.class, () -> {
            useCase.run(novoTipoUsuarioDTO);
        });

        verify(tipoUsuarioRepository, never()).criar(any(TipoUsuario.class));
    }

    @Test
    @DisplayName("Deve retornar tipo de usuário com ID após criação")
    void testTipoUsuarioComIDAposCriacao() {
        when(tipoUsuarioRepository.obterPorNome("NOVO_TIPO")).thenReturn(Optional.empty());

        TipoUsuario tipoUsuarioCriado = TipoUsuarioFactory.novo("NOVO_TIPO", "Descrição do novo tipo",
                true);

        when(tipoUsuarioRepository.criar(any(TipoUsuario.class)))
                .thenReturn(tipoUsuarioCriado);

        TipoUsuario resultado = useCase.run(novoTipoUsuarioDTO);

        assertNotNull(resultado.getId());
        assertEquals(999, resultado.getId());
    }

    @Test
    @DisplayName("Deve chamar repositório criar apenas uma vez")
    void testRepositorioCriarChamadoApenasUmaVez() {
        when(tipoUsuarioRepository.obterPorNome("NOVO_TIPO")).thenReturn(Optional.empty());

        TipoUsuario tipoUsuarioCriado = TipoUsuarioFactory.novo("NOVO_TIPO", "Descrição do novo tipo",
                true);
                
        when(tipoUsuarioRepository.criar(any(TipoUsuario.class)))
                .thenReturn(tipoUsuarioCriado);

        useCase.run(novoTipoUsuarioDTO);

        verify(tipoUsuarioRepository, times(1)).criar(any(TipoUsuario.class));
    }

}
