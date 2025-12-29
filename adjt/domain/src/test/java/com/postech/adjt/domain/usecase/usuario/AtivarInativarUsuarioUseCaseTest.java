package com.postech.adjt.domain.usecase.usuario;

import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.TipoUsuarioDonoRestaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.factory.UsuarioFactory;
import com.postech.adjt.domain.ports.GenericRepositoryPort;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtivarInativarUsuarioUseCaseTest {

    @Mock
    private GenericRepositoryPort<Usuario> usuarioRepository;

    @Mock
    private TipoUsuarioDonoRestaurante tipoDonoMock;

    @Mock
    private TipoUsuario tipoComumMock;

    private AtivarInativarUsuarioUseCase useCase;
    private MockedStatic<UsuarioFactory> factoryMockedStatic;

    @BeforeEach
    void setUp() {
        useCase = AtivarInativarUsuarioUseCase.create(usuarioRepository);
        // Mock estático para evitar a validação real dentro da Factory durante o teste do UseCase
        factoryMockedStatic = Mockito.mockStatic(UsuarioFactory.class);
    }

    @AfterEach
    void tearDown() {
        if (factoryMockedStatic != null) {
            factoryMockedStatic.close();
        }
    }

    @Test
    @DisplayName("Deve ativar usuário quando o solicitante for Dono de Restaurante")
    void deveAtivarQuandoSolicitanteForDono() {
        Integer idAlvo = 2;
        String emailLogado = "dono@teste.com";

        Usuario usuarioAlvo = Usuario.builder().id(idAlvo).nome("Alvo").email("alvo@teste.com").build();
        Usuario usuarioDono = Usuario.builder().id(1).email(emailLogado).tipoUsuario(tipoDonoMock).build();
        Usuario usuarioAtualizado = Usuario.builder().id(idAlvo).ativo(true).build();

        when(usuarioRepository.obterPorId(idAlvo)).thenReturn(Optional.of(usuarioAlvo));
        when(usuarioRepository.obterPorEmail(emailLogado)).thenReturn(Optional.of(usuarioDono));
        
        // Simula o comportamento da Factory
        factoryMockedStatic.when(() -> UsuarioFactory.usuario(
                eq(usuarioAlvo.getId()), any(), any(), any(), any(), any(), eq(true)
        )).thenReturn(usuarioAtualizado);

        when(usuarioRepository.atualizar(any(Usuario.class))).thenReturn(usuarioAtualizado);

        Usuario resultado = useCase.run(true, idAlvo, emailLogado);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getAtivo()).isTrue();
        verify(usuarioRepository).atualizar(usuarioAtualizado);
    }

    @Test
    @DisplayName("Deve desativar usuário quando o solicitante for o próprio usuário")
    void deveDesativarQuandoSolicitanteForProprio() {
        Integer id = 1;
        String email = "eu@teste.com";

        Usuario usuario = Usuario.builder().id(id).email(email).tipoUsuario(tipoComumMock).build();
        Usuario usuarioDesativado = Usuario.builder().id(id).ativo(false).build();

        when(usuarioRepository.obterPorId(id)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.of(usuario)); // Retorna o mesmo objeto

        factoryMockedStatic.when(() -> UsuarioFactory.usuario(
                eq(id), any(), any(), any(), any(), any(), eq(false)
        )).thenReturn(usuarioDesativado);

        when(usuarioRepository.atualizar(any(Usuario.class))).thenReturn(usuarioDesativado);

        Usuario resultado = useCase.run(false, id, email);

        assertThat(resultado).isEqualTo(usuarioDesativado);
        verify(usuarioRepository).atualizar(usuarioDesativado);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário alvo não for encontrado")
    void deveFalharUsuarioAlvoNaoEncontrado() {
        Integer idInexistente = 99;
        String email = "qualquer@teste.com";

        when(usuarioRepository.obterPorId(idInexistente)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(true, idInexistente, email))
                .isInstanceOf(NotificacaoException.class);

        verify(usuarioRepository, never()).obterPorEmail(anyString());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário logado não for encontrado")
    void deveFalharUsuarioLogadoNaoEncontrado() {
        Integer idAlvo = 1;
        String emailInexistente = "fantasma@teste.com";
        Usuario usuarioAlvo = Usuario.builder().id(idAlvo).build();

        when(usuarioRepository.obterPorId(idAlvo)).thenReturn(Optional.of(usuarioAlvo));
        when(usuarioRepository.obterPorEmail(emailInexistente)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(true, idAlvo, emailInexistente))
                .isInstanceOf(NotificacaoException.class);
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário comum tenta alterar outro usuário")
    void deveFalharSemPermissao() {
        Integer idAlvo = 2;
        String emailLogado = "comum@teste.com";

        Usuario usuarioAlvo = Usuario.builder().id(idAlvo).tipoUsuario(tipoComumMock).build();

        Usuario usuarioLogado = Usuario.builder().id(1).email(emailLogado).tipoUsuario(tipoComumMock).build();

        when(usuarioRepository.obterPorId(idAlvo)).thenReturn(Optional.of(usuarioAlvo));
        when(usuarioRepository.obterPorEmail(emailLogado)).thenReturn(Optional.of(usuarioLogado));

        assertThatThrownBy(() -> useCase.run(true, idAlvo, emailLogado))
                .isInstanceOf(NotificacaoException.class); // Espera mensagem de permissão negada

        verify(usuarioRepository, never()).atualizar(any());
    }
}