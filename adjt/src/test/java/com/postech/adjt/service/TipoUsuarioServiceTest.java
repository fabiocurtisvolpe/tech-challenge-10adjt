package com.postech.adjt.service;

import com.postech.adjt.dto.TipoUsuarioDTO;
import com.postech.adjt.exception.DuplicateEntityException;
import com.postech.adjt.exception.NotificacaoException;
import com.postech.adjt.mapper.TipoUsuarioMapper;
import com.postech.adjt.model.TipoUsuario;
import com.postech.adjt.repository.TipoUsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TipoUsuarioServiceTest {

    @Mock
    private TipoUsuarioRepository repository;

    @Mock
    private TipoUsuarioMapper mapper;

    @InjectMocks
    private TipoUsuarioService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void criar_DeveRetornarDTOQuandoSucesso() {
        TipoUsuarioDTO dto = new TipoUsuarioDTO();
        TipoUsuario entity = new TipoUsuario();

        when(mapper.toTipoUsuario(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toTipoUsuarioDTO(entity)).thenReturn(dto);

        TipoUsuarioDTO result = service.criar(dto);

        assertEquals(dto, result);
        verify(repository).save(entity);
    }

    @Test
    void criar_DeveLancarDuplicateEntityExceptionQuandoNomeDuplicado() {
        TipoUsuarioDTO dto = new TipoUsuarioDTO();
        when(mapper.toTipoUsuario(dto)).thenReturn(new TipoUsuario());
        when(repository.save(any())).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DuplicateEntityException.class, () -> service.criar(dto));
    }

    @Test
    void criar_DeveLancarNotificacaoExceptionParaOutrosErros() {
        TipoUsuarioDTO dto = new TipoUsuarioDTO();
        when(mapper.toTipoUsuario(dto)).thenReturn(new TipoUsuario());
        when(repository.save(any())).thenThrow(RuntimeException.class);

        assertThrows(NotificacaoException.class, () -> service.criar(dto));
    }

    @Test
    void atualizar_DeveRetornarDTOQuandoSucesso() {
        Integer id = 1;
        TipoUsuarioDTO dto = new TipoUsuarioDTO();
        dto.setNome("NovoNome");
        dto.setDescricao("desc");
        TipoUsuario entity = new TipoUsuario();
        entity.setNome("AntigoNome");
        entity.setDescricao("desc");
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toTipoUsuarioDTO(entity)).thenReturn(dto);

        TipoUsuarioDTO result = service.atualizar(id, dto);

        assertEquals(dto, result);
        verify(repository).save(entity);
    }

    @Test
    void atualizar_DeveLancarNotificacaoExceptionQuandoNaoPodeEditar() {
        Integer id = 1;
        TipoUsuarioDTO dto = new TipoUsuarioDTO();
        TipoUsuario entity = new TipoUsuario();
        entity.setNome("CLIENTE");
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        assertThrows(NotificacaoException.class, () -> service.atualizar(id, dto));
    }

    @Test
    void atualizar_DeveLancarDuplicateEntityExceptionQuandoNomeDuplicado() {
        Integer id = 1;
        TipoUsuarioDTO dto = new TipoUsuarioDTO();
        TipoUsuario entity = new TipoUsuario();
        entity.setNome("Outro");
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DuplicateEntityException.class, () -> service.atualizar(id, dto));
    }

    @Test
    void atualizar_DeveLancarNotificacaoExceptionQuandoNaoEncontrado() {
        Integer id = 1;
        TipoUsuarioDTO dto = new TipoUsuarioDTO();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotificacaoException.class, () -> service.atualizar(id, dto));
    }

    @Test
    void buscar_DeveRetornarDTOQuandoEncontrado() {
        Integer id = 1;
        TipoUsuario entity = new TipoUsuario();
        TipoUsuarioDTO dto = new TipoUsuarioDTO();
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toTipoUsuarioDTO(entity)).thenReturn(dto);

        TipoUsuarioDTO result = service.buscar(id);

        assertEquals(dto, result);
    }

    @Test
    void buscar_DeveLancarNotificacaoExceptionQuandoNaoEncontrado() {
        Integer id = 1;
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotificacaoException.class, () -> service.buscar(id));
    }

    @Test
    void listar_DeveRetornarListaDTOOrdenada() {
        TipoUsuario entity = new TipoUsuario();
        TipoUsuarioDTO dto = new TipoUsuarioDTO();
        List<TipoUsuario> entities = Collections.singletonList(entity);
        when(repository.findAll(any(Sort.class))).thenReturn(entities);
        when(mapper.toTipoUsuarioDTO(entity)).thenReturn(dto);

        List<TipoUsuarioDTO> result = service.listar();

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void listar_DeveRetornarListaVaziaQuandoNenhumEncontrado() {
        when(repository.findAll(any(Sort.class))).thenReturn(Collections.emptyList());

        List<TipoUsuarioDTO> result = service.listar();

        assertTrue(result.isEmpty());
    }

    @Test
    void ativarInativar_DeveAlternarAtivoQuandoSucesso() {
        Integer id = 1;
        TipoUsuario entity = new TipoUsuario();
        entity.setNome("Outro");
        entity.setAtivo(true);
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);

        boolean result = service.ativarInativar(id);

        assertTrue(result);
        assertFalse(entity.getAtivo());
    }

    @Test
    void ativarInativar_DeveLancarNotificacaoExceptionQuandoNaoPodeEditar() {
        Integer id = 1;
        TipoUsuario entity = new TipoUsuario();
        entity.setNome("CLIENTE");
        entity.setAtivo(true);
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        assertThrows(NotificacaoException.class, () -> service.ativarInativar(id));
    }

    @Test
    void ativarInativar_DeveLancarNotificacaoExceptionQuandoNaoEncontrado() {
        Integer id = 1;
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotificacaoException.class, () -> service.ativarInativar(id));
    }
}