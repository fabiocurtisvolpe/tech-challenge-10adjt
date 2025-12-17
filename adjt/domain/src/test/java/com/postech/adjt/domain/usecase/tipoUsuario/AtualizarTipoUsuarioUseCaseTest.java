package com.postech.adjt.domain.usecase.tipoUsuario;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.RestauranteDTO;
import com.postech.adjt.domain.dto.TipoUsuarioDTO;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarTipoUsuarioUseCaseTest {

    @Mock
    private GenericRepositoryPort<TipoUsuario> tipoUsuariorepository;

    @Mock
    private GenericRepositoryPort<Usuario> usuarioRepository;

    @Mock
    private GenericRepositoryPort<Restaurante> restauranteRepository;

    @Mock
    private TipoUsuario tipoUsuarioExistenteMock;

    @Mock
    private TipoUsuario tipoUsuarioAtualizadoMock;

    @Mock
    private Usuario usuarioLogadoMock;

    @Mock
    private Restaurante restauranteMock;

    @Mock
    private TipoUsuarioDTO dtoMock;

    @Mock
    private RestauranteDTO restauranteDtoMock;

    private AtualizarTipoUsuarioUseCase useCase;
    private MockedStatic<TipoUsuarioFactory> factoryMockedStatic;

    @BeforeEach
    void setUp() {
        useCase = AtualizarTipoUsuarioUseCase.create(tipoUsuariorepository, usuarioRepository, restauranteRepository);
        factoryMockedStatic = mockStatic(TipoUsuarioFactory.class);
    }

    @AfterEach
    void tearDown() {
        if (factoryMockedStatic != null) {
            factoryMockedStatic.close();
        }
    }

    @Test
    @DisplayName("Deve atualizar TipoUsuario com sucesso")
    void deveAtualizarComSucesso() {
        Integer idTipo = 1;
        Integer idRestaurante = 5;
        Integer idUsuario = 10;
        String email = "admin@teste.com";

        when(dtoMock.id()).thenReturn(idTipo);
        when(dtoMock.restaurante()).thenReturn(restauranteDtoMock);
        when(dtoMock.isDono()).thenReturn(true);
        when(restauranteDtoMock.id()).thenReturn(idRestaurante);

        when(tipoUsuariorepository.obterPorId(idTipo)).thenReturn(Optional.of(tipoUsuarioExistenteMock));
        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.of(usuarioLogadoMock));
        when(restauranteRepository.obterPorId(idRestaurante)).thenReturn(Optional.of(restauranteMock));

        when(tipoUsuarioExistenteMock.getIsEditavel()).thenReturn(true);
        when(tipoUsuarioExistenteMock.getId()).thenReturn(idTipo);
        when(tipoUsuarioExistenteMock.getNome()).thenReturn("Nome Antigo");
        when(tipoUsuarioExistenteMock.getDescricao()).thenReturn("Desc Antiga");
        
        when(usuarioLogadoMock.getId()).thenReturn(idUsuario);

        factoryMockedStatic.when(() -> TipoUsuarioFactory.tipoUsuario(
                eq(idTipo), anyString(), anyString(), eq(true), eq(true), eq(restauranteMock), eq(idUsuario)
        )).thenReturn(tipoUsuarioAtualizadoMock);

        when(tipoUsuariorepository.atualizar(tipoUsuarioAtualizadoMock)).thenReturn(tipoUsuarioAtualizadoMock);

        TipoUsuario resultado = useCase.run(dtoMock, email);

        assertThat(resultado).isEqualTo(tipoUsuarioAtualizadoMock);
        verify(tipoUsuariorepository).atualizar(tipoUsuarioAtualizadoMock);
    }

    @Test
    @DisplayName("Deve falhar quando TipoUsuario não encontrado")
    void deveFalharTipoUsuarioInexistente() {
        when(dtoMock.id()).thenReturn(1);
        when(tipoUsuariorepository.obterPorId(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(dtoMock, "email@teste.com"))
                .isInstanceOf(NotificacaoException.class);
    }

    @Test
    @DisplayName("Deve falhar quando Usuario logado não encontrado")
    void deveFalharUsuarioLogadoInexistente() {
        when(dtoMock.id()).thenReturn(1);
        when(tipoUsuariorepository.obterPorId(1)).thenReturn(Optional.of(tipoUsuarioExistenteMock));
        
        when(usuarioRepository.obterPorEmail("email@teste.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(dtoMock, "email@teste.com"))
                .isInstanceOf(NotificacaoException.class);
    }

    @Test
    @DisplayName("Deve falhar quando Restaurante DTO for nulo")
    void deveFalharRestauranteDtoNulo() {
        when(dtoMock.id()).thenReturn(1);
        when(dtoMock.restaurante()).thenReturn(null);
        
        when(tipoUsuariorepository.obterPorId(1)).thenReturn(Optional.of(tipoUsuarioExistenteMock));
        when(usuarioRepository.obterPorEmail("email@teste.com")).thenReturn(Optional.of(usuarioLogadoMock));

        assertThatThrownBy(() -> useCase.run(dtoMock, "email@teste.com"))
                .isInstanceOf(NotificacaoException.class);
    }

    @Test
    @DisplayName("Deve falhar quando Restaurante não encontrado no banco")
    void deveFalharRestauranteInexistente() {
        when(dtoMock.id()).thenReturn(1);
        when(dtoMock.restaurante()).thenReturn(restauranteDtoMock);
        when(restauranteDtoMock.id()).thenReturn(5);

        when(tipoUsuariorepository.obterPorId(1)).thenReturn(Optional.of(tipoUsuarioExistenteMock));
        when(usuarioRepository.obterPorEmail("email@teste.com")).thenReturn(Optional.of(usuarioLogadoMock));
        when(restauranteRepository.obterPorId(5)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(dtoMock, "email@teste.com"))
                .isInstanceOf(NotificacaoException.class);
    }

    @Test
    @DisplayName("Deve falhar quando TipoUsuario não for editável")
    void deveFalharNaoEditavel() {
        when(dtoMock.id()).thenReturn(1);
        when(dtoMock.restaurante()).thenReturn(restauranteDtoMock);
        when(restauranteDtoMock.id()).thenReturn(5);

        when(tipoUsuariorepository.obterPorId(1)).thenReturn(Optional.of(tipoUsuarioExistenteMock));
        when(usuarioRepository.obterPorEmail("email@teste.com")).thenReturn(Optional.of(usuarioLogadoMock));
        when(restauranteRepository.obterPorId(5)).thenReturn(Optional.of(restauranteMock));
        
        when(tipoUsuarioExistenteMock.getIsEditavel()).thenReturn(false);

        assertThatThrownBy(() -> useCase.run(dtoMock, "email@teste.com"))
                .isInstanceOf(NotificacaoException.class);

        verify(tipoUsuariorepository, never()).atualizar(any());
    }

    @Test
    @DisplayName("Deve falhar quando já existe um TipoUsuario com o mesmo nome no mesmo restaurante")
    void deveFalharNomeDuplicadoMesmoRestaurante() {

        Integer idTipo = 1;
        Integer idOutroTipo = 2;
        Integer idRestaurante = 5;
        String nomeDuplicado = "Gerente";
        String email = "admin@teste.com";

        when(dtoMock.id()).thenReturn(idTipo);
        when(dtoMock.nome()).thenReturn(nomeDuplicado);

        when(tipoUsuariorepository.obterPorId(idTipo)).thenReturn(Optional.of(tipoUsuarioExistenteMock));
        when(tipoUsuarioExistenteMock.getId()).thenReturn(idTipo);
        when(tipoUsuarioExistenteMock.getRestaurante()).thenReturn(restauranteMock);
        when(restauranteMock.getId()).thenReturn(idRestaurante);

        TipoUsuario tipoUsuarioEncontradoMock = mock(TipoUsuario.class);
        Restaurante restauranteEncontradoMock = mock(Restaurante.class);
        
        when(tipoUsuariorepository.obterPorNome(nomeDuplicado)).thenReturn(Optional.of(tipoUsuarioEncontradoMock));
        when(tipoUsuarioEncontradoMock.getId()).thenReturn(idOutroTipo); // ID diferente = outro registro
        when(tipoUsuarioEncontradoMock.getRestaurante()).thenReturn(restauranteEncontradoMock);
        when(restauranteEncontradoMock.getId()).thenReturn(idRestaurante); // Mesmo restaurante

        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.of(usuarioLogadoMock));

        assertThatThrownBy(() -> useCase.run(dtoMock, email))
                .isInstanceOf(NotificacaoException.class)
                .hasMessage(MensagemUtil.TIPO_USUARIO_JA_CADASTRADO);

        verify(tipoUsuariorepository, never()).atualizar(any());
    }
}
