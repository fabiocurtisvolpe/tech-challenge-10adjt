package com.postech.adjt.domain.usecase.restaurante;

import com.postech.adjt.domain.dto.EnderecoDTO;
import com.postech.adjt.domain.dto.RestauranteDTO;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.enums.TipoCozinhaEnum;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.EnderecoFactory;
import com.postech.adjt.domain.factory.RestauranteFactory;
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
class AtualizarRestauranteUseCaseTest {

    @Mock
    private GenericRepositoryPort<Restaurante> restauranteRepository;

    @Mock
    private GenericRepositoryPort<Usuario> usuarioRepository;

    @Mock
    private Restaurante restauranteExistenteMock;

    @Mock
    private Restaurante restauranteAtualizadoMock;

    @Mock
    private Restaurante outroRestauranteMock;

    @Mock
    private Usuario usuarioLogadoMock;

    @Mock
    private Endereco enderecoMock;

    @Mock
    private RestauranteDTO dtoMock;

    @Mock
    private EnderecoDTO enderecoDtoMock;

    private AtualizarRestauranteUseCase useCase;
    private MockedStatic<RestauranteFactory> restauranteFactoryMockedStatic;
    private MockedStatic<EnderecoFactory> enderecoFactoryMockedStatic;

    @BeforeEach
    void setUp() {
        useCase = AtualizarRestauranteUseCase.create(restauranteRepository, usuarioRepository);
        restauranteFactoryMockedStatic = mockStatic(RestauranteFactory.class);
        enderecoFactoryMockedStatic = mockStatic(EnderecoFactory.class);
    }

    @AfterEach
    void tearDown() {
        if (restauranteFactoryMockedStatic != null) restauranteFactoryMockedStatic.close();
        if (enderecoFactoryMockedStatic != null) enderecoFactoryMockedStatic.close();
    }

    @Test
    @DisplayName("Deve atualizar restaurante com sucesso")
    void deveAtualizarRestauranteComSucesso() {
        Integer idRestaurante = 1;
        Integer idUsuario = 10;
        String email = "dono@teste.com";
        String nome = "Restaurante Atualizado";

        when(dtoMock.id()).thenReturn(idRestaurante);
        when(dtoMock.nome()).thenReturn(nome);
        when(dtoMock.endereco()).thenReturn(enderecoDtoMock);
        when(dtoMock.tipoCozinha()).thenReturn(TipoCozinhaEnum.ITALIANA);

        when(restauranteRepository.obterPorId(idRestaurante)).thenReturn(Optional.of(restauranteExistenteMock));
        when(restauranteRepository.obterPorNome(nome)).thenReturn(Optional.empty());
        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.of(usuarioLogadoMock));

        when(restauranteExistenteMock.getId()).thenReturn(idRestaurante);
        when(restauranteExistenteMock.getDono()).thenReturn(usuarioLogadoMock);
        when(usuarioLogadoMock.getId()).thenReturn(idUsuario);

        enderecoFactoryMockedStatic.when(() -> EnderecoFactory.toEndereco(enderecoDtoMock))
                .thenReturn(enderecoMock);

        restauranteFactoryMockedStatic.when(() -> RestauranteFactory.restaurante(
                eq(idRestaurante), eq(nome), any(), any(), eq(TipoCozinhaEnum.ITALIANA), 
                eq(enderecoMock), eq(usuarioLogadoMock), eq(true), eq(idUsuario)
        )).thenReturn(restauranteAtualizadoMock);

        when(restauranteRepository.atualizar(restauranteAtualizadoMock)).thenReturn(restauranteAtualizadoMock);

        Restaurante resultado = useCase.run(dtoMock, email);

