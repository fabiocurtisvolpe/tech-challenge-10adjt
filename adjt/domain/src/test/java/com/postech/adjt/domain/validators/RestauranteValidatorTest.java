package com.postech.adjt.domain.validators;

import com.postech.adjt.domain.constants.MensagemUtil;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.TipoCozinha;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RestauranteValidator - Testes Unitários")
class RestauranteValidatorTest {

    private Usuario dono;
    private TipoCozinha tipoCozinha;
    private Endereco endereco;
    private Restaurante restauranteValido;
    private Integer idUsuarioLogado;

    @BeforeEach
    void setUp() {
        TipoUsuario tipoUsuario = TipoUsuario.builder()
                .id(1)
                .nome("DONO")
                .descricao("Dono de restaurante")
                .build();

        dono = Usuario.builder()
                .id(1)
                .nome("João Silva")
                .email("joao@email.com")
                .senha("senha123")
                .tipoUsuario(tipoUsuario)
                .isDono(true)
                .build();

        idUsuarioLogado = 1;

        tipoCozinha = TipoCozinha.builder()
                .id(1)
                .nome("Italiana")
                .descricao("Culinária italiana")
                .build();

        endereco = Endereco.builder()
                .logradouro("Rua A")
                .numero("123")
                .bairro("Centro")
                .cep("12345-678")
                .municipio("São Paulo")
                .uf("SP")
                .principal(true)
                .build();

        restauranteValido = Restaurante.builder()
                .nome("Restaurante Italiano")
                .horarioFuncionamento("11:00 - 23:00")
                .tipoCozinha(tipoCozinha)
                .endereco(endereco)
                .dono(dono)
                .build();
    }

