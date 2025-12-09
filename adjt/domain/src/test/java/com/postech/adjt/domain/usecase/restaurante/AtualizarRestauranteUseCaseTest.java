package com.postech.adjt.domain.usecase.restaurante;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.postech.adjt.domain.dto.RestauranteDTO;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.TipoCozinha;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.RestauranteRepositoryPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("AtualizarRestauranteUseCase - Testes Unitários")
class AtualizarRestauranteUseCaseTest {

    @Mock
    private RestauranteRepositoryPort restauranteRepository;

    private AtualizarRestauranteUseCase useCase;
    private RestauranteDTO restauranteAtualizadoDTO;
    private Restaurante restauranteExistente;
    private Usuario dono;
    private TipoCozinha tipoCozinha;
    private Endereco endereco;

    @BeforeEach
    void setUp() {
        useCase = AtualizarRestauranteUseCase.create(restauranteRepository);

        dono = Usuario.builder()
                .id(1)
                .nome("Dono")
                .email("dono@test.com")
                .build();

        tipoCozinha = TipoCozinha.builder()
                .id(1)
                .nome("ITALIANA")
                .descricao("Culinária italiana")
                .build();

        endereco = Endereco.builder()
                .id(1)
                .logradouro("Rua A")
                .numero("100")
                .bairro("Centro")
                .municipio("São Paulo")
                .uf("SP")
                .cep("01000-000")
                .build();

        restauranteExistente = Restaurante.builder()
                .id(1)
                .nome("Restaurante Test")
                .descricao("Ótimo restaurante")
                .horarioFuncionamento("11:00 - 22:00")
                .tipoCozinha(tipoCozinha)
                .endereco(endereco)
                .dono(dono)
                .ativo(true)
                .build();

        restauranteAtualizadoDTO = new RestauranteDTO(1, "Restaurante Premium", "Restaurante premium",
                "12:00 - 23:00", tipoCozinha, endereco, 1);
    }

    @Test
    @DisplayName("Deve atualizar restaurante com sucesso")
    void testAtualizarRestauranteComSucesso() {
        when(restauranteRepository.obterPorId(1)).thenReturn(Optional.of(restauranteExistente));

        Restaurante restauranteAtualizado = Restaurante.builder()
                .id(1)
                .nome("Restaurante Premium")
                .descricao("Restaurante premium")
                .horarioFuncionamento("12:00 - 23:00")
                .tipoCozinha(tipoCozinha)
                .endereco(endereco)
                .dono(dono)
                .ativo(true)
                .build();

        when(restauranteRepository.atualizar(any(Restaurante.class)))
                .thenReturn(restauranteAtualizado);

        Restaurante resultado = useCase.run(restauranteAtualizadoDTO);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        verify(restauranteRepository, times(1)).atualizar(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando restaurante não existe")
    void testAtualizarRestauranteNaoExistente() {
        when(restauranteRepository.obterPorId(1)).thenReturn(Optional.empty());

        assertThrows(NotificacaoException.class, () -> {
            useCase.run(restauranteAtualizadoDTO);
        });

        verify(restauranteRepository, never()).atualizar(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve manter ID ao atualizar restaurante")
    void testMantendoIDRestaurante() {
        when(restauranteRepository.obterPorId(1)).thenReturn(Optional.of(restauranteExistente));

        Restaurante restauranteAtualizado = Restaurante.builder()
                .id(1)
                .nome("Restaurante Premium")
                .descricao("Restaurante premium")
                .horarioFuncionamento("12:00 - 23:00")
                .tipoCozinha(tipoCozinha)
                .endereco(endereco)
                .dono(dono)
                .ativo(true)
                .build();

        when(restauranteRepository.atualizar(any(Restaurante.class)))
                .thenReturn(restauranteAtualizado);

        Restaurante resultado = useCase.run(restauranteAtualizadoDTO);

        assertEquals(1, resultado.getId());
    }

    @Test
    @DisplayName("Deve chamar repositório atualizar apenas uma vez")
    void testRepositorioAtualizarChamadoApenasUmaVez() {
        when(restauranteRepository.obterPorId(1)).thenReturn(Optional.of(restauranteExistente));

        Restaurante restauranteAtualizado = Restaurante.builder()
                .id(1)
                .nome("Restaurante Premium")
                .descricao("Restaurante premium")
                .horarioFuncionamento("12:00 - 23:00")
                .tipoCozinha(tipoCozinha)
                .endereco(endereco)
                .dono(dono)
                .ativo(true)
                .build();

        when(restauranteRepository.atualizar(any(Restaurante.class)))
                .thenReturn(restauranteAtualizado);

        useCase.run(restauranteAtualizadoDTO);

        verify(restauranteRepository, times(1)).atualizar(any(Restaurante.class));
    }

}
