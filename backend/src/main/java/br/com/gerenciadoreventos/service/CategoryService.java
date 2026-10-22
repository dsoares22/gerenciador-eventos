package br.com.gerenciadoreventos.service;

import br.com.gerenciadoreventos.entity.Category;
import br.com.gerenciadoreventos.exception.ConflictException;
import br.com.gerenciadoreventos.exception.ResourceNotFoundException;
import br.com.gerenciadoreventos.repository.CategoryRepository;
import br.com.gerenciadoreventos.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    public CategoryService(CategoryRepository categoryRepository, EventRepository eventRepository) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
    }

    public Category create(Category category) {
        String name = category.getName().trim();

        if (categoryRepository.existsByNameIgnoreCase(name)) {
            throw new ConflictException("Já existe uma categoria com esse nome");
        }

        category.setId(null);
        category.setName(name);
        return categoryRepository.save(category);
    }

    public Category update(Long id, Category data) {
        Category category = findById(id);
        String name = data.getName().trim();

        if (categoryRepository.existsByNameIgnoreCaseAndIdNot(name, id)) {
            throw new ConflictException("Já existe uma categoria com esse nome");
        }

        category.setName(name);
        category.setDescription(data.getDescription());
        return categoryRepository.save(category);
    }

    public void delete(Long id) {
        Category category = findById(id);

        if (eventRepository.existsByCategoryId(id)) {
            throw new ConflictException("Não é possível excluir uma categoria que possui eventos associados");
        }

        categoryRepository.delete(category);
    }
}