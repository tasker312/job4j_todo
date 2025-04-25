package ru.job4j.todo.repository.task;

import ru.job4j.todo.model.Task;

import java.util.Collection;
import java.util.Optional;

public interface TaskRepository {

    Optional<Task> create(Task task);

    Optional<Task> findById(int id);

    Collection<Task> findAll();

    boolean updateTitleAndDescription(Task task);

    boolean deleteById(int id);

    boolean doneById(int id);

}
