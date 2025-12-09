package com.postech.adjt.domain.usecase.cardapio;

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

import com.postech.adjt.domain.dto.CardapioDTO;
import com.postech.adjt.domain.entidade.Cardapio;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.CardapioRepositoryPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("CadastrarCardapioUseCase - Testes Unitários")
class CadastrarCardapioUseCaseTest {

    @Mock
    private CardapioRepositoryPort cardapioRepository;

    private CadastrarCardapioUseCase useCase;
    private CardapioDTO novoCardapioDTO;
    private Restaurante restaurante;
    private Usuario dono;

    @BeforeEach
    void setUp() {
        useCase = CadastrarCardapioUseCase.create(cardapioRepository);

        dono = Usuario.builder()
                .id(1)
                .nome("Dono")
                .email("dono@test.com")
                .build();

        restaurante = Restaurante.builder()
                .id(1)
                .nome("Restaurante Test")
                .dono(dono)
                .build();

        novoCardapioDTO = new CardapioDTO(null, "Pizza", "Pizza deliciosa", 25.0, 
                "url_foto", true, restaurante, 1);
    }

    @Test
    @DisplayName("Deve cadastrar novo cardápio com sucesso")
    void testCadastrarNovoCardapioComSucesso() {
        when(cardapioRepository.obterPorNome("Pizza")).thenReturn(Optional.empty());

        Cardapio cardapioCriado = Cardapio.builder()
                .id(1)
                .nome("Pizza")
                .descricao("Pizza deliciosa")
                .preco(25.0)
                .foto("url_foto")
                .disponivel(true)
                .restaurante(restaurante)
                .build();

        when(cardapioRepository.criar(any(Cardapio.class)))
                .thenReturn(cardapioCriado);

        Cardapio resultado = useCase.run(novoCardapioDTO);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("Pizza", resultado.getNome());
        verify(cardapioRepository, times(1)).criar(any(Cardapio.class));
    }

    @Test
    @DisplayName("Deve retornar cardápio com ID após criação")
    void testCardapioComIDAposCriacao() {
        when(cardapioRepository.obterPorNome("Pizza")).thenReturn(Optional.empty());

        Cardapio cardapioCriado = Cardapio.builder()
                .id(999)
                .nome("Pizza")
                .descricao("Pizza deliciosa")
                .preco(25.0)
                .foto("url_foto")
                .disponivel(true)
                .restaurante(restaurante)
                .build();

        when(cardapioRepository.criar(any(Cardapio.class)))
                .thenReturn(cardapioCriado);

        Cardapio resultado = useCase.run(novoCardapioDTO);

        assertNotNull(resultado.getId());
        assertEquals(999, resultado.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção quando cardápio já existe")
    void testCadastrarComCardapioExistente() {
        Cardapio cardapioExistente = Cardapio.builder()
                .id(1)
                .nome("Pizza")
                .descricao("Pizza deliciosa")
                .preco(25.0)
                .restaurante(restaurante)
                .build();

        when(cardapioRepository.obterPorNome("Pizza")).thenReturn(Optional.of(cardapioExistente));

        assertThrows(NotificacaoException.class, () -> {
            useCase.run(novoCardapioDTO);
        });

        verify(cardapioRepository, never()).criar(any(Cardapio.class));
    }

    @Test
    @DisplayName("Deve chamar repositório criar apenas uma vez")
    void testRepositorioCriarChamadoApenasUmaVez() {
        when(cardapioRepository.obterPorNome("Pizza")).thenReturn(Optional.empty());

        Cardapio cardapioCriado = Cardapio.builder()
                .id(1)
                .nome("Pizza")
                .descricao("Pizza deliciosa")
                .preco(25.0)
                .foto("url_foto")
                .disponivel(true)
                .restaurante(restaurante)
                .build();

        when(cardapioRepository.criar(any(Cardapio.class)))
                .thenReturn(cardapioCriado);

        useCase.run(novoCardapioDTO);

        verify(cardapioRepository, times(1)).criar(any(Cardapio.class));
    }

}
