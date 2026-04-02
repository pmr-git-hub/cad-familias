package br.gov.pmr.cad_familias.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CustomLocalDateDeserializer extends JsonDeserializer<LocalDate> {
    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        String date = jsonParser.getText(); // Obtém a data como String
        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ISO_DATE_TIME,        // Formato ISO 8601 (com tempo e fuso horário)
                DateTimeFormatter.ISO_LOCAL_DATE,      // Formato yyyy-MM-dd
                DateTimeFormatter.ofPattern("dd/MM/yyyy"), // Formato dd/MM/yyyy
        };

        for (DateTimeFormatter formatter : formatters) {
            try {
                if (formatter == DateTimeFormatter.ISO_DATE_TIME) {
                    return OffsetDateTime.parse(date, formatter).toLocalDate();
                } else {
                    return LocalDate.parse(date, formatter);
                }
            } catch (DateTimeParseException e) {
                // Ignora a exceção e tenta o próximo formato
            }
        }

        throw new IOException("Formato de data inválido: " + date);

    }
}