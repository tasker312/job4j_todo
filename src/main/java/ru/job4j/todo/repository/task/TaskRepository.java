package ru.job4j.todo.repository.task;

import ru.job4j.todo.model.Task;

import java.util.Collection;
import java.util.Optional;

public interface TaskRepository {

    void create(Task task);

    Optional<Task> findById(int id);

    Collection<Task> findAll();

    void update(Task task);

    void deleteById(int id);

}
