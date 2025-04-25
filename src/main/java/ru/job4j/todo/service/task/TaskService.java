package ru.job4j.todo.service.task;

import ru.job4j.todo.model.Task;

import java.util.Collection;
import java.util.Optional;

public interface TaskService {

    boolean create(Task task);

    boolean update(Task task);

    Optional<Task> findById(int id);

    Collection<Task> findAll();

    boolean deleteById(int id);

    boolean doneById(int id);

}
