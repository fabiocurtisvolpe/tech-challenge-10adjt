package com.postech.adjt.api.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.postech.adjt.api.payload.restaurante.DiaFuncionamentoPayload;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.postech.adjt.api.dto.RestauranteRespostaDTO;
import com.postech.adjt.api.dto.UsuarioRespostaDTO;
import com.postech.adjt.api.payload.EnderecoPayLoad;
import com.postech.adjt.api.payload.restaurante.AtualizaRestaurantePayLoad;
import com.postech.adjt.api.payload.restaurante.NovoRestaurantePayLoad;
import com.postech.adjt.domain.dto.RestauranteDTO;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Restaurante;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.enums.TipoCozinhaEnum;

@ExtendWith(MockitoExtension.class)
class RestauranteMapperApiTest {

    @Test
    @DisplayName("Deve converter NovoRestaurantePayLoad para RestauranteDTO corretamente")
    void testToNovoRestauranteDTO() {
        NovoRestaurantePayLoad payload = mock(NovoRestaurantePayLoad.class);
        EnderecoPayLoad enderecoPayLoad = new EnderecoPayLoad();
        enderecoPayLoad.setLogradouro("Rua Nova");

        DiaFuncionamentoPayload dia = new DiaFuncionamentoPayload();
        dia.setAberto(true);
        dia.setInicio("06:00");
        dia.setFim("11:00");

        Map<String, DiaFuncionamentoPayload> horario = new HashMap<>();
        horario.put("seg", dia);
        payload.setHorarioFuncionamento(horario);

        when(payload.getNome()).thenReturn("Novo Restaurante");
        when(payload.getDescricao()).thenReturn("Desc");
        when(payload.getTipoCozinha()).thenReturn(TipoCozinhaEnum.BRASILEIRA);
        when(payload.getEndereco()).thenReturn(enderecoPayLoad);
        when(payload.getHorarioFuncionamento()).thenReturn(horario);

        String emailUsuario = "user@test.com";
        String jsonHorario = "{\"seg\": \"09-18\"}";

        try (MockedStatic<ConversorJson> conversorMock = mockStatic(ConversorJson.class)) {
            conversorMock.when(() -> ConversorJson.converterParaJson(any())).thenReturn(jsonHorario);

            RestauranteDTO result = RestauranteMapperApi.toNovoRestauranteDTO(payload, emailUsuario);

            assertNotNull(result);
            assertNull(result.id());
            assertEquals("Novo Restaurante", result.nome());
            assertEquals(jsonHorario, result.horarioFuncionamento());
            assertEquals(TipoCozinhaEnum.BRASILEIRA, result.tipoCozinha());

            assertNotNull(result.endereco());
            assertEquals("Rua Nova", result.endereco().logradouro());

            assertNotNull(result.dono());
            assertEquals(emailUsuario, result.dono().email());
            assertTrue(result.ativo());
        }
    }

    @Test
    @DisplayName("Deve converter AtualizaRestaurantePayLoad para RestauranteDTO corretamente")
    void testToAtualizaRestauranteDTO() {
        AtualizaRestaurantePayLoad payload = mock(AtualizaRestaurantePayLoad.class);
        EnderecoPayLoad enderecoPayLoad = new EnderecoPayLoad();
        enderecoPayLoad.setLogradouro("Rua Atualizada");

        DiaFuncionamentoPayload dia = new DiaFuncionamentoPayload();
        dia.setAberto(true);
        dia.setInicio("06:00");
        dia.setFim("11:00");

        Map<String, DiaFuncionamentoPayload> horario = new HashMap<>();
        horario.put("seg", dia);
        payload.setHorarioFuncionamento(horario);

        when(payload.getId()).thenReturn(10);
        when(payload.getNome()).thenReturn("Restaurante Atualizado");
        when(payload.getEndereco()).thenReturn(enderecoPayLoad);
        when(payload.getHorarioFuncionamento()).thenReturn(horario);

        String emailUsuario = "admin@test.com";
        String jsonHorario = "{}";

        try (MockedStatic<ConversorJson> conversorMock = mockStatic(ConversorJson.class)) {
            conversorMock.when(() -> ConversorJson.converterParaJson(any())).thenReturn(jsonHorario);

            RestauranteDTO result = RestauranteMapperApi.toAtualizaRestauranteDTO(payload, emailUsuario);

            assertNotNull(result);
            assertEquals(10, result.id());
            assertEquals("Restaurante Atualizado", result.nome());
            assertEquals("Rua Atualizada", result.endereco().logradouro());
            assertEquals(emailUsuario, result.dono().email());
        }
    }

