package com.postech.adjt.domain.usecase.cardapio;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.CardapioDTO;
import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.CardapioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
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
class AtualizarCardapioUseCaseTest {

    @Mock
    private GenericRepositoryPort<Cardapio> cardapioRepositoryPort;

    @Mock
    private GenericRepositoryPort<Usuario> usuarioRepository;

    @Mock
    private Cardapio cardapioExistenteMock;

    @Mock
    private Cardapio cardapioAtualizadoMock;

    @Mock
    private Cardapio outroCardapioMock;

    @Mock
    private Usuario usuarioLogadoMock;

    @Mock
    private Restaurante restauranteMock;

    @Mock
    private CardapioDTO dtoMock;

    private AtualizarCardapioUseCase useCase;
    private MockedStatic<CardapioFactory> factoryMockedStatic;

    @BeforeEach
    void setUp() {
        useCase = AtualizarCardapioUseCase.create(cardapioRepositoryPort, usuarioRepository);
        factoryMockedStatic = mockStatic(CardapioFactory.class);
    }

    @AfterEach
    void tearDown() {
        if (factoryMockedStatic != null) {
            factoryMockedStatic.close();
        }
    }

    @Test
    @DisplayName("Deve atualizar cardápio com sucesso")
    void deveAtualizarComSucesso() {
        Integer idCardapio = 1;
        Integer idUsuario = 10;
        String email = "dono@teste.com";
        String novoNome = "Hambúrguer Especial";
        BigDecimal preco = BigDecimal.TEN;

        when(dtoMock.id()).thenReturn(idCardapio);
        when(dtoMock.nome()).thenReturn(novoNome);
        when(dtoMock.descricao()).thenReturn("Desc");
        when(dtoMock.preco()).thenReturn(preco);
        when(dtoMock.foto()).thenReturn("foto.jpg");
        when(dtoMock.disponivel()).thenReturn(true);

        when(cardapioRepositoryPort.obterPorId(idCardapio)).thenReturn(Optional.of(cardapioExistenteMock));
        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.of(usuarioLogadoMock));
        when(cardapioRepositoryPort.obterPorNome(novoNome)).thenReturn(Optional.empty());

        when(cardapioExistenteMock.getId()).thenReturn(idCardapio);
        when(cardapioExistenteMock.getRestaurante()).thenReturn(restauranteMock);
        when(usuarioLogadoMock.getId()).thenReturn(idUsuario);

        factoryMockedStatic.when(() -> CardapioFactory.cardapio(
                eq(idCardapio), eq(novoNome), any(), eq(preco), any(), eq(restauranteMock), eq(true), eq(true), eq(idUsuario)
        )).thenReturn(cardapioAtualizadoMock);

        when(cardapioRepositoryPort.atualizar(cardapioAtualizadoMock)).thenReturn(cardapioAtualizadoMock);

        Cardapio resultado = useCase.run(dtoMock, email);

