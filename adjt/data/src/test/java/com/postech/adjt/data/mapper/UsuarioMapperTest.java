package com.postech.adjt.data.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.postech.adjt.data.entidade.EnderecoEntidade;
import com.postech.adjt.data.entidade.TipoUsuarioEntidade;
import com.postech.adjt.data.entidade.UsuarioEntidade;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.Usuario;

class UsuarioMapperTest {

    @Test
    @DisplayName("toEntity: Deve converter usuario domain para entidade com enderecos e vinculo")
    void toEntity_DeveConverterCorretamente() {
        LocalDateTime agora = LocalDateTime.now();

        TipoUsuario tipoUsuario = mock(TipoUsuario.class);
        TipoUsuarioEntidade tipoUsuarioEntidade = mock(TipoUsuarioEntidade.class);

        Endereco endereco = Endereco.builder()
                .id(10)
                .logradouro("Rua Teste")
                .numero("123")
                .complemento("Apto 1")
                .bairro("Centro")
                .pontoReferencia("Mercado")
                .cep("12345-678")
                .municipio("Sao Paulo")
                .uf("SP")
                .principal(true)
                .ativo(true)
                .dataCriacao(agora)
                .dataAlteracao(agora)
                .build();

        Usuario usuario = Usuario.builder()
                .id(1)
                .nome("Teste")
                .email("teste@email.com")
                .senha("123456")
                .tipoUsuario(tipoUsuario)
                .ativo(true)
                .dataCriacao(agora)
                .dataAlteracao(agora)
                .enderecos(List.of(endereco))
                .build();

        try (MockedStatic<TipoUsuarioMapper> tipoMapperMock = mockStatic(TipoUsuarioMapper.class)) {
            tipoMapperMock.when(() -> TipoUsuarioMapper.toEntity(any())).thenReturn(tipoUsuarioEntidade);

            UsuarioEntidade entidade = UsuarioMapper.toEntity(usuario);

            assertNotNull(entidade);
            assertEquals(usuario.getId(), entidade.getId());
            assertEquals(usuario.getNome(), entidade.getNome());
            assertEquals(usuario.getEmail(), entidade.getEmail());
            assertEquals(usuario.getSenha(), entidade.getSenha());
            assertEquals(usuario.getAtivo(), entidade.getAtivo());
            assertEquals(usuario.getDataCriacao(), entidade.getDataCriacao());
            assertEquals(usuario.getDataAlteracao(), entidade.getDataAlteracao());
            assertEquals(tipoUsuarioEntidade, entidade.getTipoUsuario());
            
            assertNotNull(entidade.getEnderecos());
            assertEquals(1, entidade.getEnderecos().size());

            EnderecoEntidade endEntidade = entidade.getEnderecos().get(0);
            assertEquals(endereco.getId(), endEntidade.getId());
            assertEquals(endereco.getLogradouro(), endEntidade.getLogradouro());
            assertEquals(endereco.getCep(), endEntidade.getCep());
            
            assertEquals(entidade, endEntidade.getUsuario());
        }
    }

    @Test
    @DisplayName("toEntity: Deve retornar null se usuario for null")
    void toEntity_DeveRetornarNull() {
        assertNull(UsuarioMapper.toEntity(null));
    }

    @Test
    @DisplayName("toEntity: Deve lidar com lista de enderecos nula")
    void toEntity_SemEnderecos() {
        Usuario usuario = Usuario.builder().id(1).build();

        try (MockedStatic<TipoUsuarioMapper> tipoMapperMock = mockStatic(TipoUsuarioMapper.class)) {
            UsuarioEntidade entidade = UsuarioMapper.toEntity(usuario);
            assertNotNull(entidade);
            assertNull(entidade.getEnderecos());
        }
    }

    @Test
    @DisplayName("toDomain: Deve converter entidade para domain com enderecos")
    void toDomain_DeveConverterCorretamente() {
        LocalDateTime agora = LocalDateTime.now();

        TipoUsuario tipoUsuario = mock(TipoUsuario.class);
        TipoUsuarioEntidade tipoUsuarioEntidade = mock(TipoUsuarioEntidade.class);

        EnderecoEntidade enderecoEntidade = new EnderecoEntidade();
        enderecoEntidade.setId(10);
        enderecoEntidade.setLogradouro("Rua Domain");
        enderecoEntidade.setNumero("321");
        enderecoEntidade.setComplemento("Casa");
        enderecoEntidade.setBairro("Bairro");
        enderecoEntidade.setCep("87654-321");
        enderecoEntidade.setMunicipio("Rio");
        enderecoEntidade.setUf("RJ");
        enderecoEntidade.setPrincipal(false);
        enderecoEntidade.setAtivo(true);

        UsuarioEntidade usuarioEntidade = new UsuarioEntidade();
        usuarioEntidade.setId(1);
        usuarioEntidade.setNome("Domain User");
        usuarioEntidade.setEmail("domain@email.com");
        usuarioEntidade.setSenha("654321");
        usuarioEntidade.setTipoUsuario(tipoUsuarioEntidade);
        usuarioEntidade.setAtivo(true);
        usuarioEntidade.setDataCriacao(agora);
        usuarioEntidade.setDataAlteracao(agora);
        usuarioEntidade.setEnderecos(Collections.singletonList(enderecoEntidade));

        try (MockedStatic<TipoUsuarioMapper> tipoMapperMock = mockStatic(TipoUsuarioMapper.class)) {
            tipoMapperMock.when(() -> TipoUsuarioMapper.toDomain(any())).thenReturn(tipoUsuario);

            Usuario domain = UsuarioMapper.toDomain(usuarioEntidade);

            assertNotNull(domain);
            assertEquals(usuarioEntidade.getId(), domain.getId());
            assertEquals(usuarioEntidade.getNome(), domain.getNome());
            assertEquals(usuarioEntidade.getEmail(), domain.getEmail());
            assertEquals(tipoUsuario, domain.getTipoUsuario());
            
            assertNotNull(domain.getEnderecos());
            assertEquals(1, domain.getEnderecos().size());
            
            Endereco endDomain = domain.getEnderecos().get(0);
            assertEquals(enderecoEntidade.getId(), endDomain.getId());
            assertEquals(enderecoEntidade.getLogradouro(), endDomain.getLogradouro());
            assertEquals(enderecoEntidade.getNumero(), endDomain.getNumero());
        }
    }

    @Test
    @DisplayName("toDomain: Deve retornar null se entidade for null")
    void toDomain_DeveRetornarNull() {
        assertNull(UsuarioMapper.toDomain(null));
    }

    @Test
    @DisplayName("toDomain: Deve lidar com lista de enderecos nula")
    void toDomain_SemEnderecos() {
        UsuarioEntidade entidade = new UsuarioEntidade();
        entidade.setId(1);
        entidade.setEnderecos(null);

        try (MockedStatic<TipoUsuarioMapper> tipoMapperMock = mockStatic(TipoUsuarioMapper.class)) {
            Usuario domain = UsuarioMapper.toDomain(entidade);
            assertNotNull(domain);
            assertNull(domain.getEnderecos());
        }
    }
}