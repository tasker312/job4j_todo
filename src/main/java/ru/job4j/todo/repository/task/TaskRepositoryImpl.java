package ru.job4j.todo.repository.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.CrudRepository;
import ru.job4j.todo.util.timezone.TimeZonesOperations;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private final CrudRepository crud;

    @Override
    public Optional<Task> create(Task task) {
        task.setCreated(TimeZonesOperations.getUTCDateTime());
        try {
            crud.run(session -> session.persist(task));
            return Optional.of(task);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Task> findById(int id) {
        try {
            return crud.optional(
                    "from Task t join fetch t.priority join fetch t.categories where t.id = :id",
                    Task.class,
                    Map.of("id", id)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Collection<Task> findAll() {
        try {
            return crud.query("from Task t join fetch t.priority order by t.id", Task.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return List.of();
    }

    @Override
    public boolean updateTitleAndDescription(Task task) {
        try {
            crud.run(
                    "update Task set title = :title, description = :description, priority = :priority where id = :id",
                    Map.of(
                            "id", task.getId(),
                            "title", task.getTitle(),
                            "description", task.getDescription(),
                            "priority", task.getPriority()
                    )
            );
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        try {
            crud.run(
                    "delete from Task where id = :id",
                    Map.of("id", id)
            );
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean doneById(int id) {
        try {
            crud.run(
                    "update Task set done = true where id = :id",
                    Map.of("id", id)
            );
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

}
