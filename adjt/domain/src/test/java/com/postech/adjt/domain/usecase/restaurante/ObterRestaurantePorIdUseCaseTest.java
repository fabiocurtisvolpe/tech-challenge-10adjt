package com.postech.adjt.domain.usecase.restaurante;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.enums.TipoCozinhaEnum;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("ObterRestaurantePorIdUseCase - Testes Unitários")
class ObterRestaurantePorIdUseCaseTest {

    @Mock
    private GenericRepositoryPort<Restaurante> restauranteRepository;

    private ObterRestaurantePorIdUseCase useCase;
    private Restaurante restauranteValido;

    @BeforeEach
    void setUp() {
        useCase = ObterRestaurantePorIdUseCase.create(restauranteRepository);

        Usuario dono = Usuario.builder()
                .id(1)
                .nome("Dono")
                .email("dono@test.com")
                .build();

        Endereco endereco = Endereco.builder()
                .id(1)
                .logradouro("Rua A")
                .numero("100")
                .bairro("Centro")
                .municipio("São Paulo")
                .uf("SP")
                .cep("01000-000")
                .build();

        restauranteValido = Restaurante.builder()
                .id(1)
                .nome("Restaurante Test")
                .descricao("Ótimo restaurante")
                .horarioFuncionamento("11:00 - 22:00")
                .tipoCozinha(TipoCozinhaEnum.AMERICANA)
                .endereco(endereco)
                .dono(dono)
                .ativo(true)
                .build();
    }

    @Test
    @DisplayName("Deve obter restaurante por ID com sucesso")
    void testObterRestaurantePorIdComSucesso() {
        when(restauranteRepository.obterPorId(1)).thenReturn(Optional.of(restauranteValido));

        Optional<Restaurante> resultado = useCase.run(1);

        assertTrue(resultado.isPresent());
        assertEquals("Restaurante Test", resultado.get().getNome());
        verify(restauranteRepository, times(1)).obterPorId(1);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID é nulo")
    void testObterRestauranteComIDNulo() {
        assertThrows(NotificacaoException.class, () -> {
            useCase.run(null);
        });

        verify(restauranteRepository, never()).obterPorId(anyInt());
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID é inválido")
    void testObterRestauranteComIDInvalido() {
        assertThrows(NotificacaoException.class, () -> {
            useCase.run(0);
        });

        assertThrows(NotificacaoException.class, () -> {
            useCase.run(-1);
        });

        verify(restauranteRepository, never()).obterPorId(anyInt());
    }

    @Test
    @DisplayName("Deve lançar exceção quando restaurante não existe")
    void testObterRestauranteNaoExistente() {
        when(restauranteRepository.obterPorId(999)).thenReturn(Optional.empty());

        assertThrows(NotificacaoException.class, () -> {
            useCase.run(999);
        });

        verify(restauranteRepository, times(1)).obterPorId(999);
    }

    @Test
    @DisplayName("Deve retornar restaurante com informações corretas")
    void testRetornarRestauranteComInformacoesCorretas() {
        when(restauranteRepository.obterPorId(1)).thenReturn(Optional.of(restauranteValido));

        Optional<Restaurante> resultado = useCase.run(1);

        assertTrue(resultado.isPresent());
        assertEquals(1, resultado.get().getId());
        assertEquals("Restaurante Test", resultado.get().getNome());
        assertEquals("11:00 - 22:00", resultado.get().getHorarioFuncionamento());
    }

}
