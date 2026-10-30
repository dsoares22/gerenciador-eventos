package br.com.gerenciadoreventos.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record EventRequest(

        @Schema(example = "Show de Rock")
        @NotBlank(message = "O título é obrigatório")
        @Size(max = 100, message = "O título pode ter no máximo 100 caracteres")
        String title,

        @Schema(example = "Banda local")
        @Size(max = 100, message = "A descrição pode ter no máximo 100 caracteres")
        String description,

        @Schema(type = "string", format = "date", example = "2026-12-10", description = "Formato aaaa-mm-dd")
        @NotNull(message = "A data é obrigatória")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        @Schema(type = "string", example = "20:00", description = "Formato hh:mm")
        @NotNull(message = "O horário é obrigatório")
        @JsonFormat(pattern = "HH:mm")
        LocalTime time,

        @Schema(example = "Praça Central")
        @NotBlank(message = "O local é obrigatório")
        @Size(max = 100, message = "O local pode ter no máximo 100 caracteres")
        String location,

        @Schema(example = "1")
        @NotNull(message = "A categoria é obrigatória")
        Long categoryId) {
}
