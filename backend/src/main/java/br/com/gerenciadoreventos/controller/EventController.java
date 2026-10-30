package br.com.gerenciadoreventos.controller;

import br.com.gerenciadoreventos.dto.EventRequest;
import br.com.gerenciadoreventos.dto.EventResponse;
import br.com.gerenciadoreventos.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Eventos", description = "Cadastro de eventos e filtros")
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService){
        this.eventService = eventService;
    }

    @Operation(summary = "Lista os eventos", description = "Use um filtro por vez: título, categoria ou período (data inicial e final)")
    @ApiResponse(responseCode = "200", description = "Lista retornada")
    @ApiResponse(responseCode = "400", description = "Período inválido ou parâmetro com valor errado")
    @GetMapping
    public ResponseEntity<List<EventResponse>> findAll(
            @Parameter(description = "Parte do título") @RequestParam(required = false) String title,
            @Parameter(description = "Id da categoria") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "Data inicial (aaaa-mm-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "Data final (aaaa-mm-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
        return ResponseEntity.ok(eventService.findAll(title, categoryId, startDate, endDate));
    }

    @Operation(summary = "Busca um evento pelo id")
    @ApiResponse(responseCode = "200", description = "Evento encontrado")
    @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(eventService.findById(id));
    }

    @Operation(summary = "Cadastra um evento")
    @ApiResponse(responseCode = "201", description = "Evento criado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou data no passado")
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    @PostMapping
    public ResponseEntity<EventResponse> create(@Valid @RequestBody EventRequest request){
        EventResponse event = eventService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }

    @Operation(summary = "Atualiza um evento")
    @ApiResponse(responseCode = "200", description = "Evento atualizado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou data no passado")
    @ApiResponse(responseCode = "404", description = "Evento ou categoria não encontrados")
    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> update(@PathVariable Long id, @Valid @RequestBody EventRequest request){
        return ResponseEntity.ok(eventService.update(id, request));
    }

    @Operation(summary = "Exclui um evento")
    @ApiResponse(responseCode = "204", description = "Evento excluído")
    @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}