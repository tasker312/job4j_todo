package ru.job4j.todo.service.task;

import ru.job4j.todo.model.Task;

import java.util.Collection;
import java.util.Optional;

public interface TaskService {

    void save(Task task);

    Optional<Task> findById(int id);

    Collection<Task> findAll();

    void deleteById(int id);

    void doneById(int id);

}
