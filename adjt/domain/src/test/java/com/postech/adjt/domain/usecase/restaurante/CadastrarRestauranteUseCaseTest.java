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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarRestauranteUseCaseTest {

    @Mock
    private GenericRepositoryPort<Restaurante> restauranteRepository;

    @Mock
    private GenericRepositoryPort<Usuario> usuarioRepository;

    @Mock
    private RestauranteDTO restauranteDtoMock;

    @Mock
    private EnderecoDTO enderecoDtoMock;

    @Mock
    private Usuario usuarioLogadoMock;

    @Mock
    private Endereco enderecoMock;

    @Mock
    private Restaurante restauranteCriadoMock;

    private CadastrarRestauranteUseCase useCase;
    private MockedStatic<RestauranteFactory> restauranteFactoryMockedStatic;
    private MockedStatic<EnderecoFactory> enderecoFactoryMockedStatic;

    @BeforeEach
    void setUp() {
        useCase = CadastrarRestauranteUseCase.create(restauranteRepository, usuarioRepository);
        restauranteFactoryMockedStatic = mockStatic(RestauranteFactory.class);
        enderecoFactoryMockedStatic = mockStatic(EnderecoFactory.class);
    }

    @AfterEach
    void tearDown() {
        if (restauranteFactoryMockedStatic != null) restauranteFactoryMockedStatic.close();
        if (enderecoFactoryMockedStatic != null) enderecoFactoryMockedStatic.close();
    }

    @Test
    @DisplayName("Deve cadastrar restaurante com sucesso")
    void deveCadastrarComSucesso() {
        String email = "dono@teste.com";
        String nomeRestaurante = "Restaurante Teste";

        when(restauranteDtoMock.nome()).thenReturn(nomeRestaurante);
        when(restauranteDtoMock.endereco()).thenReturn(enderecoDtoMock);
        when(restauranteDtoMock.tipoCozinha()).thenReturn(TipoCozinhaEnum.BRASILEIRA);
        
        when(restauranteRepository.obterPorNome(nomeRestaurante)).thenReturn(Optional.empty());
        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.of(usuarioLogadoMock));

        enderecoFactoryMockedStatic.when(() -> EnderecoFactory.toEndereco(enderecoDtoMock))
                .thenReturn(enderecoMock);

        restauranteFactoryMockedStatic.when(() -> RestauranteFactory.criar(
                anyString(), any(), any(), any(), eq(enderecoMock), eq(usuarioLogadoMock)
        )).thenReturn(restauranteCriadoMock);

        when(restauranteRepository.criar(restauranteCriadoMock)).thenReturn(restauranteCriadoMock);

        Restaurante resultado = useCase.run(restauranteDtoMock, email);

        assertThat(resultado).isEqualTo(restauranteCriadoMock);
        verify(restauranteRepository).criar(restauranteCriadoMock);
    }

    @Test
    @DisplayName("Deve falhar quando restaurante já existe")
    void deveFalharRestauranteJaExistente() {
        String nomeRestaurante = "Existente";
        when(restauranteDtoMock.nome()).thenReturn(nomeRestaurante);

        when(restauranteRepository.obterPorNome(nomeRestaurante)).thenReturn(Optional.of(mock(Restaurante.class)));

        assertThatThrownBy(() -> useCase.run(restauranteDtoMock, "email@teste.com"))
                .isInstanceOf(NotificacaoException.class);

        verify(usuarioRepository, never()).obterPorEmail(anyString());
        verify(restauranteRepository, never()).criar(any());
    }

    @Test
    @DisplayName("Deve falhar quando usuário logado não for encontrado")
    void deveFalharUsuarioNaoEncontrado() {
        String nomeRestaurante = "Novo";
        String email = "inexistente@teste.com";

        when(restauranteDtoMock.nome()).thenReturn(nomeRestaurante);
        
        when(restauranteRepository.obterPorNome(nomeRestaurante)).thenReturn(Optional.empty());
        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(restauranteDtoMock, email))
                .isInstanceOf(NotificacaoException.class);

        verify(restauranteRepository, never()).criar(any());
    }
}