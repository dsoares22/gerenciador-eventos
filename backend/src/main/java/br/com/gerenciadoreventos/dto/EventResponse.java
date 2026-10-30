package br.com.gerenciadoreventos.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public record EventResponse(Long id, String title, String description,
                            @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
                            @JsonFormat(pattern = "HH:mm") LocalTime time,
                            String location, CategoryResponse category) {
}
