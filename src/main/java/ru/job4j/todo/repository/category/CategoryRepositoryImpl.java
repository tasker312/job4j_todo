package ru.job4j.todo.repository.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CrudRepository crud;

    @Override
    public Collection<Category> findAll() {
        try {
            return crud.query("from Category order by id", Category.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return List.of();
    }

}
