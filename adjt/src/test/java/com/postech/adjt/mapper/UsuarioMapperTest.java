package com.postech.adjt.mapper;

import com.postech.adjt.dto.EnderecoDTO;
import com.postech.adjt.dto.TipoUsuarioDTO;
import com.postech.adjt.dto.usuario.UsuarioDTO;
import com.postech.adjt.model.Endereco;
import com.postech.adjt.model.TipoUsuario;
import com.postech.adjt.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioMapperTest {

    private UsuarioMapper usuarioMapper;
    private TipoUsuarioMapper tipoUsuarioMapper;

    @BeforeEach
    void setUp() {
        tipoUsuarioMapper = mock(TipoUsuarioMapper.class);
        usuarioMapper = new UsuarioMapper(tipoUsuarioMapper);
    }

    @Test
    void testToUsuarioDTONull() {
        assertNull(usuarioMapper.toUsuarioDTO(null));
    }

    @Test
    void testToUsuarioDTO() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setDataCriacao(LocalDateTime.now().minusDays(1));
        usuario.setDataAlteracao(LocalDateTime.now());
        usuario.setAtivo(true);
        usuario.setNome("John Doe");
        usuario.setEmail("john@example.com");

        TipoUsuario tipoUsuario = new TipoUsuario();
        usuario.setTipoUsuario(tipoUsuario);

        Endereco endereco = new Endereco();
        endereco.setId(10);
        usuario.setEnderecos(List.of(endereco));

        TipoUsuarioDTO tipoUsuarioDTO = new TipoUsuarioDTO();
        when(tipoUsuarioMapper.toTipoUsuarioDTO(tipoUsuario)).thenReturn(tipoUsuarioDTO);

        UsuarioDTO dto = usuarioMapper.toUsuarioDTO(usuario);

        assertNotNull(dto);
        assertEquals(usuario.getId(), dto.getId());
        assertEquals(usuario.getNome(), dto.getNome());
        assertEquals(usuario.getEmail(), dto.getEmail());
        assertEquals(usuario.getAtivo(), dto.getAtivo());
        assertEquals(tipoUsuarioDTO, dto.getTipoUsuario());
        assertNotNull(dto.getEnderecos());
        assertEquals(1, dto.getEnderecos().size());
        assertEquals(endereco.getId(), dto.getEnderecos().get(0).getId());
    }

    @Test
    void testToUsuarioDTOWithEmptyEnderecos() {
        Usuario usuario = new Usuario();
        usuario.setId(2);
        usuario.setEnderecos(Collections.emptyList());
        usuario.setTipoUsuario(new TipoUsuario());

        when(tipoUsuarioMapper.toTipoUsuarioDTO(any())).thenReturn(new TipoUsuarioDTO());

        UsuarioDTO dto = usuarioMapper.toUsuarioDTO(usuario);

        assertNotNull(dto);
        assertTrue(dto.getEnderecos().isEmpty());
    }

    @Test
    void testToUsuarioNull() {
        assertNull(usuarioMapper.toUsuario(null));
    }

    @Test
    void testToUsuario() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(3);
        dto.setDataCriacao(LocalDateTime.now().minusDays(2));
        dto.setDataAlteracao(LocalDateTime.now());
        dto.setAtivo(false);
        dto.setNome("Jane Doe");
        dto.setEmail("jane@example.com");
        dto.setSenha("secret");

        TipoUsuarioDTO tipoUsuarioDTO = new TipoUsuarioDTO();
        dto.setTipoUsuario(tipoUsuarioDTO);

        TipoUsuario tipoUsuario = new TipoUsuario();
        when(tipoUsuarioMapper.toTipoUsuario(tipoUsuarioDTO)).thenReturn(tipoUsuario);

        Usuario usuario = usuarioMapper.toUsuario(dto);

        assertNotNull(usuario);
        assertEquals(dto.getId(), usuario.getId());
        assertEquals(dto.getNome(), usuario.getNome());
        assertEquals(dto.getEmail(), usuario.getEmail());
        assertEquals(dto.getAtivo(), usuario.getAtivo());
        assertEquals(dto.getSenha(), usuario.getSenha());
        assertEquals(tipoUsuario, usuario.getTipoUsuario());
    }

    @Test
    void testToEnderecoDTO() {
        Endereco endereco = new Endereco();
        endereco.setId(5);
        endereco.setDataCriacao(LocalDateTime.now().minusDays(3));
        endereco.setDataAlteracao(LocalDateTime.now());
        endereco.setAtivo(true);
        endereco.setLogradouro("Rua X");
        endereco.setNumero("123");
        endereco.setComplemento("Apto 1");
        endereco.setBairro("Centro");
        endereco.setPontoReferencia("Próximo ao parque");
        endereco.setCep("12345-678");
        endereco.setMunicipio("Cidade");
        endereco.setUf("UF");
        endereco.setPrincipal(true);

        EnderecoDTO dto = usuarioMapper.toEnderecoDTO(endereco);

        assertNotNull(dto);
        assertEquals(endereco.getId(), dto.getId());
        assertEquals(endereco.getLogradouro(), dto.getLogradouro());
        assertEquals(endereco.getNumero(), dto.getNumero());
        assertEquals(endereco.getComplemento(), dto.getComplemento());
        assertEquals(endereco.getBairro(), dto.getBairro());
        assertEquals(endereco.getPontoReferencia(), dto.getPontoReferencia());
        assertEquals(endereco.getCep(), dto.getCep());
        assertEquals(endereco.getMunicipio(), dto.getMunicipio());
        assertEquals(endereco.getUf(), dto.getUf());
        assertEquals(endereco.getPrincipal(), dto.getPrincipal());
    }

    @Test
    void testToEndereco() {
        EnderecoDTO dto = new EnderecoDTO();
        dto.setId(6);
        dto.setDataCriacao(LocalDateTime.now().minusDays(4));
        dto.setDataAlteracao(LocalDateTime.now());
        dto.setAtivo(false);
        dto.setLogradouro("Rua Y");
        dto.setNumero("456");
        dto.setComplemento("Casa");
        dto.setBairro("Bairro");
        dto.setPontoReferencia("Ao lado do mercado");
        dto.setCep("87654-321");
        dto.setMunicipio("Outra Cidade");
        dto.setUf("OU");
        dto.setPrincipal(false);

        Endereco endereco = usuarioMapper.toEndereco(dto);

        assertNotNull(endereco);
        assertEquals(dto.getId(), endereco.getId());
        assertEquals(dto.getLogradouro(), endereco.getLogradouro());
        assertEquals(dto.getNumero(), endereco.getNumero());
        assertEquals(dto.getComplemento(), endereco.getComplemento());
        assertEquals(dto.getBairro(), endereco.getBairro());
        assertEquals(dto.getPontoReferencia(), endereco.getPontoReferencia());
        assertEquals(dto.getCep(), endereco.getCep());
        assertEquals(dto.getMunicipio(), endereco.getMunicipio());
        assertEquals(dto.getUf(), endereco.getUf());
        assertEquals(dto.getPrincipal(), endereco.getPrincipal());
    }
}