package ru.job4j.todo.service.task;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.task.TaskRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    private static TaskService taskService;

    private static TaskRepository taskRepository;

    @BeforeAll
    public static void init() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskServiceImpl(taskRepository);
    }

    @Test
    public void whenCreateNewTaskThenCreate() {
        var task = new Task(0, "title1", "test", now(), false);

        taskService.create(task);

        verify(taskRepository).create(task);
    }

    @Test
    public void whenCreateExistingTaskThenUpdate() {
        var task = new Task(1, "title1", "test", now(), false);
        taskService.create(task);

        verify(taskRepository).create(task);
    }

    @Test
    public void whenFindByIdThenReturnTask() {
        var task = new Task(1, "title1", "test", now(), false);
        when(taskRepository.findById(1)).thenReturn(Optional.of(task));

        var result = taskService.findById(1);

        assertThat(result).isPresent().get()
                .usingRecursiveComparison()
                .withComparatorForType(
                        Comparator.comparing(o -> o.truncatedTo(ChronoUnit.SECONDS)),
                        LocalDateTime.class
                )
                .isEqualTo(task);
    }

    @Test
    public void whenFindNonExistentThenReturnEmpty() {
        when(taskRepository.findById(1)).thenReturn(Optional.empty());

        var result = taskService.findById(1);

        assertThat(result).isEmpty();
    }

    @Test
    public void whenFindAllThenReturnAllTasks() {
        var task1 = new Task(1, "title1", "test1", now(), false);
        var task2 = new Task(2, "title2", "test2", now(), true);
        when(taskRepository.findAll()).thenReturn(List.of(task1, task2));

        var result = taskService.findAll();

        assertThat(result)
                .usingRecursiveComparison()
                .withComparatorForType(
                        Comparator.comparing(o -> o.truncatedTo(ChronoUnit.SECONDS)),
                        LocalDateTime.class
                )
                .isEqualTo(List.of(task1, task2));
    }

    @Test
    public void whenDeleteByIdThenDelete() {
        taskService.deleteById(1);

        verify(taskRepository).deleteById(1);
    }

    @Test
    public void whenDoneByIdThenUpdate() {
        var task = new Task(1, "title1", "test", now(), false);
        taskService.doneById(task.getId());

        verify(taskRepository).doneById(task.getId());
    }

}
