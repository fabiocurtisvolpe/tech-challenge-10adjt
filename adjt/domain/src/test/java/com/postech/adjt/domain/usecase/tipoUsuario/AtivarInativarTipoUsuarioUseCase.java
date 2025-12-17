package com.postech.adjt.domain.usecase.tipoUsuario;

import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.TipoUsuarioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtivarInativarTipoUsuarioUseCaseTest {

    @Mock
    private GenericRepositoryPort<TipoUsuario> tipoUsuariorepository;

    @Mock
    private GenericRepositoryPort<Usuario> usuarioRepository;

    @Mock
    private TipoUsuario tipoUsuarioMock;

    @Mock
    private TipoUsuario tipoUsuarioAtualizadoMock;

    @Mock
    private Usuario usuarioLogadoMock;

    @Mock
    private Restaurante restauranteMock;

    private AtivarInativarTipoUsuarioUseCase useCase;
    private MockedStatic<TipoUsuarioFactory> factoryMockedStatic;

    @BeforeEach
    void setUp() {
        useCase = AtivarInativarTipoUsuarioUseCase.create(tipoUsuariorepository, usuarioRepository);
        factoryMockedStatic = mockStatic(TipoUsuarioFactory.class);
    }

    @AfterEach
    void tearDown() {
        if (factoryMockedStatic != null) {
            factoryMockedStatic.close();
        }
    }

    @Test
    @DisplayName("Deve ativar TipoUsuario com sucesso")
    void deveAtivarComSucesso() {
        Integer idTipo = 1;
        Integer idUsuario = 10;
        String email = "admin@teste.com";
        Boolean novoStatus = true;

        when(tipoUsuariorepository.obterPorId(idTipo)).thenReturn(Optional.of(tipoUsuarioMock));
        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.of(usuarioLogadoMock));
        
        when(usuarioLogadoMock.getId()).thenReturn(idUsuario);
        
        when(tipoUsuarioMock.getId()).thenReturn(idTipo);
        when(tipoUsuarioMock.getNome()).thenReturn("Gerente");
        when(tipoUsuarioMock.getDescricao()).thenReturn("Desc");
        when(tipoUsuarioMock.getIsDono()).thenReturn(false);
        when(tipoUsuarioMock.getRestaurante()).thenReturn(restauranteMock);
        when(tipoUsuarioMock.getIsEditavel()).thenReturn(true);

        factoryMockedStatic.when(() -> TipoUsuarioFactory.tipoUsuario(
                eq(idTipo), anyString(), anyString(), eq(novoStatus), anyBoolean(), any(), eq(idUsuario)
        )).thenReturn(tipoUsuarioAtualizadoMock);

        when(tipoUsuariorepository.atualizar(tipoUsuarioAtualizadoMock)).thenReturn(tipoUsuarioAtualizadoMock);

        TipoUsuario resultado = useCase.run(novoStatus, idTipo, email);

        assertThat(resultado).isEqualTo(tipoUsuarioAtualizadoMock);
        verify(tipoUsuariorepository).atualizar(tipoUsuarioAtualizadoMock);
    }

    @Test
    @DisplayName("Deve falhar quando TipoUsuario não encontrado")
    void deveFalharTipoUsuarioNaoEncontrado() {
        Integer idTipo = 1;
        String email = "email@teste.com";

        when(tipoUsuariorepository.obterPorId(idTipo)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(true, idTipo, email))
                .isInstanceOf(NotificacaoException.class);

        verify(usuarioRepository, never()).obterPorEmail(anyString());
    }

    @Test
    @DisplayName("Deve falhar quando Usuario logado não encontrado")
    void deveFalharUsuarioNaoEncontrado() {
        Integer idTipo = 1;
        String email = "inexistente@teste.com";

        when(tipoUsuariorepository.obterPorId(idTipo)).thenReturn(Optional.of(tipoUsuarioMock));
        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(true, idTipo, email))
                .isInstanceOf(NotificacaoException.class);

        verify(tipoUsuariorepository, never()).atualizar(any());
    }

    @Test
    @DisplayName("Deve falhar quando TipoUsuario não for editável")
    void deveFalharTipoUsuarioNaoEditavel() {
        Integer idTipo = 1;
        String email = "admin@teste.com";

        when(tipoUsuariorepository.obterPorId(idTipo)).thenReturn(Optional.of(tipoUsuarioMock));
        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.of(usuarioLogadoMock));
        when(tipoUsuarioMock.getIsEditavel()).thenReturn(false);

        assertThatThrownBy(() -> useCase.run(true, idTipo, email))
                .isInstanceOf(NotificacaoException.class);

        verify(tipoUsuariorepository, never()).atualizar(any());
    }
}