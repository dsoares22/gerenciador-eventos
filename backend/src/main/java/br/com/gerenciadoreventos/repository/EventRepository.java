package br.com.gerenciadoreventos.repository;

import br.com.gerenciadoreventos.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    boolean existsByCategoryId(Long categoryId);

    List<Event> findByTitleContainingIgnoreCase(String title);
    List<Event> findByCategoryId(Long categoryId);
    List<Event> findByDateBetween(LocalDate startDate, LocalDate endDate);
}