        assertThat(resultado).isEqualTo(restauranteAtualizadoMock);
        verify(restauranteRepository).atualizar(restauranteAtualizadoMock);
    }

    @Test
    @DisplayName("Deve falhar quando ID do DTO for nulo")
    void deveFalharIdNulo() {
        when(dtoMock.id()).thenReturn(null);

        assertThatThrownBy(() -> useCase.run(dtoMock, "email@teste.com"))
                .isInstanceOf(NotificacaoException.class);
        
        verify(restauranteRepository, never()).obterPorId(any());
    }

    @Test
    @DisplayName("Deve falhar quando restaurante não for encontrado")
    void deveFalharRestauranteInexistente() {
        Integer idRestaurante = 1;
        when(dtoMock.id()).thenReturn(idRestaurante);
        
        when(restauranteRepository.obterPorId(idRestaurante)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(dtoMock, "email@teste.com"))
                .isInstanceOf(NotificacaoException.class);

        verify(usuarioRepository, never()).obterPorEmail(any());
    }

    @Test
    @DisplayName("Deve falhar quando nome já existe em outro restaurante")
    void deveFalharNomeDuplicado() {
        Integer idRestaurante = 1;
        Integer idOutroRestaurante = 2;
        String nome = "Restaurante Duplicado";

        when(dtoMock.id()).thenReturn(idRestaurante);
        when(dtoMock.nome()).thenReturn(nome);

        when(restauranteRepository.obterPorId(idRestaurante)).thenReturn(Optional.of(restauranteExistenteMock));
        when(restauranteRepository.obterPorNome(nome)).thenReturn(Optional.of(outroRestauranteMock));
        
        when(restauranteExistenteMock.getId()).thenReturn(idRestaurante);
        when(outroRestauranteMock.getId()).thenReturn(idOutroRestaurante);

        assertThatThrownBy(() -> useCase.run(dtoMock, "email@teste.com"))
                .isInstanceOf(NotificacaoException.class);

        verify(usuarioRepository, never()).obterPorEmail(any());
    }

    @Test
    @DisplayName("Deve atualizar com sucesso se o nome já existe mas é do mesmo restaurante")
    void deveSucessoMesmoNomeMesmoRestaurante() {
        Integer idRestaurante = 1;
        Integer idUsuario = 10;
        String nome = "Mesmo Nome";
        String email = "dono@teste.com";

        when(dtoMock.id()).thenReturn(idRestaurante);
        when(dtoMock.nome()).thenReturn(nome);
        when(dtoMock.endereco()).thenReturn(enderecoDtoMock);

        when(restauranteRepository.obterPorId(idRestaurante)).thenReturn(Optional.of(restauranteExistenteMock));
        when(restauranteRepository.obterPorNome(nome)).thenReturn(Optional.of(restauranteExistenteMock));
        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.of(usuarioLogadoMock));

        when(restauranteExistenteMock.getId()).thenReturn(idRestaurante);
        when(restauranteExistenteMock.getDono()).thenReturn(usuarioLogadoMock);
        when(usuarioLogadoMock.getId()).thenReturn(idUsuario);
        
        enderecoFactoryMockedStatic.when(() -> EnderecoFactory.toEndereco(enderecoDtoMock))
                .thenReturn(enderecoMock);

        restauranteFactoryMockedStatic.when(() -> RestauranteFactory.restaurante(
                eq(idRestaurante), eq(nome), any(), any(), any(), 
                eq(enderecoMock), any(), eq(true), eq(idUsuario)
        )).thenReturn(restauranteAtualizadoMock);

        when(restauranteRepository.atualizar(restauranteAtualizadoMock)).thenReturn(restauranteAtualizadoMock);

        Restaurante resultado = useCase.run(dtoMock, email);
        assertThat(resultado).isEqualTo(restauranteAtualizadoMock);
    }

    @Test
    @DisplayName("Deve falhar quando usuário logado não for encontrado")
    void deveFalharUsuarioNaoEncontrado() {
        Integer idRestaurante = 1;
        String nome = "Restaurante";
        String email = "inexistente@teste.com";

        when(dtoMock.id()).thenReturn(idRestaurante);
        when(dtoMock.nome()).thenReturn(nome);

        when(restauranteRepository.obterPorId(idRestaurante)).thenReturn(Optional.of(restauranteExistenteMock));
        when(restauranteRepository.obterPorNome(nome)).thenReturn(Optional.empty());
        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(dtoMock, email))
                .isInstanceOf(NotificacaoException.class);

        verify(restauranteRepository, never()).atualizar(any());
    }
}