    @Test
    @DisplayName("Deve converter Restaurante para RestauranteRespostaGeralDTO (sem dono)")
    void testToRestauranteRespostaGeralDTO() {
        Endereco endereco = mock(Endereco.class);
        when(endereco.getLogradouro()).thenReturn("Rua Geral");

        Restaurante restaurante = mock(Restaurante.class);
        when(restaurante.getId()).thenReturn(5);
        when(restaurante.getNome()).thenReturn("Geral");
        when(restaurante.getEndereco()).thenReturn(endereco);
        when(restaurante.getDataAlteracao()).thenReturn(LocalDateTime.now());
        when(restaurante.getAtivo()).thenReturn(true);

        RestauranteRespostaDTO result = RestauranteMapperApi.toRestauranteRespostaGeralDTO(restaurante);

        assertNotNull(result);
        assertEquals(5, result.getId());
        assertEquals("Geral", result.getNome());
        assertNotNull(result.getEndereco());
        assertEquals("Rua Geral", result.getEndereco().getLogradouro());
        assertNull(result.getDono());
        assertTrue(result.getAtivo());
    }

    @Test
    @DisplayName("toRestauranteRespostaGeralDTO deve retornar null se entrada for null")
    void testToRestauranteRespostaGeralDTO_Null() {
        assertNull(RestauranteMapperApi.toRestauranteRespostaGeralDTO(null));
    }

    @Test
    @DisplayName("Deve converter Restaurante para RestauranteRespostaDTO (com dono)")
    void testToRestauranteRespostaDTO() {
        Endereco endereco = mock(Endereco.class);
        when(endereco.getLogradouro()).thenReturn("Rua Completa");

        Usuario dono = mock(Usuario.class);
        UsuarioRespostaDTO donoDTO = mock(UsuarioRespostaDTO.class);

        Restaurante restaurante = mock(Restaurante.class);
        when(restaurante.getId()).thenReturn(1);
        when(restaurante.getEndereco()).thenReturn(endereco);
        when(restaurante.getDono()).thenReturn(dono);

        try (MockedStatic<UsuarioMapperApi> userMapperMock = mockStatic(UsuarioMapperApi.class)) {
            userMapperMock.when(() -> UsuarioMapperApi.toUsuarioRespostaDTO(dono)).thenReturn(donoDTO);

            RestauranteRespostaDTO result = RestauranteMapperApi.toRestauranteRespostaDTO(restaurante);

            assertNotNull(result);
            assertEquals(1, result.getId());
            assertEquals("Rua Completa", result.getEndereco().getLogradouro());
            assertEquals(donoDTO, result.getDono());
        }
    }

    @Test
    @DisplayName("toRestauranteRespostaDTO deve retornar null se entrada for null")
    void testToRestauranteRespostaDTO_Null() {
        assertNull(RestauranteMapperApi.toRestauranteRespostaDTO(null));
    }

    @Test
    @DisplayName("Deve lidar com endereço nulo ao converter para resposta")
    void testToRestauranteRespostaDTO_NullEndereco() {
        Restaurante restaurante = mock(Restaurante.class);
        when(restaurante.getId()).thenReturn(1);
        when(restaurante.getEndereco()).thenReturn(null);
        when(restaurante.getDono()).thenReturn(mock(Usuario.class));

        try (MockedStatic<UsuarioMapperApi> userMapperMock = mockStatic(UsuarioMapperApi.class)) {
            userMapperMock.when(() -> UsuarioMapperApi.toUsuarioRespostaDTO(any())).thenReturn(mock(UsuarioRespostaDTO.class));

            RestauranteRespostaDTO result = RestauranteMapperApi.toRestauranteRespostaDTO(restaurante);

            assertNotNull(result);
            assertNull(result.getEndereco());
        }
    }

    @Test
    @DisplayName("toRestauranteRespostaGeralDTO deve lidar com endereço nulo")
    void testToRestauranteRespostaGeralDTO_NullEndereco() {
        Restaurante restaurante = mock(Restaurante.class);
        when(restaurante.getId()).thenReturn(1);
        when(restaurante.getEndereco()).thenReturn(null);

        RestauranteRespostaDTO result = RestauranteMapperApi.toRestauranteRespostaGeralDTO(restaurante);

        assertNotNull(result);
        assertNull(result.getEndereco());
    }
}