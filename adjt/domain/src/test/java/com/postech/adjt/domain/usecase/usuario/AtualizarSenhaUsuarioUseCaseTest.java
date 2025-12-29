package com.postech.adjt.domain.usecase.usuario;

import com.postech.adjt.domain.dto.TrocarSenhaUsuarioDTO;
import com.postech.adjt.domain.entidade.TipoUsuario;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarSenhaUsuarioUseCaseTest {

    @Mock
    private GenericRepositoryPort<Usuario> usuarioRepository;

    @Mock
    private TipoUsuario tipoUsuarioMock;

    private AtualizarSenhaUsuarioUseCase useCase;
    private MockedStatic<UsuarioFactory> factoryMockedStatic;

    @BeforeEach
    void setUp() {
        useCase = AtualizarSenhaUsuarioUseCase.create(usuarioRepository);
        factoryMockedStatic = Mockito.mockStatic(UsuarioFactory.class);
    }

    @AfterEach
    void tearDown() {
        if (factoryMockedStatic != null) {
            factoryMockedStatic.close();
        }
    }

    @Test
    @DisplayName("Deve atualizar a senha com sucesso quando usuário existe")
    void deveAtualizarSenhaComSucesso() {
        String email = "teste@email.com";
        String novaSenhaCodificada = "novaSenhaHash";
        
        // Mock do DTO (assumindo record ou class com getters)
        TrocarSenhaUsuarioDTO dto = mock(TrocarSenhaUsuarioDTO.class);
        when(dto.email()).thenReturn(email);
        when(dto.senhaCodificada()).thenReturn(novaSenhaCodificada);

        Usuario usuarioExistente = Usuario.builder()
                .id(1)
                .email(email)
                .senha("senhaAntiga")
                .tipoUsuario(tipoUsuarioMock)
                .ativo(true)
                .build();

        Usuario usuarioComNovaSenha = Usuario.builder()
                .id(1)
                .email(email)
                .senha(novaSenhaCodificada)
                .tipoUsuario(tipoUsuarioMock)
                .ativo(true)
                .build();

        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.of(usuarioExistente));
        
        // Simula o comportamento da Factory retornando o objeto atualizado
        factoryMockedStatic.when(() -> UsuarioFactory.usuario(
                eq(usuarioExistente.getId()),
                eq(usuarioExistente.getNome()),
                eq(usuarioExistente.getEmail()),
                eq(novaSenhaCodificada),
                eq(usuarioExistente.getTipoUsuario()),
                eq(usuarioExistente.getEnderecos()),
                eq(usuarioExistente.getAtivo())
        )).thenReturn(usuarioComNovaSenha);

        when(usuarioRepository.atualizar(any(Usuario.class))).thenReturn(usuarioComNovaSenha);

        Usuario resultado = useCase.run(dto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getSenha()).isEqualTo(novaSenhaCodificada);
        
        verify(usuarioRepository).atualizar(usuarioComNovaSenha);
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não for encontrado")
    void deveFalharUsuarioNaoEncontrado() {
        String email = "inexistente@email.com";
        TrocarSenhaUsuarioDTO dto = mock(TrocarSenhaUsuarioDTO.class);
        when(dto.email()).thenReturn(email);

        when(usuarioRepository.obterPorEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.run(dto))
                .isInstanceOf(NotificacaoException.class);

        verify(usuarioRepository, never()).atualizar(any());
    }
}