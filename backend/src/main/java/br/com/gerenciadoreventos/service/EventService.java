package br.com.gerenciadoreventos.service;

import br.com.gerenciadoreventos.entity.Category;
import br.com.gerenciadoreventos.entity.Event;
import br.com.gerenciadoreventos.exception.BusinessException;
import br.com.gerenciadoreventos.exception.ResourceNotFoundException;
import br.com.gerenciadoreventos.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final CategoryService categoryService;

    public EventService(EventRepository eventRepository, CategoryService categoryService) {
        this.eventRepository = eventRepository;
        this.categoryService = categoryService;
    }

    public List<Event> findAll(String title, Long categoryId, LocalDate startDate, LocalDate endDate) {
        if (title != null && !title.isBlank()) {
            return eventRepository.findByTitleContainingIgnoreCase(title.trim());
        }

        if (categoryId != null) {
            return eventRepository.findByCategoryId(categoryId);
        }

        if (startDate != null || endDate != null) {
            if (startDate == null || endDate == null) {
                throw new BusinessException("Informe a data inicial e a data final para filtrar por período");
            }
            if (startDate.isAfter(endDate)) {
                throw new BusinessException("A data inicial não pode ser posterior à data final");
            }
            return eventRepository.findByDateBetween(startDate, endDate);
        }

        return eventRepository.findAll();
    }

    public Event findById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado"));
    }

    public Event create(Event event) {
        validateDateTime(event);

        event.setId(null);
        event.setCategory(findCategory(event));
        return eventRepository.save(event);
    }

    public Event update(Long id, Event data) {
        Event event = findById(id);
        validateDateTime(data);

        event.setTitle(data.getTitle());
        event.setDescription(data.getDescription());
        event.setDate(data.getDate());
        event.setTime(data.getTime());
        event.setLocation(data.getLocation());
        event.setCategory(findCategory(data));
        return eventRepository.save(event);
    }

    public void delete(Long id) {
        Event event = findById(id);
        eventRepository.delete(event);
    }

    private void validateDateTime(Event event) {
        LocalDateTime eventDateTime = LocalDateTime.of(event.getDate(), event.getTime());

        if (eventDateTime.isBefore(LocalDateTime.now())) {
            throw new BusinessException("A data e o horário do evento não podem estar no passado");
        }
    }

    private Category findCategory(Event event) {
        if (event.getCategory().getId() == null) {
            throw new BusinessException("A categoria é obrigatória");
        }
        return categoryService.findById(event.getCategory().getId());
    }
}