package com.postech.adjt.data.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.postech.adjt.data.entidade.EnderecoEntidade;
import com.postech.adjt.data.entidade.RestauranteEntirade; // Mantendo o nome conforme sua classe (Typo: Entirade)
import com.postech.adjt.data.entidade.TipoCozinhaEntidade;
import com.postech.adjt.data.entidade.UsuarioEntidade;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.TipoCozinha;
import com.postech.adjt.domain.entidade.Usuario;

class RestauranteMapperTest {

    @Test
    @DisplayName("toDomain: Deve converter entidade para domínio com sucesso")
    void toDomain_DeveConverterEntidadeParaDominio() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();

        // Mocks dos objetos retornados pelos outros mappers
        TipoCozinha tipoCozinhaDomain = mock(TipoCozinha.class);
        Endereco enderecoDomain = mock(Endereco.class);
        Usuario donoDomain = mock(Usuario.class);

        // Mocks das entidades internas
        TipoCozinhaEntidade tipoCozinhaEntidade = mock(TipoCozinhaEntidade.class);
        EnderecoEntidade enderecoEntidade = mock(EnderecoEntidade.class);
        UsuarioEntidade donoEntidade = mock(UsuarioEntidade.class);

        RestauranteEntirade entidade = new RestauranteEntirade();
        entidade.setId(1);
        entidade.setNome("Restaurante Teste");
        entidade.setDescricao("Descrição Teste");
        entidade.setHorarioFuncionamento("{\"seg\": \"09-18\"}");
        entidade.setTipoCozinha(tipoCozinhaEntidade);
        entidade.setEndereco(enderecoEntidade);
        entidade.setDono(donoEntidade);
        entidade.setDataCriacao(agora);
        entidade.setDataAlteracao(agora);

        // Mock Static: Precisamos interceptar as chamadas para os outros mappers
        // Usamos try-with-resources aninhado para garantir que todos sejam fechados
        try (MockedStatic<TipoCozinhaMapper> tcMock = mockStatic(TipoCozinhaMapper.class);
             MockedStatic<EnderecoMapper> endMock = mockStatic(EnderecoMapper.class);
             MockedStatic<UsuarioMapper> userMock = mockStatic(UsuarioMapper.class)) {

            // Configurando comportamento dos mocks estáticos
            tcMock.when(() -> TipoCozinhaMapper.toDomain(any())).thenReturn(tipoCozinhaDomain);
            endMock.when(() -> EnderecoMapper.toDomain(any())).thenReturn(enderecoDomain);
            userMock.when(() -> UsuarioMapper.toDomain(any())).thenReturn(donoDomain);

            // Act
            Restaurante domain = RestauranteMapper.toDomain(entidade);

            // Assert
            assertNotNull(domain);
            assertEquals(entidade.getId(), domain.getId());
            assertEquals(entidade.getNome(), domain.getNome());
            assertEquals(entidade.getDescricao(), domain.getDescricao());
            assertEquals(entidade.getHorarioFuncionamento(), domain.getHorarioFuncionamento());
            assertEquals(entidade.getDataCriacao(), domain.getDataCriacao());
            assertEquals(entidade.getDataAlteracao(), domain.getDataAlteracao());
            
            // Verifica se os objetos aninhados foram mapeados corretamente pelos mocks
            assertEquals(tipoCozinhaDomain, domain.getTipoCozinha());
            assertEquals(enderecoDomain, domain.getEndereco());
            assertEquals(donoDomain, domain.getDono());
        }
    }

    @Test
    @DisplayName("toDomain: Deve retornar null quando a entrada for null")
    void toDomain_DeveRetornarNull_QuandoInputNull() {
        assertNull(RestauranteMapper.toDomain(null));
    }

    @Test
    @DisplayName("toEntity: Deve converter domínio para entidade com sucesso")
    void toEntity_DeveConverterDominioParaEntidade() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();

        // Mocks dos objetos de domínio internos
        TipoCozinha tipoCozinhaDomain = mock(TipoCozinha.class);
        Endereco enderecoDomain = mock(Endereco.class);
        Usuario donoDomain = mock(Usuario.class);

        // Mocks das entidades retornadas pelos outros mappers
        TipoCozinhaEntidade tipoCozinhaEntidade = mock(TipoCozinhaEntidade.class);
        EnderecoEntidade enderecoEntidade = mock(EnderecoEntidade.class);
        UsuarioEntidade donoEntidade = mock(UsuarioEntidade.class);

        Restaurante domain = Restaurante.builder()
                .id(99)
                .nome("Cantina Italiana")
                .descricao("Melhor massa")
                .horarioFuncionamento("{}")
                .tipoCozinha(tipoCozinhaDomain)
                .endereco(enderecoDomain)
                .dono(donoDomain)
                .dataCriacao(agora)
                .dataAlteracao(agora)
                .build();

        try (MockedStatic<TipoCozinhaMapper> tcMock = mockStatic(TipoCozinhaMapper.class);
             MockedStatic<EnderecoMapper> endMock = mockStatic(EnderecoMapper.class);
             MockedStatic<UsuarioMapper> userMock = mockStatic(UsuarioMapper.class)) {

            // Configurando comportamento dos mocks estáticos para toEntity
            tcMock.when(() -> TipoCozinhaMapper.toEntity(any())).thenReturn(tipoCozinhaEntidade);
            endMock.when(() -> EnderecoMapper.toEntity(any())).thenReturn(enderecoEntidade);
            userMock.when(() -> UsuarioMapper.toEntity(any())).thenReturn(donoEntidade);

            // Act
            RestauranteEntirade entidade = RestauranteMapper.toEntity(domain);

            // Assert
            assertNotNull(entidade);
            assertEquals(domain.getId(), entidade.getId());
            assertEquals(domain.getNome(), entidade.getNome());
            assertEquals(domain.getDescricao(), entidade.getDescricao());
            assertEquals(domain.getHorarioFuncionamento(), entidade.getHorarioFuncionamento());
            assertEquals(domain.getDataCriacao(), entidade.getDataCriacao());
            assertEquals(domain.getDataAlteracao(), entidade.getDataAlteracao());

            // Verifica relacionamentos
            assertEquals(tipoCozinhaEntidade, entidade.getTipoCozinha());
            assertEquals(enderecoEntidade, entidade.getEndereco());
            assertEquals(donoEntidade, entidade.getDono());
        }
    }

    @Test
    @DisplayName("toEntity: Deve retornar null quando a entrada for null")
    void toEntity_DeveRetornarNull_QuandoInputNull() {
        assertNull(RestauranteMapper.toEntity(null));
    }
}