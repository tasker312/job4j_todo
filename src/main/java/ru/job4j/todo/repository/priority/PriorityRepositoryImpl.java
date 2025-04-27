package ru.job4j.todo.repository.priority;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PriorityRepositoryImpl implements PriorityRepository {

    private final CrudRepository crud;

    @Override
    public Collection<Priority> findAll() {
        try {
            return crud.query("from Priority order by id", Priority.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return List.of();
    }

}
