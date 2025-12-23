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

/**
 * Validador especializado para o horário de funcionamento do restaurante.
 * <p>
 * O horário é armazenado como uma String JSON representando um mapa onde a chave é o
 * dia da semana (abbreviado) e o valor contém o status de abertura e intervalos de tempo.
 */
public class HorarioRestauranteValidator {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /** Lista de chaves permitidas para os dias da semana. */
    private static final List<String> DIAS_PERMITIDOS = Arrays.asList("seg", "ter", "qua", "qui", "sex", "sab", "dom");

    /**
     * Valida a estrutura e a lógica de uma string de horários em formato JSON.
     *
     * @param jsonHorario String contendo o JSON de horários (Ex: {"seg": {"aberto": true, "inicio": "08:00", "fim": "18:00"}})
     * @throws IllegalArgumentException Caso o JSON seja inválido, contenha dias inexistentes,
     *                                  horas mal formatadas ou lógica de tempo impossível (fim antes do início).
     */
    public static void validar(String jsonHorario) {
        // Validação de presença
        if (jsonHorario == null || jsonHorario.trim().isEmpty()) {
            throw new IllegalArgumentException("O horário de funcionamento não pode estar vazio.");
        }

        Map<String, DadosDia> mapaHorarios;

        // 1. Validação de Formato JSON (Sintaxe e Esquema)
        try {
            mapaHorarios = objectMapper.readValue(jsonHorario, new TypeReference<Map<String, DadosDia>>() {
            });
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("O formato do JSON de horário é inválido. Verifique a sintaxe.");
        }

        // 2. Validação Lógica dos Dias
        Set<String> diasInformados = mapaHorarios.keySet();

        for (String dia : diasInformados) {
            // Verifica se a chave é válida (deve estar em seg, ter, qua, qui, sex, sab, dom)
            if (!DIAS_PERMITIDOS.contains(dia)) {
                throw new IllegalArgumentException("Dia da semana inválido encontrado: " + dia);
            }

            DadosDia dados = mapaHorarios.get(dia);

            // Se o restaurante não abre nesse dia, ignora a validação de horas
            if (!dados.aberto) {
                continue;
            }

            // Horários são obrigatórios se o restaurante estiver marcado como 'aberto'
            if (dados.inicio == null || dados.fim == null) {
                throw new IllegalArgumentException(
                        "Horário de início e fim são obrigatórios para dias abertos: " + dia);
            }

            // 3. Validação do Formato de Hora (HH:mm) e Lógica de Intervalo
            try {
                LocalTime inicio = LocalTime.parse(dados.inicio);
                LocalTime fim = LocalTime.parse(dados.fim);

                // Regra: Hora de fechamento não pode ser anterior à hora de abertura
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