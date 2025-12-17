package com.postech.adjt.domain.usecase.usuario;

import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.TipoUsuarioDonoRestaurante;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObterUsuarioPorIdUseCaseTest {

    @Mock
    private GenericRepositoryPort<Usuario> usuarioRepository;

    @Mock
    private TipoUsuarioDonoRestaurante tipoDonoMock;

    @Mock
    private TipoUsuario tipoComumMock;

    private ObterUsuarioPorIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = ObterUsuarioPorIdUseCase.create(usuarioRepository);
    }

    @Test
    @DisplayName("Deve retornar usuário quando solicitante é dono e ID existe")
    void deveRetornarUsuarioComSucesso() {
        Integer idAlvo = 10;
        String emailLogado = "dono@teste.com";

        Usuario usuarioDono = Usuario.builder()
                .email(emailLogado)
                .tipoUsuario(tipoDonoMock)
                .build();

        Usuario usuarioAlvo = Usuario.builder()
                .id(idAlvo)
                .nome("Usuario Alvo")
                .build();

        when(usuarioRepository.obterPorEmail(emailLogado)).thenReturn(Optional.of(usuarioDono));
        when(usuarioRepository.obterPorId(idAlvo)).thenReturn(Optional.of(usuarioAlvo));

        Usuario resultado = useCase.run(idAlvo, emailLogado);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(idAlvo);
        assertThat(resultado.getNome()).isEqualTo("Usuario Alvo");
    }

    @Test
    @DisplayName("Deve falhar quando ID fornecido for nulo")
    void deveFalharIdNulo() {
        assertThatThrownBy(() -> useCase.run(null, "email@teste.com"))
                .isInstanceOf(NotificacaoException.class); // MensagemUtil.ID_NULO

        verify(usuarioRepository, never()).obterPorEmail(any());
    }

    @Test
    @DisplayName("Deve falhar quando ID fornecido for zero ou negativo")
    void deveFalharIdInvalido() {
        assertThatThrownBy(() -> useCase.run(0, "email@teste.com"))
                .isInstanceOf(NotificacaoException.class);

        verify(usuarioRepository, never()).obterPorEmail(any());
    }

    @Test
    @DisplayName("Deve falhar quando usuário logado não for encontrado")
    void deveFalharUsuarioLogadoNaoEncontrado() {
        String emailInexistente = "fantasma@teste.com";
        when(usuarioRepository.obterPorEmail(emailInexistente)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(1, emailInexistente))
                .isInstanceOf(NotificacaoException.class); // MensagemUtil.USUARIO_NAO_ENCONTRADO

        verify(usuarioRepository, never()).obterPorId(any());
    }

    @Test
    @DisplayName("Deve falhar quando usuário logado não for Dono de Restaurante")
    void deveFalharPermissaoNegada() {
        String emailComum = "comum@teste.com";
        Usuario usuarioComum = Usuario.builder()
                .email(emailComum)
                .tipoUsuario(tipoComumMock) // Não é TipoUsuarioDonoRestaurante
                .build();

        when(usuarioRepository.obterPorEmail(emailComum)).thenReturn(Optional.of(usuarioComum));

        assertThatThrownBy(() -> useCase.run(1, emailComum))
                .isInstanceOf(NotificacaoException.class); // MensagemUtil.USUARIO_NAO_PERMITE_OPERACAO

        verify(usuarioRepository, never()).obterPorId(any());
    }

    @Test
    @DisplayName("Deve falhar quando usuário alvo não for encontrado pelo ID")
    void deveFalharUsuarioAlvoNaoEncontrado() {
        Integer idInexistente = 99;
        String emailLogado = "dono@teste.com";

        Usuario usuarioDono = Usuario.builder()
                .email(emailLogado)
                .tipoUsuario(tipoDonoMock)
                .build();

        when(usuarioRepository.obterPorEmail(emailLogado)).thenReturn(Optional.of(usuarioDono));
        when(usuarioRepository.obterPorId(idInexistente)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(idInexistente, emailLogado))
                .isInstanceOf(NotificacaoException.class); // MensagemUtil.USUARIO_NAO_ENCONTRADO
    }
}