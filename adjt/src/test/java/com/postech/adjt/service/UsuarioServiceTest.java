package com.postech.adjt.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.postech.adjt.constants.MensagemUtil;
import com.postech.adjt.dto.TipoUsuarioDTO;
import com.postech.adjt.dto.usuario.UsuarioDTO;
import com.postech.adjt.exception.DuplicateEntityException;
import com.postech.adjt.exception.NotificacaoException;
import com.postech.adjt.mapper.TipoUsuarioMapper;
import com.postech.adjt.mapper.UsuarioMapper;
import com.postech.adjt.model.Usuario;
import com.postech.adjt.repository.UsuarioRepository;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;
    @Mock
    private UsuarioMapper mapper;
    @Mock
    private TipoUsuarioMapper tipoUsuarioMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TipoUsuarioService tipoUsuarioService;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioService = new UsuarioService(repository, mapper, tipoUsuarioMapper, passwordEncoder, tipoUsuarioService);
    }

    @Test
    void criar_DeveCriarUsuarioComSucesso() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setSenha("senha123");
        dto.setEmail("email@teste.com");
        dto.setTipoUsuario(new TipoUsuarioDTO());
        dto.setEnderecos(new ArrayList<>());
        when(passwordEncoder.encode(anyString())).thenReturn("senhaCodificada");
        when(tipoUsuarioService.buscar(any())).thenReturn(new TipoUsuarioDTO());
        Usuario usuario = new Usuario();
        when(mapper.toUsuario(any(UsuarioDTO.class))).thenReturn(usuario);
        when(repository.save(any(Usuario.class))).thenReturn(usuario);
        when(mapper.toUsuarioDTO(any(Usuario.class))).thenReturn(dto);

        UsuarioDTO result = usuarioService.criar(dto);

        assertNotNull(result);
        verify(repository).save(any(Usuario.class));
    }

    @Test
    void criar_DeveLancarExcecaoQuandoSenhaEmBranco() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setSenha("");
        Exception ex = assertThrows(NotificacaoException.class, () -> usuarioService.criar(dto));
        assertEquals("A senha não pode estar em branco.", ex.getMessage());
    }

    @Test
    void criar_DeveLancarDuplicateEntityExceptionQuandoEmailDuplicado() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setSenha("senha");
        dto.setEmail("email@teste.com");
        dto.setTipoUsuario(new TipoUsuarioDTO());
        dto.setEnderecos(new ArrayList<>());
        when(passwordEncoder.encode(anyString())).thenReturn("senhaCodificada");
        when(tipoUsuarioService.buscar(any())).thenReturn(new TipoUsuarioDTO());
        when(mapper.toUsuario(any(UsuarioDTO.class))).thenReturn(new Usuario());
        when(repository.save(any(Usuario.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DuplicateEntityException.class, () -> usuarioService.criar(dto));
    }

    @Test
    void buscar_DeveRetornarUsuarioDTOQuandoEncontrado() {
        Integer id = 1;
        Usuario usuario = new Usuario();
        UsuarioDTO dto = new UsuarioDTO();
        when(repository.findById(id)).thenReturn(Optional.of(usuario));
        when(mapper.toUsuarioDTO(usuario)).thenReturn(dto);

        UsuarioDTO result = usuarioService.buscar(id);

        assertNotNull(result);
    }

    @Test
    void buscar_DeveLancarExcecaoQuandoNaoEncontrado() {
        Integer id = 1;
        when(repository.findById(id)).thenReturn(Optional.empty());

        Exception ex = assertThrows(NotificacaoException.class, () -> usuarioService.buscar(id));
        assertEquals(MensagemUtil.USUARIO_NAO_ENCONTRADO, ex.getMessage());
    }

    @Test
    void listar_DeveRetornarListaDeUsuarios() {
        Usuario usuario = new Usuario();
        UsuarioDTO dto = new UsuarioDTO();
        when(repository.findAll(any(Sort.class))).thenReturn(List.of(usuario));
        when(mapper.toUsuarioDTO(usuario)).thenReturn(dto);

        List<UsuarioDTO> result = usuarioService.listar();

        assertEquals(1, result.size());
    }
}