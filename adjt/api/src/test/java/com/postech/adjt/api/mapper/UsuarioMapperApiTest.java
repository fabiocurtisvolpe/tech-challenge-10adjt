package com.postech.adjt.api.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.postech.adjt.api.dto.UsuarioRespostaDTO;
import com.postech.adjt.api.payload.EnderecoPayLoad;
import com.postech.adjt.api.payload.usuario.AtualizaUsuarioPayLoad;
import com.postech.adjt.api.payload.usuario.NovoUsuarioPayLoad;
import com.postech.adjt.api.payload.usuario.TipoUsuarioPayLoad;
import com.postech.adjt.domain.dto.UsuarioDTO;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.Usuario;

class UsuarioMapperApiTest {

    @Test
    @DisplayName("Deve converter NovoUsuarioPayLoad para UsuarioDTO corretamente")
    void testToNovoUsuarioDTO() {
        NovoUsuarioPayLoad payload = new NovoUsuarioPayLoad();
        payload.setNome("Teste Silva");
        payload.setEmail("teste@email.com");
        payload.setSenha("senha123");

        TipoUsuarioPayLoad tipoPayload = mock(TipoUsuarioPayLoad.class);
        when(tipoPayload.getId()).thenReturn(1);
        when(tipoPayload.getNome()).thenReturn("ADMIN");
        when(tipoPayload.getDescricao()).thenReturn("Administrador");
        when(tipoPayload.getIsDono()).thenReturn(true);
        payload.setTipoUsuario(tipoPayload);

        EnderecoPayLoad enderecoPayload = new EnderecoPayLoad();
        enderecoPayload.setLogradouro("Rua A");
        enderecoPayload.setNumero("123");
        enderecoPayload.setPrincipal(true);
        payload.setEnderecos(List.of(enderecoPayload));

        String senhaEncriptada = "senhaCriptografada";

        UsuarioDTO result = UsuarioMapperApi.toNovoUsuarioDTO(payload, senhaEncriptada);

        assertNotNull(result);
        assertNull(result.id());
        assertEquals(payload.getNome(), result.nome());
        assertEquals(payload.getEmail(), result.email());
        assertEquals(senhaEncriptada, result.senha());
        assertEquals(true, result.ativo());
        
        assertNotNull(result.tipoUsuario());
        assertEquals(1, result.tipoUsuario().id());
        assertEquals("ADMIN", result.tipoUsuario().nome());
        
        assertNotNull(result.enderecos());
        assertEquals(1, result.enderecos().size());
        assertEquals("Rua A", result.enderecos().get(0).logradouro());
    }

    @Test
    @DisplayName("Deve converter AtualizaUsuarioPayLoad para UsuarioDTO corretamente")
    void testToAtualizaUsuarioDTO() {
        AtualizaUsuarioPayLoad payload = mock(AtualizaUsuarioPayLoad.class);
        when(payload.getNome()).thenReturn("Nome Atualizado");
        when(payload.getEmail()).thenReturn("novo@email.com");
        when(payload.getAtivo()).thenReturn(false);

        EnderecoPayLoad enderecoPayload = new EnderecoPayLoad();
        enderecoPayload.setLogradouro("Rua B");
        when(payload.getEnderecos()).thenReturn(List.of(enderecoPayload));

        UsuarioDTO result = UsuarioMapperApi.toAtualizaUsuarioDTO(payload);

        assertNotNull(result);
        assertNull(result.id());
        assertEquals("Nome Atualizado", result.nome());
        assertEquals("novo@email.com", result.email());
        assertNull(result.senha()); 
        assertNull(result.tipoUsuario());
        assertEquals(false, result.ativo());
        
        assertNotNull(result.enderecos());
        assertEquals(1, result.enderecos().size());
        assertEquals("Rua B", result.enderecos().get(0).logradouro());
    }

    @Test
    @DisplayName("Deve converter Usuario entidade para UsuarioRespostaDTO corretamente")
    void testToUsuarioRespostaDTO() {
        Usuario usuario = mock(Usuario.class);
        TipoUsuario tipoUsuario = mock(TipoUsuario.class);
        Endereco endereco = mock(Endereco.class);
        LocalDateTime dataAlteracao = LocalDateTime.now();

        when(usuario.getId()).thenReturn(10);
        when(usuario.getNome()).thenReturn("Usuario Resposta");
        when(usuario.getEmail()).thenReturn("resp@email.com");
        when(usuario.getTipoUsuario()).thenReturn(tipoUsuario);
        when(usuario.getDataAlteracao()).thenReturn(dataAlteracao);

        when(endereco.getLogradouro()).thenReturn("Rua C");
        when(endereco.getNumero()).thenReturn("10");
        when(endereco.getPrincipal()).thenReturn(true);
        
        when(usuario.getEnderecos()).thenReturn(List.of(endereco));

        UsuarioRespostaDTO result = UsuarioMapperApi.toUsuarioRespostaDTO(usuario);

        assertNotNull(result);
        assertEquals(10, result.getId());
        assertEquals("Usuario Resposta", result.getNome());
        assertEquals("resp@email.com", result.getEmail());
        assertEquals(tipoUsuario, result.getTipoUsuario());
        assertEquals(dataAlteracao, result.getDataAlteracao());
        
        assertNotNull(result.getEnderecos());
        assertEquals(1, result.getEnderecos().size());
        assertEquals("Rua C", result.getEnderecos().get(0).getLogradouro());
        assertTrue(result.getEnderecos().get(0).getPrincipal());
    }

    @Test
    @DisplayName("Deve retornar null ao converter Usuario nulo para RespostaDTO")
    void testToUsuarioRespostaDTO_NullInput() {
        UsuarioRespostaDTO result = UsuarioMapperApi.toUsuarioRespostaDTO(null);
        assertNull(result);
    }

    @Test
    @DisplayName("Deve lidar com lista de endereços nula ao converter para RespostaDTO")
    void testToUsuarioRespostaDTO_NullEnderecos() {
        Usuario usuario = mock(Usuario.class);
        when(usuario.getId()).thenReturn(1);
        when(usuario.getNome()).thenReturn("Teste");
        when(usuario.getEnderecos()).thenReturn(null);

        UsuarioRespostaDTO result = UsuarioMapperApi.toUsuarioRespostaDTO(usuario);

        assertNotNull(result);
        assertNull(result.getEnderecos());
    }
    
    @Test
    @DisplayName("Deve retornar null ao converter lista de endereços nula no Payload")
    void testToNovoUsuarioDTO_NullEnderecos() {
        NovoUsuarioPayLoad payload = new NovoUsuarioPayLoad();
        payload.setNome("Teste");
        
        TipoUsuarioPayLoad tipoPayload = mock(TipoUsuarioPayLoad.class);
        when(tipoPayload.getId()).thenReturn(1);
        payload.setTipoUsuario(tipoPayload);
        
        payload.setEnderecos(null);

        UsuarioDTO result = UsuarioMapperApi.toNovoUsuarioDTO(payload, "123");

        assertNotNull(result);
        assertNull(result.enderecos());
    }
}