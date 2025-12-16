package com.postech.adjt.domain.usecase.usuario;

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
class ObterUsuarioPorEmailUseCaseTest {

    @Mock
    private GenericRepositoryPort<Usuario> usuarioRepository;

    private ObterUsuarioPorEmailUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = ObterUsuarioPorEmailUseCase.create(usuarioRepository);
    }

    @Test
    @DisplayName("Deve retornar usuário com sucesso quando email existe")
    void deveRetornarUsuarioQuandoExiste() {
        String email = "teste@email.com";
        Usuario usuarioMock = Usuario.builder().email(email).nome("Teste").build();

        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.of(usuarioMock));

        Optional<Usuario> resultado = useCase.run(email);

        assertThat(resultado).isNotNull();
        assertThat(resultado.get().getEmail()).isEqualTo(email);
        verify(usuarioRepository, times(1)).obterPorEmail(email);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o email for nulo")
    void deveFalharEmailNulo() {
        assertThatThrownBy(() -> useCase.run(null))
                .isInstanceOf(NotificacaoException.class);
        
        verify(usuarioRepository, never()).obterPorEmail(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o email for vazio ou em branco")
    void deveFalharEmailVazio() {
        assertThatThrownBy(() -> useCase.run(""))
                .isInstanceOf(NotificacaoException.class);

        assertThatThrownBy(() -> useCase.run("   "))
                .isInstanceOf(NotificacaoException.class);
        
        verify(usuarioRepository, never()).obterPorEmail(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não for encontrado no repositório")
    void deveFalharUsuarioNaoEncontrado() {
        String email = "inexistente@email.com";

        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(email))
                .isInstanceOf(NotificacaoException.class);
        
        verify(usuarioRepository, times(1)).obterPorEmail(email);
    }
}
