package com.postech.adjt.domain.validators;

import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class UsuarioValidatorTest {

    @Mock
    private TipoUsuario tipoUsuarioMock;

    private static final String NOME_VALIDO = "João da Silva";
    private static final String EMAIL_VALIDO = "joao@email.com";
    private static final String SENHA_VALIDA = "123456"; // Assumindo min 6
    private static final Integer ID_VALIDO = 1;

    @Nested
    @DisplayName("Método: validarParaCriacao")
    class ValidarParaCriacaoTests {

        @Test
        @DisplayName("Deve validar com sucesso um usuário correto para criação")
        void deveValidarUsuarioCorreto() {
            Usuario usuario = Usuario.builder()
                    .nome(NOME_VALIDO)
                    .email(EMAIL_VALIDO)
                    .senha(SENHA_VALIDA)
                    .tipoUsuario(tipoUsuarioMock)
                    .build();

            assertThatCode(() -> UsuarioValidator.validarParaCriacao(usuario))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Deve lançar exceção quando usuário for nulo")
        void deveFalharUsuarioNulo() {
            assertThatThrownBy(() -> UsuarioValidator.validarParaCriacao(null))
                    .isInstanceOf(NotificacaoException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção quando TipoUsuario for nulo")
        void deveFalharTipoUsuarioNulo() {
            Usuario usuario = Usuario.builder()
                    .nome(NOME_VALIDO)
                    .email(EMAIL_VALIDO)
                    .senha(SENHA_VALIDA)
                    .tipoUsuario(null) // Inválido
                    .build();

            assertThatThrownBy(() -> UsuarioValidator.validarParaCriacao(usuario))
                    .isInstanceOf(NotificacaoException.class);
        }
        
        @Test
        @DisplayName("Deve lançar exceção para senha nula ou vazia")
        void deveFalharSenhaVazia() {
            Usuario usuario = Usuario.builder()
                    .nome(NOME_VALIDO)
                    .email(EMAIL_VALIDO)
                    .senha("") // Inválido
                    .tipoUsuario(tipoUsuarioMock)
                    .build();

            assertThatThrownBy(() -> UsuarioValidator.validarParaCriacao(usuario))
                    .isInstanceOf(NotificacaoException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção para senha curta")
        void deveFalharSenhaCurta() {
            // Assumindo que TamanhoUtil.SENHA_MINIMA_LENGTH seja > 1
            Usuario usuario = Usuario.builder()
                    .nome(NOME_VALIDO)
                    .email(EMAIL_VALIDO)
                    .senha("1") // Provavelmente inválido
                    .tipoUsuario(tipoUsuarioMock)
                    .build();

            assertThatThrownBy(() -> UsuarioValidator.validarParaCriacao(usuario))
                    .isInstanceOf(NotificacaoException.class);
        }
    }

    @Nested
    @DisplayName("Método: validarUsuario (Edição/Geral)")
    class ValidarUsuarioTests {

        @Test
        @DisplayName("Deve validar com sucesso um usuário existente com ID")
        void deveValidarUsuarioExistente() {
            Usuario usuario = Usuario.builder()
                    .id(ID_VALIDO)
                    .nome(NOME_VALIDO)
                    .email(EMAIL_VALIDO)
                    .tipoUsuario(tipoUsuarioMock)
                    .build();

            assertThatCode(() -> UsuarioValidator.validarUsuario(usuario))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Deve lançar exceção quando ID for nulo")
        void deveFalharIdNulo() {
            Usuario usuario = Usuario.builder()
                    .id(null) // Inválido
                    .nome(NOME_VALIDO)
                    .email(EMAIL_VALIDO)
                    .tipoUsuario(tipoUsuarioMock)
                    .build();

            assertThatThrownBy(() -> UsuarioValidator.validarUsuario(usuario))
                    .isInstanceOf(NotificacaoException.class)
                    .hasMessageContaining("ID do usuário não pode ser nulo");
        }

        @Test
        @DisplayName("Deve lançar exceção quando ID for zero ou negativo")
        void deveFalharIdInvalido() {
            Usuario usuario = Usuario.builder()
                    .id(0) // Inválido
                    .nome(NOME_VALIDO)
                    .email(EMAIL_VALIDO)
                    .tipoUsuario(tipoUsuarioMock)
                    .build();

            assertThatThrownBy(() -> UsuarioValidator.validarUsuario(usuario))
                    .isInstanceOf(NotificacaoException.class)
                    .hasMessageContaining("ID do usuário deve ser maior que zero");
        }
    }

    @Nested
    @DisplayName("Validações Comuns (Nome e Email)")
    class ValidacoesComunsTests {

        @Test
        @DisplayName("Deve lançar exceção para nome nulo ou vazio")
        void deveFalharNomeVazio() {
            Usuario usuario = Usuario.builder()
                    .nome("") // Inválido
                    .email(EMAIL_VALIDO)
                    .senha(SENHA_VALIDA)
                    .tipoUsuario(tipoUsuarioMock)
                    .build();

            assertThatThrownBy(() -> UsuarioValidator.validarParaCriacao(usuario))
                    .isInstanceOf(NotificacaoException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção para nome muito curto")
        void deveFalharNomeCurto() {
            // Assumindo TamanhoUtil.NOME_MINIMO_LENGTH > 1
            Usuario usuario = Usuario.builder()
                    .nome("A") // Inválido
                    .email(EMAIL_VALIDO)
                    .senha(SENHA_VALIDA)
                    .tipoUsuario(tipoUsuarioMock)
                    .build();

            assertThatThrownBy(() -> UsuarioValidator.validarParaCriacao(usuario))
                    .isInstanceOf(NotificacaoException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção para nome muito longo")
        void deveFalharNomeLongo() {
            // Gera uma string com 200 caracteres (assumindo que estoura o limite)
            String nomeGigante = "a".repeat(200);
            
            Usuario usuario = Usuario.builder()
                    .nome(nomeGigante)
                    .email(EMAIL_VALIDO)
                    .senha(SENHA_VALIDA)
                    .tipoUsuario(tipoUsuarioMock)
                    .build();

            assertThatThrownBy(() -> UsuarioValidator.validarParaCriacao(usuario))
                    .isInstanceOf(NotificacaoException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção para email nulo ou vazio")
        void deveFalharEmailVazio() {
            Usuario usuario = Usuario.builder()
                    .nome(NOME_VALIDO)
                    .email("") // Inválido
                    .senha(SENHA_VALIDA)
                    .tipoUsuario(tipoUsuarioMock)
                    .build();

            assertThatThrownBy(() -> UsuarioValidator.validarParaCriacao(usuario))
                    .isInstanceOf(NotificacaoException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção para formato de email inválido")
        void deveFalharEmailFormatoInvalido() {
            Usuario usuario = Usuario.builder()
                    .nome(NOME_VALIDO)
                    .email("email-invalido.com") // Sem @
                    .senha(SENHA_VALIDA)
                    .tipoUsuario(tipoUsuarioMock)
                    .build();

            assertThatThrownBy(() -> UsuarioValidator.validarParaCriacao(usuario))
                    .isInstanceOf(NotificacaoException.class);
        }
    }
}