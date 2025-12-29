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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarTipoUsuarioUseCaseTest {

    @Mock
    private GenericRepositoryPort<TipoUsuario> tipoUsuariorepository;

    @Mock
    private GenericRepositoryPort<Usuario> usuarioRepository;

    @Mock
    private GenericRepositoryPort<Restaurante> restauranteRepository;

    @Mock
    private TipoUsuario tipoUsuarioCriadoMock;

    @Mock
    private Usuario usuarioLogadoMock;

    @Mock
    private Restaurante restauranteMock;

    @Mock
    private TipoUsuarioDTO dtoMock;

    @Mock
    private RestauranteDTO restauranteDtoMock;

    private CadastrarTipoUsuarioUseCase useCase;
    private MockedStatic<TipoUsuarioFactory> factoryMockedStatic;

    @BeforeEach
    void setUp() {
        useCase = CadastrarTipoUsuarioUseCase.create(tipoUsuariorepository, usuarioRepository, restauranteRepository);
        factoryMockedStatic = mockStatic(TipoUsuarioFactory.class);
    }

    @AfterEach
    void tearDown() {
        if (factoryMockedStatic != null) {
            factoryMockedStatic.close();
        }
    }

    @Test
    @DisplayName("Deve cadastrar TipoUsuario com sucesso")
    void deveCadastrarComSucesso() {
        String email = "admin@teste.com";
        String nomeTipo = "Gerente";
        Integer idRestaurante = 5;
        Integer idUsuario = 1;

        when(dtoMock.nome()).thenReturn(nomeTipo);
        when(dtoMock.descricao()).thenReturn("Desc");
        when(dtoMock.isDono()).thenReturn(true);
        when(dtoMock.restaurante()).thenReturn(restauranteDtoMock);
        when(restauranteDtoMock.id()).thenReturn(idRestaurante);

        when(tipoUsuariorepository.obterPorNome(nomeTipo)).thenReturn(Optional.empty());
        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.of(usuarioLogadoMock));
        when(restauranteRepository.obterPorId(idRestaurante)).thenReturn(Optional.of(restauranteMock));
        when(usuarioLogadoMock.getId()).thenReturn(idUsuario);

        factoryMockedStatic.when(() -> TipoUsuarioFactory.novo(
                eq(nomeTipo), anyString(), eq(true), eq(restauranteMock), eq(idUsuario)
        )).thenReturn(tipoUsuarioCriadoMock);

        when(tipoUsuariorepository.criar(tipoUsuarioCriadoMock)).thenReturn(tipoUsuarioCriadoMock);

        TipoUsuario resultado = useCase.run(dtoMock, email);

        assertThat(resultado).isEqualTo(tipoUsuarioCriadoMock);
        verify(tipoUsuariorepository).criar(tipoUsuarioCriadoMock);
    }

    @Test
    @DisplayName("Deve falhar quando TipoUsuario já existe no mesmo restaurante")
    void deveFalharTipoUsuarioJaExistente() {

        String nomeTipo = "Gerente";
        String email = "email@teste.com";
        Integer idRestaurante = 1;

        RestauranteDTO restauranteDtoMock = mock(RestauranteDTO.class);
        when(dtoMock.nome()).thenReturn(nomeTipo);
        when(dtoMock.restaurante()).thenReturn(restauranteDtoMock);
        when(restauranteDtoMock.id()).thenReturn(idRestaurante);

        when(tipoUsuariorepository.obterPorNome(nomeTipo)).thenReturn(Optional.of(tipoUsuarioCriadoMock));

        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.of(usuarioLogadoMock));

        when(restauranteRepository.obterPorId(idRestaurante)).thenReturn(Optional.of(restauranteMock));
        when(restauranteMock.getId()).thenReturn(idRestaurante);

        Restaurante restauranteDoTipoExistente = mock(Restaurante.class);
        when(tipoUsuarioCriadoMock.getRestaurante()).thenReturn(restauranteDoTipoExistente);
        when(restauranteDoTipoExistente.getId()).thenReturn(idRestaurante);

        assertThatThrownBy(() -> useCase.run(dtoMock, email))
                .isInstanceOf(NotificacaoException.class)
                .hasMessage(MensagemUtil.TIPO_USUARIO_JA_CADASTRADO);

        verify(usuarioRepository).obterPorEmail(email);
    }

    @Test
    @DisplayName("Deve falhar quando Usuário Logado não encontrado")
    void deveFalharUsuarioNaoEncontrado() {
        String email = "inexistente@teste.com";
        String nomeTipo = "Novo";
        
        when(dtoMock.nome()).thenReturn(nomeTipo);
        when(tipoUsuariorepository.obterPorNome(nomeTipo)).thenReturn(Optional.empty());
        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(dtoMock, email))
                .isInstanceOf(NotificacaoException.class);

        verify(restauranteRepository, never()).obterPorId(anyInt());
    }

    @Test
    @DisplayName("Deve falhar quando Restaurante não encontrado")
    void deveFalharRestauranteNaoEncontrado() {
        String email = "admin@teste.com";
        String nomeTipo = "Novo";
        Integer idRestaurante = 99;

        when(dtoMock.nome()).thenReturn(nomeTipo);
        when(dtoMock.restaurante()).thenReturn(restauranteDtoMock);
        when(restauranteDtoMock.id()).thenReturn(idRestaurante);

        when(tipoUsuariorepository.obterPorNome(nomeTipo)).thenReturn(Optional.empty());
        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.of(usuarioLogadoMock));
        when(restauranteRepository.obterPorId(idRestaurante)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(dtoMock, email))
                .isInstanceOf(NotificacaoException.class);

        verify(tipoUsuariorepository, never()).criar(any());
    }

    @Test
    @DisplayName("Deve falhar quando já existe um TipoUsuario com o mesmo nome no mesmo restaurante")
    void deveFalharNomeDuplicadoMesmoRestaurante() {

        String nomeTipo = "Gerente";
        String email = "admin@teste.com";
        Integer idRestaurante = 5;

        when(dtoMock.nome()).thenReturn(nomeTipo);
        when(dtoMock.restaurante()).thenReturn(restauranteDtoMock);
        when(restauranteDtoMock.id()).thenReturn(idRestaurante);

        TipoUsuario tipoUsuarioExistenteMock = mock(TipoUsuario.class);
        when(tipoUsuariorepository.obterPorNome(nomeTipo)).thenReturn(Optional.of(tipoUsuarioExistenteMock));

        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.of(usuarioLogadoMock));
        when(restauranteRepository.obterPorId(idRestaurante)).thenReturn(Optional.of(restauranteMock));
        
        when(restauranteMock.getId()).thenReturn(idRestaurante); // Restaurante do cadastro atual
        
        Restaurante restauranteDoTipoExistenteMock = mock(Restaurante.class);
        when(tipoUsuarioExistenteMock.getRestaurante()).thenReturn(restauranteDoTipoExistenteMock);
        when(restauranteDoTipoExistenteMock.getId()).thenReturn(idRestaurante); // Restaurante do tipo já existente

        assertThatThrownBy(() -> useCase.run(dtoMock, email))
                .isInstanceOf(NotificacaoException.class)
                .hasMessage(MensagemUtil.TIPO_USUARIO_JA_CADASTRADO);

        verify(tipoUsuariorepository, never()).criar(any());
    }
}