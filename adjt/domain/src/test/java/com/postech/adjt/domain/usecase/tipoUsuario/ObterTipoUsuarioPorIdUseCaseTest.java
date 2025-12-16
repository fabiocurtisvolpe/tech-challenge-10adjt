package com.postech.adjt.domain.usecase.tipoUsuario;

import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObterTipoUsuarioPorIdUseCaseTest {

    @Mock
    private GenericRepositoryPort<TipoUsuario> repositoryPort;

    @Mock
    private GenericRepositoryPort<Usuario> usuarioRepository;

    @Mock
    private TipoUsuario tipoUsuarioMock;

    @Mock
    private Usuario usuarioLogadoMock;

    @Mock
    private Usuario donoRestauranteMock;

    @Mock
    private Restaurante restauranteMock;

    private ObterTipoUsuarioPorIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = ObterTipoUsuarioPorIdUseCase.create(repositoryPort, usuarioRepository);
    }

    @Test
    @DisplayName("Deve retornar TipoUsuario com sucesso quando não possui restaurante vinculado")
    void deveRetornarComSucessoSemRestaurante() {
        Integer id = 1;
        String email = "user@teste.com";

        when(repositoryPort.obterPorId(id)).thenReturn(Optional.of(tipoUsuarioMock));
        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.of(usuarioLogadoMock));
        when(tipoUsuarioMock.getRestaurante()).thenReturn(null);

        Optional<TipoUsuario> resultado = useCase.run(id, email);

        assertThat(resultado.get()).isEqualTo(tipoUsuarioMock);
        verify(repositoryPort).obterPorId(id);
    }

    @Test
    @DisplayName("Deve retornar TipoUsuario com sucesso quando usuário logado é dono do restaurante")
    void deveRetornarComSucessoQuandoDono() {
        Integer id = 1;
        String email = "dono@teste.com";
        Integer idUsuario = 10;

        when(repositoryPort.obterPorId(id)).thenReturn(Optional.of(tipoUsuarioMock));
        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.of(usuarioLogadoMock));
        
        when(tipoUsuarioMock.getRestaurante()).thenReturn(restauranteMock);
        when(restauranteMock.getDono()).thenReturn(donoRestauranteMock);
        
        when(usuarioLogadoMock.getId()).thenReturn(idUsuario);
        when(donoRestauranteMock.getId()).thenReturn(idUsuario);

        Optional<TipoUsuario> resultado = useCase.run(id, email);

        assertThat(resultado.get()).isEqualTo(tipoUsuarioMock);
    }

    @Test
    @DisplayName("Deve falhar quando ID é nulo")
    void deveFalharIdNulo() {
        assertThatThrownBy(() -> useCase.run(null, "email"))
                .isInstanceOf(NotificacaoException.class);
        
        verify(repositoryPort, never()).obterPorId(any());
    }

    @Test
    @DisplayName("Deve falhar quando ID é zero")
    void deveFalharIdZero() {
        assertThatThrownBy(() -> useCase.run(0, "email"))
                .isInstanceOf(NotificacaoException.class);
        
        verify(repositoryPort, never()).obterPorId(any());
    }

    @Test
    @DisplayName("Deve falhar quando TipoUsuario não encontrado")
    void deveFalharTipoUsuarioNaoEncontrado() {
        Integer id = 1;
        when(repositoryPort.obterPorId(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(id, "email"))
                .isInstanceOf(NotificacaoException.class);
        
        verify(usuarioRepository, never()).obterPorEmail(any());
    }

    @Test
    @DisplayName("Deve falhar quando Usuario Logado não encontrado")
    void deveFalharUsuarioLogadoNaoEncontrado() {
        Integer id = 1;
        String email = "inexistente@teste.com";

        when(repositoryPort.obterPorId(id)).thenReturn(Optional.of(tipoUsuarioMock));
        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(id, email))
                .isInstanceOf(NotificacaoException.class);
    }

    @Test
    @DisplayName("Deve falhar quando Usuario Logado não é o dono do restaurante vinculado")
    void deveFalharNaoDono() {
        Integer id = 1;
        String email = "intruso@teste.com";
        Integer idIntruso = 99;
        Integer idDono = 50;

        when(repositoryPort.obterPorId(id)).thenReturn(Optional.of(tipoUsuarioMock));
        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.of(usuarioLogadoMock));

        when(tipoUsuarioMock.getRestaurante()).thenReturn(restauranteMock);
        when(restauranteMock.getDono()).thenReturn(donoRestauranteMock);

        when(usuarioLogadoMock.getId()).thenReturn(idIntruso);
        when(donoRestauranteMock.getId()).thenReturn(idDono);

        assertThatThrownBy(() -> useCase.run(id, email))
                .isInstanceOf(NotificacaoException.class);
    }
}
