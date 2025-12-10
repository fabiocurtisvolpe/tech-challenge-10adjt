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

import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.TipoCozinha;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.GenericRepositoryPort;


@ExtendWith(MockitoExtension.class)
@DisplayName("AtivarInativarRestauranteUseCase - Testes Unitários")
class AtivarInativarRestauranteUseCaseTest {

    @Mock
    private GenericRepositoryPort<Restaurante> restauranteRepository;

    private AtivarInativarRestauranteUseCase useCase;
    private Restaurante restauranteExistente;

    @BeforeEach
    void setUp() {
        useCase = AtivarInativarRestauranteUseCase.create(restauranteRepository);

        Usuario dono = Usuario.builder()
                .id(1)
                .nome("Dono")
                .email("dono@test.com")
                .build();

        TipoCozinha tipoCozinha = TipoCozinha.builder()
                .id(1)
                .nome("ITALIANA")
                .descricao("Culinária italiana")
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
    }

    @Test
    @DisplayName("Deve ativar restaurante com sucesso")
    void testAtivarRestauranteComSucesso() {
        when(restauranteRepository.obterPorId(1)).thenReturn(Optional.of(restauranteExistente));

        Restaurante restauranteAtivado = Restaurante.builder()
                .id(1)
                .nome("Restaurante Test")
                .descricao("Ótimo restaurante")
                .horarioFuncionamento("11:00 - 22:00")
                .tipoCozinha(restauranteExistente.getTipoCozinha())
                .endereco(restauranteExistente.getEndereco())
                .dono(restauranteExistente.getDono())
                .ativo(true)
                .build();

        when(restauranteRepository.atualizar(any(Restaurante.class)))
                .thenReturn(restauranteAtivado);

        Restaurante resultado = useCase.run(1, true, 1);

        assertNotNull(resultado);
        assertTrue(resultado.getAtivo());
        verify(restauranteRepository, times(1)).atualizar(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve inativar restaurante com sucesso")
    void testInativarRestauranteComSucesso() {
        when(restauranteRepository.obterPorId(1)).thenReturn(Optional.of(restauranteExistente));

        Restaurante restauranteInativado = Restaurante.builder()
                .id(1)
                .nome("Restaurante Test")
                .descricao("Ótimo restaurante")
                .horarioFuncionamento("11:00 - 22:00")
                .tipoCozinha(restauranteExistente.getTipoCozinha())
                .endereco(restauranteExistente.getEndereco())
                .dono(restauranteExistente.getDono())
                .ativo(false)
                .build();

        when(restauranteRepository.atualizar(any(Restaurante.class)))
                .thenReturn(restauranteInativado);

        Restaurante resultado = useCase.run(1, false, 1);

        assertNotNull(resultado);
        assertFalse(resultado.getAtivo());
        verify(restauranteRepository, times(1)).atualizar(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando restaurante não existe")
    void testAtivarRestauranteNaoExistente() {
        when(restauranteRepository.obterPorId(999)).thenReturn(Optional.empty());

        assertThrows(NotificacaoException.class, () -> {
            useCase.run(999, true, 1);
        });

        verify(restauranteRepository, never()).atualizar(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve manter informações do restaurante ao inativar")
    void testMantendoInformacoesAoInativar() {
        when(restauranteRepository.obterPorId(1)).thenReturn(Optional.of(restauranteExistente));

        Restaurante restauranteInativado = Restaurante.builder()
                .id(1)
                .nome("Restaurante Test")
                .descricao("Ótimo restaurante")
                .horarioFuncionamento("11:00 - 22:00")
                .tipoCozinha(restauranteExistente.getTipoCozinha())
                .endereco(restauranteExistente.getEndereco())
                .dono(restauranteExistente.getDono())
                .ativo(false)
                .build();

        when(restauranteRepository.atualizar(any(Restaurante.class)))
                .thenReturn(restauranteInativado);

        Restaurante resultado = useCase.run(1, false, 1);

        assertEquals("Restaurante Test", resultado.getNome());
        assertEquals("Ótimo restaurante", resultado.getDescricao());
    }

    @Test
    @DisplayName("Deve chamar repositório atualizar apenas uma vez")
    void testRepositorioAtualizarChamadoApenasUmaVez() {
        when(restauranteRepository.obterPorId(1)).thenReturn(Optional.of(restauranteExistente));

        Restaurante restauranteAtivado = Restaurante.builder()
                .id(1)
                .nome("Restaurante Test")
                .descricao("Ótimo restaurante")
                .horarioFuncionamento("11:00 - 22:00")
                .tipoCozinha(restauranteExistente.getTipoCozinha())
                .endereco(restauranteExistente.getEndereco())
                .dono(restauranteExistente.getDono())
                .ativo(true)
                .build();

        when(restauranteRepository.atualizar(any(Restaurante.class)))
                .thenReturn(restauranteAtivado);

        useCase.run(1, true, 1);

        verify(restauranteRepository, times(1)).atualizar(any(Restaurante.class));
    }

}
