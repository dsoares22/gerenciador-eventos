package br.com.gerenciadoreventos.service;

import br.com.gerenciadoreventos.dto.CategoryRequest;
import br.com.gerenciadoreventos.dto.CategoryResponse;
import br.com.gerenciadoreventos.entity.Category;
import br.com.gerenciadoreventos.exception.ConflictException;
import br.com.gerenciadoreventos.exception.ResourceNotFoundException;
import br.com.gerenciadoreventos.repository.CategoryRepository;
import br.com.gerenciadoreventos.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    public CategoryService(CategoryRepository categoryRepository, EventRepository eventRepository){
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }

    public List<CategoryResponse> findAll(){
        List<CategoryResponse> list = new ArrayList<>();
        for (Category category : categoryRepository.findAll()){
            list.add(toResponse(category));
        }
        return list;
    }

    public CategoryResponse findById(Long id){
        return toResponse(getCategory(id));
    }

    public CategoryResponse create(CategoryRequest request){
        String name = request.name().trim();

        // não deixa cadastrar categoria com nome repetido
        if (categoryRepository.existsByNameIgnoreCase(name)){
            throw new ConflictException("Já existe uma categoria com esse nome");
        }

        Category category = new Category();
        category.setName(name);
        category.setDescription(request.description());

        return toResponse(categoryRepository.save(category));
    }

    public CategoryResponse update(Long id, CategoryRequest request){
        Category category = getCategory(id);
        String name = request.name().trim();

        // verifica se outra categoria já usa esse nome
        if (categoryRepository.existsByNameIgnoreCaseAndIdNot(name, id)){
            throw new ConflictException("Já existe uma categoria com esse nome");
        }

        category.setName(name);
        category.setDescription(request.description());

        return toResponse(categoryRepository.save(category));
    }

    public void delete(Long id){
        Category category = getCategory(id);

        // não deixa excluir categoria que tem eventos
        if (eventRepository.existsByCategoryId(id)){
            throw new ConflictException("Não é possível excluir uma categoria que possui eventos associados");
        }

        categoryRepository.delete(category);
    }

    public Category getCategory(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
    }

    public CategoryResponse toResponse(Category category){
        return new CategoryResponse(category.getId(), category.getName(), category.getDescription());
    }
}
