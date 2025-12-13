package com.postech.adjt.domain.validators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.postech.adjt.domain.entidade.DadosDia;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HorarioRestauranteValidator {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final List<String> DIAS_PERMITIDOS = Arrays.asList("seg", "ter", "qua", "qui", "sex", "sab", "dom");

    public static void validar(String jsonHorario) {
        if (jsonHorario == null || jsonHorario.trim().isEmpty()) {
            throw new IllegalArgumentException("O horário de funcionamento não pode estar vazio.");
        }

        Map<String, DadosDia> mapaHorarios;

        // 1. Validação de Formato JSON (Sintaxe)
        try {
            mapaHorarios = objectMapper.readValue(jsonHorario, new TypeReference<Map<String, DadosDia>>() {
            });
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("O formato do JSON de horário é inválido. Verifique a sintaxe.");
        }

        // 2. Validação Lógica dos Dias
        Set<String> diasInformados = mapaHorarios.keySet();

        for (String dia : diasInformados) {
            // Verifica se a chave é válida (seg, ter...)
            if (!DIAS_PERMITIDOS.contains(dia)) {
                throw new IllegalArgumentException("Dia da semana inválido encontrado: " + dia);
            }

            DadosDia dados = mapaHorarios.get(dia);

            // Se o restaurante não abre nesse dia, ignora a validação de horas
            if (!dados.aberto) {
                continue;
            }

            // Valida se os campos existem
            if (dados.inicio == null || dados.fim == null) {
                throw new IllegalArgumentException(
                        "Horário de início e fim são obrigatórios para dias abertos: " + dia);
            }

            // 3. Validação do Formato de Hora e Lógica (Fim > Inicio)
            try {
                LocalTime inicio = LocalTime.parse(dados.inicio);
                LocalTime fim = LocalTime.parse(dados.fim);

                if (fim.isBefore(inicio)) {
                    throw new IllegalArgumentException(
                            String.format("Erro em '%s': A hora final (%s) não pode ser menor que a inicial (%s).", dia,
                                    dados.fim, dados.inicio));
                }

            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException(
                        "Formato de hora inválido para o dia " + dia + ". Use o formato HH:mm (ex: 09:00).");
            }
        }
    }
}
