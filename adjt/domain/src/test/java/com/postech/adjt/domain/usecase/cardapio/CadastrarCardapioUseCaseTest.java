package com.postech.adjt.domain.usecase.cardapio;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.CardapioDTO;
import com.postech.adjt.domain.dto.RestauranteDTO;
import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.CardapioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import com.postech.adjt.domain.usecase.util.UsuarioLogadoUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarCardapioUseCaseTest {

    @Mock
    private GenericRepositoryPort<Cardapio> cardapioRepositoryPort;

    @Mock
    private GenericRepositoryPort<Usuario> usuarioRepository;

    @Mock
    private GenericRepositoryPort<Restaurante> restauranteRepository;

    @Mock
    private CardapioDTO dtoMock;

    @Mock
    private RestauranteDTO restauranteDtoMock;

    @Mock
    private Usuario usuarioLogadoMock;

    @Mock
    private Restaurante restauranteMock;

    @Mock
    private Cardapio cardapioCriadoMock;

    @Mock
    private Cardapio cardapioExistenteMock;

    private CadastrarCardapioUseCase useCase;
    private MockedStatic<CardapioFactory> factoryMockedStatic;
    private MockedStatic<UsuarioLogadoUtil> usuarioUtilMockedStatic;

    @BeforeEach
    void setUp() {
        useCase = CadastrarCardapioUseCase.create(cardapioRepositoryPort, usuarioRepository, restauranteRepository);
        factoryMockedStatic = mockStatic(CardapioFactory.class);
        usuarioUtilMockedStatic = mockStatic(UsuarioLogadoUtil.class);
    }

    @AfterEach
    void tearDown() {
        if (factoryMockedStatic != null) factoryMockedStatic.close();
        if (usuarioUtilMockedStatic != null) usuarioUtilMockedStatic.close();
    }

    @Test
    @DisplayName("Deve cadastrar cardápio com sucesso")
    void deveCadastrarComSucesso() {
        String email = "dono@teste.com";
        Integer idRestaurante = 1;
        Integer idUsuario = 10;
        String nomeCardapio = "Pizza";

        when(dtoMock.restaurante()).thenReturn(restauranteDtoMock);
        when(restauranteDtoMock.id()).thenReturn(idRestaurante);
        when(dtoMock.nome()).thenReturn(nomeCardapio);
        when(dtoMock.descricao()).thenReturn("Deliciosa");
        when(dtoMock.preco()).thenReturn(BigDecimal.TEN);
        when(dtoMock.foto()).thenReturn("foto.jpg");
        when(dtoMock.disponivel()).thenReturn(true);

        usuarioUtilMockedStatic.when(() -> UsuarioLogadoUtil.usuarioLogado(usuarioRepository, email))
                .thenReturn(usuarioLogadoMock);
        when(usuarioLogadoMock.getId()).thenReturn(idUsuario);

        when(restauranteRepository.obterPorId(idRestaurante)).thenReturn(Optional.of(restauranteMock));
        when(cardapioRepositoryPort.obterPorNome(nomeCardapio)).thenReturn(Optional.empty());

        factoryMockedStatic.when(() -> CardapioFactory.criar(
                eq(nomeCardapio), any(), any(), any(), eq(true), eq(restauranteMock), eq(idUsuario)
        )).thenReturn(cardapioCriadoMock);

        when(cardapioRepositoryPort.criar(cardapioCriadoMock)).thenReturn(cardapioCriadoMock);

        Cardapio resultado = useCase.run(dtoMock, email);

        assertThat(resultado).isEqualTo(cardapioCriadoMock);
        verify(cardapioRepositoryPort).criar(cardapioCriadoMock);
    }

    @Test
    @DisplayName("Deve falhar quando restaurante no DTO for nulo")
    void deveFalharRestauranteDtoNulo() {
        when(dtoMock.restaurante()).thenReturn(null);

        assertThatThrownBy(() -> useCase.run(dtoMock, "email"))
                .isInstanceOf(NotificacaoException.class)
                .hasMessage(MensagemUtil.RESTAURANTE_OBRIGATORIO);

        verify(cardapioRepositoryPort, never()).criar(any());
    }

    @Test
    @DisplayName("Deve falhar quando ID do restaurante no DTO for nulo")
    void deveFalharIdRestauranteNulo() {
        when(dtoMock.restaurante()).thenReturn(restauranteDtoMock);
        when(restauranteDtoMock.id()).thenReturn(null);

        assertThatThrownBy(() -> useCase.run(dtoMock, "email"))
                .isInstanceOf(NotificacaoException.class)
                .hasMessage(MensagemUtil.RESTAURANTE_OBRIGATORIO);
    }

    @Test
    @DisplayName("Deve falhar quando restaurante não encontrado no banco")
    void deveFalharRestauranteNaoEncontrado() {
        String email = "email@teste.com";
        Integer idRestaurante = 1;

        when(dtoMock.restaurante()).thenReturn(restauranteDtoMock);
        when(restauranteDtoMock.id()).thenReturn(idRestaurante);

        usuarioUtilMockedStatic.when(() -> UsuarioLogadoUtil.usuarioLogado(usuarioRepository, email))
                .thenReturn(usuarioLogadoMock);

        when(restauranteRepository.obterPorId(idRestaurante)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(dtoMock, email))
                .isInstanceOf(NotificacaoException.class)
                .hasMessage(MensagemUtil.RESTAURANTE_NAO_ENCONTRADO);

        verify(cardapioRepositoryPort, never()).criar(any());
    }

    @Test
    @DisplayName("Deve falhar quando cardápio já existe no mesmo restaurante")
    void deveFalharCardapioDuplicadoMesmoRestaurante() {
        String email = "dono@teste.com";
        Integer idRestaurante = 1;
        String nomeCardapio = "Pizza";

        when(dtoMock.restaurante()).thenReturn(restauranteDtoMock);
        when(restauranteDtoMock.id()).thenReturn(idRestaurante);
        when(dtoMock.nome()).thenReturn(nomeCardapio);

        usuarioUtilMockedStatic.when(() -> UsuarioLogadoUtil.usuarioLogado(usuarioRepository, email))
                .thenReturn(usuarioLogadoMock);

        when(restauranteRepository.obterPorId(idRestaurante)).thenReturn(Optional.of(restauranteMock));
        when(restauranteMock.getId()).thenReturn(idRestaurante);

        when(cardapioRepositoryPort.obterPorNome(nomeCardapio)).thenReturn(Optional.of(cardapioExistenteMock));
        
        Restaurante restauranteDoExistente = mock(Restaurante.class);
        when(cardapioExistenteMock.getRestaurante()).thenReturn(restauranteDoExistente);
        when(restauranteDoExistente.getId()).thenReturn(idRestaurante);

        assertThatThrownBy(() -> useCase.run(dtoMock, email))
                .isInstanceOf(NotificacaoException.class)
                .hasMessage(MensagemUtil.CARDAPIO_JA_CADASTRADO);

        verify(cardapioRepositoryPort, never()).criar(any());
    }

    @Test
    @DisplayName("Deve permitir cadastro com mesmo nome em restaurante diferente")
    void devePermitirMesmoNomeRestauranteDiferente() {
        String email = "dono@teste.com";
        Integer idRestaurante = 1;
        Integer idOutroRestaurante = 2;
        String nomeCardapio = "Pizza";

        when(dtoMock.restaurante()).thenReturn(restauranteDtoMock);
        when(restauranteDtoMock.id()).thenReturn(idRestaurante);
        when(dtoMock.nome()).thenReturn(nomeCardapio);
        when(dtoMock.preco()).thenReturn(BigDecimal.ONE);
        when(dtoMock.disponivel()).thenReturn(true);

        usuarioUtilMockedStatic.when(() -> UsuarioLogadoUtil.usuarioLogado(usuarioRepository, email))
                .thenReturn(usuarioLogadoMock);
        when(usuarioLogadoMock.getId()).thenReturn(10);

        when(restauranteRepository.obterPorId(idRestaurante)).thenReturn(Optional.of(restauranteMock));
        when(restauranteMock.getId()).thenReturn(idRestaurante);

        when(cardapioRepositoryPort.obterPorNome(nomeCardapio)).thenReturn(Optional.of(cardapioExistenteMock));
        
        Restaurante restauranteDoExistente = mock(Restaurante.class);
        when(cardapioExistenteMock.getRestaurante()).thenReturn(restauranteDoExistente);
        when(restauranteDoExistente.getId()).thenReturn(idOutroRestaurante); // Diferente

        factoryMockedStatic.when(() -> CardapioFactory.criar(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(cardapioCriadoMock);
        when(cardapioRepositoryPort.criar(cardapioCriadoMock)).thenReturn(cardapioCriadoMock);

        Cardapio resultado = useCase.run(dtoMock, email);

        assertThat(resultado).isEqualTo(cardapioCriadoMock);
    }
}