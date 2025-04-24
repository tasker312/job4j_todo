package ru.job4j.todo.service.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.task.TaskRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public void save(Task task) {
        if (task.getId() == 0) {
            taskRepository.create(task);
        } else {
            taskRepository.update(task);
        }
    }

    @Override
    public Optional<Task> findById(int id) {
        return taskRepository.findById(id);
    }

    @Override
    public Collection<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public void deleteById(int id) {
        taskRepository.deleteById(id);
    }

    @Override
    public void doneById(int id) {
        var task = taskRepository.findById(id);
        task.ifPresent(t -> {
            t.setDone(true);
            taskRepository.update(t);
        });
    }

}
