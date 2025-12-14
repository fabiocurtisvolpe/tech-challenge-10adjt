package com.postech.adjt.api.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postech.adjt.api.payload.restaurante.DiaFuncionamentoPayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;

public class ConversorJson {

    public static String converterParaJson(Map<String, DiaFuncionamentoPayload> mapa) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(mapa);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter mapa de horários para JSON", e);
        }
    }
}