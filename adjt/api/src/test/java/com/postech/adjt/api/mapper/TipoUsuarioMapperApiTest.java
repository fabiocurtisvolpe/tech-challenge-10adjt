package com.postech.adjt.api.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.postech.adjt.api.dto.TipoUsuarioRespostaDTO;
import com.postech.adjt.api.payload.tipoUsuario.AtualizaTipoUsuarioPayLoad;
import com.postech.adjt.api.payload.tipoUsuario.NovoTipoUsuarioPayLoad;
import com.postech.adjt.domain.dto.TipoUsuarioDTO;
import com.postech.adjt.domain.entidade.TipoUsuario;
import com.postech.adjt.domain.entidade.TipoUsuarioDonoRestaurante;

class TipoUsuarioMapperApiTest {

    @Test
    @DisplayName("Deve converter NovoTipoUsuarioPayLoad para TipoUsuarioDTO corretamente")
    void testToNovoTipoUsuarioDTO() {
        NovoTipoUsuarioPayLoad payload = mock(NovoTipoUsuarioPayLoad.class);
        when(payload.getIdRestaurante()).thenReturn(10);
        when(payload.getNome()).thenReturn("Gerente");
        when(payload.getDescricao()).thenReturn("Gerente da loja");
        when(payload.getIsDono()).thenReturn(false);

        TipoUsuarioDTO result = TipoUsuarioMapperApi.toNovoTipoUsuarioDTO(payload);

        assertNotNull(result);
        assertNull(result.id());
        assertEquals("Gerente", result.nome());
        assertEquals("Gerente da loja", result.descricao());
        assertTrue(result.ativo());
        assertFalse(result.isDono());

        assertNotNull(result.restaurante());
        assertEquals(10, result.restaurante().id());
    }

    @Test
    @DisplayName("Deve converter AtualizaTipoUsuarioPayLoad para TipoUsuarioDTO corretamente")
    void testToAtualizaTipoUsuarioDTO() {
        AtualizaTipoUsuarioPayLoad payload = new AtualizaTipoUsuarioPayLoad();
        payload.setId(1);
        payload.setNome("Dono");
        payload.setDescricao("Dono do estabelecimento");
        payload.setAtivo(false);
        payload.setIsDono(true);
        payload.setIdRestaurante(5);

        TipoUsuarioDTO result = TipoUsuarioMapperApi.toAtualizaTipoUsuarioDTO(payload);

        assertNotNull(result);
        assertEquals(1, result.id());
        assertEquals("Dono", result.nome());
        assertEquals("Dono do estabelecimento", result.descricao());
        assertFalse(result.ativo());
        assertTrue(result.isDono());

        assertNotNull(result.restaurante());
        assertEquals(5, result.restaurante().id());
    }

    @Test
    @DisplayName("Deve retornar null ao converter TipoUsuario nulo para RespostaDTO")
    void testToTipoUsuarioRespostaDTO_NullInput() {
        TipoUsuarioRespostaDTO result = TipoUsuarioMapperApi.toTipoUsuarioRespostaDTO(null);
        assertNull(result);
    }

    @Test
    @DisplayName("Deve converter TipoUsuarioDonoRestaurante para RespostaDTO com isDono=true")
    void testToTipoUsuarioRespostaDTO_IsDono() {
        TipoUsuarioDonoRestaurante tipoUsuario = mock(TipoUsuarioDonoRestaurante.class);
        LocalDateTime dataAlteracao = LocalDateTime.now();

        when(tipoUsuario.getId()).thenReturn(100);
        when(tipoUsuario.getNome()).thenReturn("Proprietário");
        when(tipoUsuario.getDescricao()).thenReturn("Admin");
        when(tipoUsuario.getDataAlteracao()).thenReturn(dataAlteracao);
        when(tipoUsuario.getAtivo()).thenReturn(true);

        TipoUsuarioRespostaDTO result = TipoUsuarioMapperApi.toTipoUsuarioRespostaDTO(tipoUsuario);

        assertNotNull(result);
        assertEquals(100, result.getId());
        assertEquals("Proprietário", result.getNome());
        assertEquals("Admin", result.getDescricao());
        assertEquals(dataAlteracao, result.getDataAlteracao());
        assertTrue(result.getAtivo());
        assertTrue(result.getIsDono());
    }

    @Test
    @DisplayName("Deve converter TipoUsuario genérico para RespostaDTO com isDono=false")
    void testToTipoUsuarioRespostaDTO_NotDono() {
        TipoUsuario tipoUsuario = mock(TipoUsuario.class);
        LocalDateTime dataAlteracao = LocalDateTime.now();

        when(tipoUsuario.getId()).thenReturn(200);
        when(tipoUsuario.getNome()).thenReturn("Funcionario");
        when(tipoUsuario.getDescricao()).thenReturn("Atendente");
        when(tipoUsuario.getDataAlteracao()).thenReturn(dataAlteracao);
        when(tipoUsuario.getAtivo()).thenReturn(true);

        TipoUsuarioRespostaDTO result = TipoUsuarioMapperApi.toTipoUsuarioRespostaDTO(tipoUsuario);

        assertNotNull(result);
        assertEquals(200, result.getId());
        assertEquals("Funcionario", result.getNome());
        assertFalse(result.getIsDono());
    }
}