    @Test
    @DisplayName("Deve validar restaurante válido com sucesso")
    void testValidarRestauranteValido() {
        // Act & Assert
        assertDoesNotThrow(() -> RestauranteValidator.validar(restauranteValido, idUsuarioLogado));
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar restaurante nulo")
    void testValidarRestauranteNulo() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            RestauranteValidator.validar(null, idUsuarioLogado);
        });
        assertEquals(MensagemUtil.RESTAURANTE_NULO, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar restaurante com nome nulo")
    void testValidarRestauranteComNomeNulo() {
        // Arrange
        Restaurante restaurante = Restaurante.builder()
                .nome(null)
                .horarioFuncionamento("11:00 - 23:00")
                .tipoCozinha(tipoCozinha)
                .endereco(endereco)
                .dono(dono)
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            RestauranteValidator.validar(restaurante, idUsuarioLogado);
        });
        assertEquals(MensagemUtil.NOME_RESTAURANTE_OBRIGATORIO, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar restaurante com nome vazio")
    void testValidarRestauranteComNomeVazio() {
        // Arrange
        Restaurante restaurante = Restaurante.builder()
                .nome("  ")
                .horarioFuncionamento("11:00 - 23:00")
                .tipoCozinha(tipoCozinha)
                .endereco(endereco)
                .dono(dono)
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            RestauranteValidator.validar(restaurante, idUsuarioLogado);
        });
        assertEquals(MensagemUtil.NOME_RESTAURANTE_OBRIGATORIO, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar restaurante com nome menor que 3 caracteres")
    void testValidarRestauranteComNomeMuitoCurto() {
        // Arrange
        Restaurante restaurante = Restaurante.builder()
                .nome("AB")
                .horarioFuncionamento("11:00 - 23:00")
                .tipoCozinha(tipoCozinha)
                .endereco(endereco)
                .dono(dono)
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            RestauranteValidator.validar(restaurante, idUsuarioLogado);
        });
        assertEquals(MensagemUtil.NOME_MINIMO_CARACTERES, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar restaurante com nome maior que 50 caracteres")
    void testValidarRestauranteComNomeMuitoLongo() {
        // Arrange
        String nomeLongo = "A".repeat(51);
        Restaurante restaurante = Restaurante.builder()
                .nome(nomeLongo)
                .horarioFuncionamento("11:00 - 23:00")
                .tipoCozinha(tipoCozinha)
                .endereco(endereco)
                .dono(dono)
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            RestauranteValidator.validar(restaurante, idUsuarioLogado);
        });
        assertEquals(MensagemUtil.NOME_MAXIMO_CARACTERES, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar restaurante com horário nulo")
    void testValidarRestauranteComHorarioNulo() {
        // Arrange
        Restaurante restaurante = Restaurante.builder()
                .nome("Restaurante Italiano")
                .horarioFuncionamento(null)
                .tipoCozinha(tipoCozinha)
                .endereco(endereco)
                .dono(dono)
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            RestauranteValidator.validar(restaurante, idUsuarioLogado);
        });
        assertEquals(MensagemUtil.HORARIO_FUNCIONAMENTO_OBRIGATORIO, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar restaurante com horário vazio")
    void testValidarRestauranteComHorarioVazio() {
        // Arrange
        Restaurante restaurante = Restaurante.builder()
                .nome("Restaurante Italiano")
                .horarioFuncionamento("  ")
                .tipoCozinha(tipoCozinha)
                .endereco(endereco)
                .dono(dono)
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            RestauranteValidator.validar(restaurante, idUsuarioLogado);
        });
        assertEquals(MensagemUtil.HORARIO_FUNCIONAMENTO_OBRIGATORIO, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar restaurante com tipo de cozinha nulo")
    void testValidarRestauranteComTipoCozinhaNulo() {
        // Arrange
        Restaurante restaurante = Restaurante.builder()
                .nome("Restaurante Italiano")
                .horarioFuncionamento("11:00 - 23:00")
                .tipoCozinha(null)
                .endereco(endereco)
                .dono(dono)
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            RestauranteValidator.validar(restaurante, idUsuarioLogado);
        });
        assertEquals(MensagemUtil.TIPO_COZINHA_OBRIGATORIO, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar restaurante com endereço nulo")
    void testValidarRestauranteComEnderecoNulo() {
        // Arrange
        Restaurante restaurante = Restaurante.builder()
                .nome("Restaurante Italiano")
                .horarioFuncionamento("11:00 - 23:00")
                .tipoCozinha(tipoCozinha)
                .endereco(null)
                .dono(dono)
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            RestauranteValidator.validar(restaurante, idUsuarioLogado);
        });
        assertEquals(MensagemUtil.ENDERECO_OBRIGATORIO, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar restaurante com dono nulo")
    void testValidarRestauranteComDonoNulo() {
        // Arrange
        Restaurante restaurante = Restaurante.builder()
                .nome("Restaurante Italiano")
                .horarioFuncionamento("11:00 - 23:00")
                .tipoCozinha(tipoCozinha)
                .endereco(endereco)
                .dono(null)
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            RestauranteValidator.validar(restaurante, idUsuarioLogado);
        });
        assertEquals(MensagemUtil.DONO_RESTAURANTE_OBRIGATORIO, exception.getMessage());
    }

    @Test
    @DisplayName("Deve validar restaurante com nome exatamente 3 caracteres")
    void testValidarRestauranteComNomeExatamente3Caracteres() {
        // Arrange
        Restaurante restaurante = Restaurante.builder()
                .nome("ABC")
                .horarioFuncionamento("11:00 - 23:00")
                .tipoCozinha(tipoCozinha)
                .endereco(endereco)
                .dono(dono)
                .build();

        // Act & Assert
        assertDoesNotThrow(() -> RestauranteValidator.validar(restaurante, idUsuarioLogado));
    }

    @Test
    @DisplayName("Deve validar restaurante com nome exatamente 50 caracteres")
    void testValidarRestauranteComNomeExatamente50Caracteres() {
        // Arrange
        String nome50 = "A".repeat(50);
        Restaurante restaurante = Restaurante.builder()
                .nome(nome50)
                .horarioFuncionamento("11:00 - 23:00")
                .tipoCozinha(tipoCozinha)
                .endereco(endereco)
                .dono(dono)
                .build();

        // Act & Assert
        assertDoesNotThrow(() -> RestauranteValidator.validar(restaurante, idUsuarioLogado));
    }
}
