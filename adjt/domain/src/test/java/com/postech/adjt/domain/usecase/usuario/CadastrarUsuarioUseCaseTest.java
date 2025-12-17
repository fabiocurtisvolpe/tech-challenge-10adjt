package com.postech.adjt.domain.usecase.usuario;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.dto.TipoUsuarioDTO;
import com.postech.adjt.domain.dto.UsuarioDTO;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.EnderecoFactory;
import com.postech.adjt.domain.factory.UsuarioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarUsuarioUseCaseTest {

    @Mock
    private GenericRepositoryPort<Usuario> usuarioRepository;

    @Mock
    private GenericRepositoryPort<TipoUsuario> tipoUsuarioRepository;

    @Mock
    private TipoUsuario tipoUsuarioMock;

    @Mock
    private Usuario usuarioCriadoMock;

    private CadastrarUsuarioUseCase useCase;
    private MockedStatic<UsuarioFactory> usuarioFactoryMockedStatic;
    private MockedStatic<EnderecoFactory> enderecoFactoryMockedStatic;

    @BeforeEach
    void setUp() {
        useCase = CadastrarUsuarioUseCase.create(usuarioRepository, tipoUsuarioRepository);
        usuarioFactoryMockedStatic = mockStatic(UsuarioFactory.class);
        enderecoFactoryMockedStatic = mockStatic(EnderecoFactory.class);
    }

    @AfterEach
    void tearDown() {
        usuarioFactoryMockedStatic.close();
        enderecoFactoryMockedStatic.close();
    }

    @Test
    @DisplayName("Deve cadastrar usuário com sucesso quando dados são válidos")
    void deveCadastrarUsuarioComSucesso() {
        UsuarioDTO dto = mock(UsuarioDTO.class);
        TipoUsuarioDTO tipoDto = mock(TipoUsuarioDTO.class);

        when(dto.email()).thenReturn("teste@teste.com");
        when(dto.nome()).thenReturn("Teste");
        when(dto.senha()).thenReturn("123456");
        when(dto.tipoUsuario()).thenReturn(tipoDto);
        when(dto.enderecos()).thenReturn(Collections.emptyList());
        when(tipoDto.id()).thenReturn(1);

        List<Endereco> enderecos = Collections.emptyList();

        // O repositório DEVE retornar vazio para permitir o cadastro
        when(usuarioRepository.obterPorEmail(dto.email())).thenReturn(Optional.empty());

        when(tipoUsuarioRepository.obterPorId(1)).thenReturn(Optional.of(tipoUsuarioMock));

        enderecoFactoryMockedStatic.when(() -> EnderecoFactory.toEnderecoList(any()))
                .thenReturn(enderecos);

        usuarioFactoryMockedStatic.when(() -> UsuarioFactory.novo(dto.nome(), dto.email(), dto.senha(), tipoUsuarioMock, enderecos))
                .thenReturn(usuarioCriadoMock);

        when(usuarioRepository.criar(usuarioCriadoMock)).thenReturn(usuarioCriadoMock);

        Usuario resultado = useCase.run(dto);

        assertThat(resultado).isEqualTo(usuarioCriadoMock);
        verify(usuarioRepository).criar(usuarioCriadoMock);
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário já existe")
    void deveFalharUsuarioJaExistente() {
        UsuarioDTO dto = mock(UsuarioDTO.class);
        when(dto.email()).thenReturn("existente@teste.com");

        // O repositório retorna um usuário, simulando que já existe
        when(usuarioRepository.obterPorEmail(dto.email())).thenReturn(Optional.of(mock(Usuario.class)));

        assertThatThrownBy(() -> useCase.run(dto))
                .isInstanceOf(NotificacaoException.class)
                .hasMessage(MensagemUtil.USUARIO_EXISTENTE);

        verify(tipoUsuarioRepository, never()).obterPorId(anyInt());
        verify(usuarioRepository, never()).criar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando TipoUsuario não é encontrado")
    void deveFalharTipoUsuarioNaoEncontrado() {
        UsuarioDTO dto = mock(UsuarioDTO.class);
        TipoUsuarioDTO tipoDto = mock(TipoUsuarioDTO.class);

        when(dto.email()).thenReturn("novo@teste.com");
        when(dto.tipoUsuario()).thenReturn(tipoDto);
        when(tipoDto.id()).thenReturn(99);

        when(usuarioRepository.obterPorEmail(dto.email())).thenReturn(Optional.empty());

        when(tipoUsuarioRepository.obterPorId(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(dto))
                .isInstanceOf(NotificacaoException.class)
                .hasMessage(MensagemUtil.TIPO_USUARIO_NAO_ENCONTRADO);

        verify(usuarioRepository, never()).criar(any());
    }
}