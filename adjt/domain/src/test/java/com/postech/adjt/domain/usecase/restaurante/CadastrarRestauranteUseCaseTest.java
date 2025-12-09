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
import com.postech.adjt.domain.ports.UsuarioRepositoryPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("CadastrarRestauranteUseCase - Testes Unitários")
class CadastrarRestauranteUseCaseTest {

    @Mock
    private RestauranteRepositoryPort restauranteRepository;

    @Mock
    private UsuarioRepositoryPort usuarioRepository;

    private CadastrarRestauranteUseCase useCase;
    private RestauranteDTO novoRestauranteDTO;
    private Usuario dono;
    private TipoCozinha tipoCozinha;
    private Endereco endereco;

    @BeforeEach
    void setUp() {
        useCase = CadastrarRestauranteUseCase.create(restauranteRepository, usuarioRepository);

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

        novoRestauranteDTO = new RestauranteDTO(null, "Restaurante Test", "Ótimo restaurante",
                "11:00 - 22:00", tipoCozinha, endereco, 1);
    }

    @Test
    @DisplayName("Deve cadastrar novo restaurante com sucesso")
    void testCadastrarNovoRestauranteComSucesso() {
        when(restauranteRepository.obterPorNome("Restaurante Test")).thenReturn(Optional.empty());
        when(usuarioRepository.obterPorId(1)).thenReturn(Optional.of(dono));

        Restaurante restauranteCriado = Restaurante.builder()
                .id(1)
                .nome("Restaurante Test")
                .descricao("Ótimo restaurante")
                .horarioFuncionamento("11:00 - 22:00")
                .tipoCozinha(tipoCozinha)
                .endereco(endereco)
                .dono(dono)
                .ativo(true)
                .build();

        when(restauranteRepository.criar(any(Restaurante.class)))
                .thenReturn(restauranteCriado);

        Restaurante resultado = useCase.run(novoRestauranteDTO);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("Restaurante Test", resultado.getNome());
        verify(restauranteRepository, times(1)).criar(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando restaurante já existe")
    void testCadastrarComRestauranteExistente() {
        Restaurante restauranteExistente = Restaurante.builder()
                .id(1)
                .nome("Restaurante Test")
                .dono(dono)
                .build();

        when(restauranteRepository.obterPorNome("Restaurante Test")).thenReturn(Optional.of(restauranteExistente));

        assertThrows(NotificacaoException.class, () -> {
            useCase.run(novoRestauranteDTO);
        });

        verify(restauranteRepository, never()).criar(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não existe")
    void testCadastrarComUsuarioNaoExistente() {
        when(restauranteRepository.obterPorNome("Restaurante Test")).thenReturn(Optional.empty());
        when(usuarioRepository.obterPorId(999)).thenReturn(Optional.empty());

        RestauranteDTO dto = new RestauranteDTO(null, "Restaurante Test", "Ótimo restaurante",
                "11:00 - 22:00", tipoCozinha, endereco, 999);

        assertThrows(NotificacaoException.class, () -> {
            useCase.run(dto);
        });

        verify(restauranteRepository, never()).criar(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve retornar restaurante com ID após criação")
    void testRestauranteComIDAposCriacao() {
        when(restauranteRepository.obterPorNome("Restaurante Test")).thenReturn(Optional.empty());
        when(usuarioRepository.obterPorId(1)).thenReturn(Optional.of(dono));

        Restaurante restauranteCriado = Restaurante.builder()
                .id(999)
                .nome("Restaurante Test")
                .descricao("Ótimo restaurante")
                .horarioFuncionamento("11:00 - 22:00")
                .tipoCozinha(tipoCozinha)
                .endereco(endereco)
                .dono(dono)
                .ativo(true)
                .build();

        when(restauranteRepository.criar(any(Restaurante.class)))
                .thenReturn(restauranteCriado);

        Restaurante resultado = useCase.run(novoRestauranteDTO);

        assertNotNull(resultado.getId());
        assertEquals(999, resultado.getId());
    }

}
