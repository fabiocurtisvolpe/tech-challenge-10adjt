package com.postech.adjt.api.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.postech.adjt.api.payload.restaurante.DiaFuncionamentoPayload;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConversorJsonTest {

    @Mock
    private DiaFuncionamentoPayload diaPayload;

    @Test
    @DisplayName("Deve converter mapa para JSON com sucesso")
    void converterParaJson_Sucesso() {
        Map<String, DiaFuncionamentoPayload> mapa = new HashMap<>();
        mapa.put("segunda", diaPayload);

        String json = ConversorJson.converterParaJson(mapa);

        assertNotNull(json);
        assertTrue(json.contains("segunda"));
    }

    @Test
    @DisplayName("Deve lançar RuntimeException quando ocorrer JsonProcessingException")
    void converterParaJson_ErroProcessamento() {
        Map<String, DiaFuncionamentoPayload> mapa = new HashMap<>();
        mapa.put("terca", diaPayload);

        try (MockedConstruction<ObjectMapper> mocked = Mockito.mockConstruction(ObjectMapper.class,
                (mock, context) -> when(mock.writeValueAsString(any())).thenThrow(mock(JsonProcessingException.class)))) {

            RuntimeException exception = assertThrows(RuntimeException.class, () ->
                    ConversorJson.converterParaJson(mapa)
            );

            assertEquals("Erro ao converter mapa de horários para JSON", exception.getMessage());
        }
    }
}