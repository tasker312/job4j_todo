package ru.job4j.todo.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.repository.category.CategoryRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Collection<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Collection<Category> findByIds(Collection<Integer> ids) {
        var categories = categoryRepository.findAll();
        return categories.stream()
                .filter(c -> ids.contains(c.getId()))
                .toList();
    }

}
