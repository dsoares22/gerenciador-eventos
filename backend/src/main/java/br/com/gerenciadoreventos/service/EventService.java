package br.com.gerenciadoreventos.service;

import br.com.gerenciadoreventos.dto.EventRequest;
import br.com.gerenciadoreventos.dto.EventResponse;
import br.com.gerenciadoreventos.entity.Event;
import br.com.gerenciadoreventos.exception.BusinessException;
import br.com.gerenciadoreventos.exception.ResourceNotFoundException;
import br.com.gerenciadoreventos.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final CategoryService categoryService;

    public EventService(EventRepository eventRepository, CategoryService categoryService){
        this.eventRepository = eventRepository;
        this.categoryService = categoryService;
    }

    // filtra por título, categoria ou período (um de cada vez)
    public List<EventResponse> findAll(String title, Long categoryId, LocalDate startDate, LocalDate endDate){
        if (title != null && !title.isBlank()){
            return toResponseList(eventRepository.findByTitleContainingIgnoreCase(title.trim()));
        }

        if (categoryId != null){
            return toResponseList(eventRepository.findByCategoryId(categoryId));
        }

        if (startDate != null || endDate != null){
            if (startDate == null || endDate == null){
                throw new BusinessException("Informe a data inicial e a data final para filtrar por período");
            }
            if (startDate.isAfter(endDate)){
                throw new BusinessException("A data inicial não pode ser posterior à data final");
            }
            return toResponseList(eventRepository.findByDateBetween(startDate, endDate));
        }

        return toResponseList(eventRepository.findAll());
    }

    public EventResponse findById(Long id){
        return toResponse(getEvent(id));
    }

    public EventResponse create(EventRequest request){
        validateDateTime(request);

        Event event = new Event();
        fillEvent(event, request);

        return toResponse(eventRepository.save(event));
    }

    public EventResponse update(Long id, EventRequest request){
        Event event = getEvent(id);
        validateDateTime(request);

        fillEvent(event, request);

        return toResponse(eventRepository.save(event));
    }

    public void delete(Long id){
        Event event = getEvent(id);
        eventRepository.delete(event);
    }

    private Event getEvent(Long id){
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado"));
    }

    // não deixa cadastrar evento no passado
    private void validateDateTime(EventRequest request){
        LocalDateTime eventDateTime = LocalDateTime.of(request.date(), request.time());

        if (eventDateTime.isBefore(LocalDateTime.now())){
            throw new BusinessException("A data e o horário do evento não podem estar no passado");
        }
    }

    private void fillEvent(Event event, EventRequest request){
        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setDate(request.date());
        event.setTime(request.time());
        event.setLocation(request.location());
        event.setCategory(categoryService.getCategory(request.categoryId()));
    }

    private EventResponse toResponse(Event event){
        return new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getTime(),
                event.getLocation(),
                categoryService.toResponse(event.getCategory()));
    }

    private List<EventResponse> toResponseList(List<Event> events){
        List<EventResponse> list = new ArrayList<>();
        for (Event event : events){
            list.add(toResponse(event));
        }
        return list;
    }
}
