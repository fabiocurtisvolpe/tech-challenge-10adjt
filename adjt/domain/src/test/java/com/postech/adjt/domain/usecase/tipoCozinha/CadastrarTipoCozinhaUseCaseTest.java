package com.postech.adjt.domain.usecase.tipoCozinha;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.postech.adjt.domain.entidade.TipoCozinha;
import com.postech.adjt.domain.ports.GenericRepositoryPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("CadastrarTipoCozinhaUseCase - Testes Unitários")
class CadastrarTipoCozinhaUseCaseTest {

    @Mock
    private GenericRepositoryPort<TipoCozinha> tipoCozinhaRepository;

    private CadastrarTipoCozinhaUseCase useCase;
    private TipoCozinha novoTipoCozinha;

    @BeforeEach
    void setUp() {
        useCase = CadastrarTipoCozinhaUseCase.create(tipoCozinhaRepository);

        novoTipoCozinha = TipoCozinha.builder()
                .nome("ITALIANA")
                .descricao("Culinária italiana")
                .build();
    }

    @Test
    @DisplayName("Deve cadastrar novo tipo de cozinha com sucesso")
    void testCadastrarNovoTipoCozinhaComSucesso() {
        TipoCozinha tipoCozinhaCriada = TipoCozinha.builder()
                .id(1)
                .nome("ITALIANA")
                .descricao("Culinária italiana")
                .build();

        when(tipoCozinhaRepository.criar(any(TipoCozinha.class)))
                .thenReturn(tipoCozinhaCriada);

        TipoCozinha resultado = useCase.run(novoTipoCozinha);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("ITALIANA", resultado.getNome());
        verify(tipoCozinhaRepository, times(1)).criar(any(TipoCozinha.class));
    }

    @Test
    @DisplayName("Deve retornar tipo de cozinha com ID após criação")
    void testTipoCozinhaComIDAposCriacao() {
        TipoCozinha tipoCozinhaCriada = TipoCozinha.builder()
                .id(999)
                .nome("ITALIANA")
                .descricao("Culinária italiana")
                .build();

        when(tipoCozinhaRepository.criar(any(TipoCozinha.class)))
                .thenReturn(tipoCozinhaCriada);

        TipoCozinha resultado = useCase.run(novoTipoCozinha);

        assertNotNull(resultado.getId());
        assertEquals(999, resultado.getId());
    }

    @Test
    @DisplayName("Deve retornar tipo de cozinha com informações corretas")
    void testRetornarTipoCozinhaComInformacoesCorretas() {
        TipoCozinha tipoCozinhaCriada = TipoCozinha.builder()
                .id(1)
                .nome("ITALIANA")
                .descricao("Culinária italiana")
                .build();

        when(tipoCozinhaRepository.criar(any(TipoCozinha.class)))
                .thenReturn(tipoCozinhaCriada);

        TipoCozinha resultado = useCase.run(novoTipoCozinha);

        assertEquals("ITALIANA", resultado.getNome());
        assertEquals("Culinária italiana", resultado.getDescricao());
    }

    @Test
    @DisplayName("Deve chamar repositório criar apenas uma vez")
    void testRepositorioCriarChamadoApenasUmaVez() {
        TipoCozinha tipoCozinhaCriada = TipoCozinha.builder()
                .id(1)
                .nome("ITALIANA")
                .descricao("Culinária italiana")
                .build();

        when(tipoCozinhaRepository.criar(any(TipoCozinha.class)))
                .thenReturn(tipoCozinhaCriada);

        useCase.run(novoTipoCozinha);

        verify(tipoCozinhaRepository, times(1)).criar(any(TipoCozinha.class));
    }

}