        assertThat(resultado).isEqualTo(cardapioAtualizadoMock);
        verify(cardapioRepositoryPort).atualizar(cardapioAtualizadoMock);
    }

    @Test
    @DisplayName("Deve falhar quando ID no DTO for nulo")
    void deveFalharIdNulo() {
        when(dtoMock.id()).thenReturn(null);

        assertThatThrownBy(() -> useCase.run(dtoMock, "email@teste.com"))
                .isInstanceOf(NotificacaoException.class)
                .hasMessage(MensagemUtil.ID_NULO);
        
        verify(cardapioRepositoryPort, never()).obterPorId(any());
    }

    @Test
    @DisplayName("Deve falhar quando cardápio não encontrado")
    void deveFalharCardapioNaoEncontrado() {
        Integer id = 1;
        when(dtoMock.id()).thenReturn(id);
        when(cardapioRepositoryPort.obterPorId(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(dtoMock, "email@teste.com"))
                .isInstanceOf(NotificacaoException.class)
                .hasMessage(MensagemUtil.CARDAPIO_NAO_ENCONTRADO);
    }

    @Test
    @DisplayName("Deve falhar quando usuário logado não encontrado")
    void deveFalharUsuarioNaoEncontrado() {
        Integer id = 1;
        String email = "fantasma@teste.com";
        when(dtoMock.id()).thenReturn(id);
        
        when(cardapioRepositoryPort.obterPorId(id)).thenReturn(Optional.of(cardapioExistenteMock));
        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(dtoMock, email))
                .isInstanceOf(NotificacaoException.class)
                .hasMessage(MensagemUtil.USUARIO_NAO_ENCONTRADO);
    }

    @Test
    @DisplayName("Deve falhar quando já existe outro cardápio com mesmo nome no mesmo restaurante")
    void deveFalharNomeDuplicadoMesmoRestaurante() {
        Integer idAtual = 1;
        Integer idOutro = 2;
        Integer idRestaurante = 5;
        String nome = "Pizza";
        String email = "dono@teste.com";

        when(dtoMock.id()).thenReturn(idAtual);
        when(dtoMock.nome()).thenReturn(nome);

        when(cardapioRepositoryPort.obterPorId(idAtual)).thenReturn(Optional.of(cardapioExistenteMock));
        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.of(usuarioLogadoMock));
        when(cardapioRepositoryPort.obterPorNome(nome)).thenReturn(Optional.of(outroCardapioMock));

        when(cardapioExistenteMock.getId()).thenReturn(idAtual);
        when(cardapioExistenteMock.getRestaurante()).thenReturn(restauranteMock);
        
        when(outroCardapioMock.getId()).thenReturn(idOutro);
        when(outroCardapioMock.getRestaurante()).thenReturn(restauranteMock);
        
        when(restauranteMock.getId()).thenReturn(idRestaurante);

        assertThatThrownBy(() -> useCase.run(dtoMock, email))
                .isInstanceOf(NotificacaoException.class)
                .hasMessage(MensagemUtil.CARDAPIO_JA_CADASTRADO);

        verify(cardapioRepositoryPort, never()).atualizar(any());
    }

    @Test
    @DisplayName("Deve permitir mesmo nome se for o próprio cardápio sendo atualizado")
    void devePermitirMesmoNomeProprioCardapio() {
        Integer idAtual = 1;
        Integer idUsuario = 10;
        String nome = "Pizza";
        String email = "dono@teste.com";

        when(dtoMock.id()).thenReturn(idAtual);
        when(dtoMock.nome()).thenReturn(nome);

        when(cardapioRepositoryPort.obterPorId(idAtual)).thenReturn(Optional.of(cardapioExistenteMock));
        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.of(usuarioLogadoMock));
        when(cardapioRepositoryPort.obterPorNome(nome)).thenReturn(Optional.of(cardapioExistenteMock));

        when(cardapioExistenteMock.getId()).thenReturn(idAtual);
        when(cardapioExistenteMock.getRestaurante()).thenReturn(restauranteMock);
        when(usuarioLogadoMock.getId()).thenReturn(idUsuario);

        factoryMockedStatic.when(() -> CardapioFactory.cardapio(any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(cardapioAtualizadoMock);
        
        when(cardapioRepositoryPort.atualizar(cardapioAtualizadoMock)).thenReturn(cardapioAtualizadoMock);

        Cardapio result = useCase.run(dtoMock, email);
        assertThat(result).isEqualTo(cardapioAtualizadoMock);
    }

    @Test
    @DisplayName("Deve propagar erro da Factory se usuário não for dono (validado no CardapioValidator)")
    void deveFalharSeUsuarioNaoForDono() {
        Integer id = 1;
        String email = "intruso@teste.com";
        Integer idIntruso = 99;

        when(dtoMock.id()).thenReturn(id);
        when(dtoMock.nome()).thenReturn("Novo Nome");

        when(cardapioRepositoryPort.obterPorId(id)).thenReturn(Optional.of(cardapioExistenteMock));
        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.of(usuarioLogadoMock));
        when(cardapioRepositoryPort.obterPorNome(any())).thenReturn(Optional.empty());

        when(cardapioExistenteMock.getId()).thenReturn(id);
        when(cardapioExistenteMock.getRestaurante()).thenReturn(restauranteMock);
        when(usuarioLogadoMock.getId()).thenReturn(idIntruso);

        factoryMockedStatic.when(() -> CardapioFactory.cardapio(any(), any(), any(), any(), any(), any(), any(), any(), eq(idIntruso)))
                .thenThrow(new IllegalArgumentException("Usuário não é dono"));

        assertThatThrownBy(() -> useCase.run(dtoMock, email))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Usuário não é dono");

        verify(cardapioRepositoryPort, never()).atualizar(any());
    }
}