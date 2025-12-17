package com.postech.adjt.data.mapper;

import com.postech.adjt.data.entidade.EnderecoEntidade;
import com.postech.adjt.data.entidade.RestauranteEntidade;
import com.postech.adjt.data.entidade.UsuarioEntidade;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.enums.TipoCozinhaEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class RestauranteMapperTest {

    @Test
    @DisplayName("toDomain: Deve converter RestauranteEntidade para Restaurante (Domain) corretamente")
    void toDomain_DeveConverterEntidadeParaDominio() {

        LocalDateTime agora = LocalDateTime.now();

        Endereco enderecoDomain = mock(Endereco.class);
        Usuario donoDomain = mock(Usuario.class);

        EnderecoEntidade enderecoEntidade = mock(EnderecoEntidade.class);
        UsuarioEntidade donoEntidade = mock(UsuarioEntidade.class);

        RestauranteEntidade entidade = new RestauranteEntidade();
        entidade.setId(1);
        entidade.setNome("Restaurante Teste");
        entidade.setDescricao("Melhor comida");
        entidade.setHorarioFuncionamento("09:00-18:00");
        entidade.setTipoCozinha(TipoCozinhaEnum.BRASILEIRA);
        entidade.setEndereco(enderecoEntidade);
        entidade.setDono(donoEntidade);
        entidade.setDataCriacao(agora);
        entidade.setDataAlteracao(agora);

        try (MockedStatic<EnderecoMapper> enderecoMapperMock = mockStatic(EnderecoMapper.class);
             MockedStatic<UsuarioMapper> usuarioMapperMock = mockStatic(UsuarioMapper.class)) {

            enderecoMapperMock.when(() -> EnderecoMapper.toDomain(enderecoEntidade)).thenReturn(enderecoDomain);
            usuarioMapperMock.when(() -> UsuarioMapper.toDomain(donoEntidade)).thenReturn(donoDomain);

            Restaurante domain = RestauranteMapper.toDomain(entidade);

            assertNotNull(domain);
            assertEquals(entidade.getId(), domain.getId());
            assertEquals(entidade.getNome(), domain.getNome());
            assertEquals(entidade.getDescricao(), domain.getDescricao());
            assertEquals(entidade.getHorarioFuncionamento(), domain.getHorarioFuncionamento());
            assertEquals(entidade.getTipoCozinha(), domain.getTipoCozinha());
            assertEquals(entidade.getDataCriacao(), domain.getDataCriacao());
            assertEquals(entidade.getDataAlteracao(), domain.getDataAlteracao());
            
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
    @DisplayName("toEntity: Deve converter Restaurante (Domain) para RestauranteEntidade corretamente")
    void toEntity_DeveConverterDominioParaEntidade() {

        LocalDateTime agora = LocalDateTime.now();

        Endereco enderecoDomain = mock(Endereco.class);
        Usuario donoDomain = mock(Usuario.class);

        EnderecoEntidade enderecoEntidade = mock(EnderecoEntidade.class);
        UsuarioEntidade donoEntidade = mock(UsuarioEntidade.class);

        Restaurante domain = Restaurante.builder()
                .id(1)
                .nome("Restaurante Domain")
                .descricao("Descrição Domain")
                .horarioFuncionamento("10:00-22:00")
                .tipoCozinha(TipoCozinhaEnum.ITALIANA)
                .endereco(enderecoDomain)
                .dono(donoDomain)
                .dataCriacao(agora)
                .dataAlteracao(agora)
                .build();

        try (MockedStatic<EnderecoMapper> enderecoMapperMock = mockStatic(EnderecoMapper.class);
             MockedStatic<UsuarioMapper> usuarioMapperMock = mockStatic(UsuarioMapper.class)) {

            enderecoMapperMock.when(() -> EnderecoMapper.toEntity(enderecoDomain)).thenReturn(enderecoEntidade);
            usuarioMapperMock.when(() -> UsuarioMapper.toEntity(donoDomain)).thenReturn(donoEntidade);

            RestauranteEntidade entidade = RestauranteMapper.toEntity(domain);

            assertNotNull(entidade);
            assertEquals(domain.getId(), entidade.getId());
            assertEquals(domain.getNome(), entidade.getNome());
            assertEquals(domain.getDescricao(), entidade.getDescricao());
            assertEquals(domain.getHorarioFuncionamento(), entidade.getHorarioFuncionamento());
            assertEquals(domain.getTipoCozinha(), entidade.getTipoCozinha());
            assertEquals(domain.getDataCriacao(), entidade.getDataCriacao());
            assertEquals(domain.getDataAlteracao(), entidade.getDataAlteracao());

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