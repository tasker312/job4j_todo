package ru.job4j.todo.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.task.TaskService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    private static TaskController taskController;

    private static TaskService taskService;

    @BeforeAll
    public static void init() {
        taskService = mock(TaskService.class);
        taskController = new TaskController(taskService);
    }

    @Test
    public void whenFindAllThenReturnAllList() {
        var task1 = new Task(1, "test1", now(), false);
        var task2 = new Task(2, "test2", now(), true);
        var expectedTasks = List.of(task1, task2);
        when(taskService.findAll()).thenReturn(expectedTasks);
        var parameterType = "all";

        var model = new ConcurrentModel();
        var view = taskController.getTaskList(parameterType, model);
        var actualTasks = model.getAttribute("tasks");
        var actualType = model.getAttribute("type");

        assertThat(view).isEqualTo("/tasks/list");
        assertThat(actualType).isEqualTo(parameterType);
        assertThat(actualTasks)
                .usingRecursiveComparison()
                .withComparatorForType(
                        Comparator.comparing(o -> o.truncatedTo(ChronoUnit.SECONDS)),
                        LocalDateTime.class
                )
                .isEqualTo(expectedTasks);
    }

    @Test
    public void whenFindDoneThenReturnDoneList() {
        var task1 = new Task(1, "test1", now(), false);
        var expectedTasks = List.of(task1);
        when(taskService.findAll()).thenReturn(expectedTasks);
        var parameterType = "done";

        var model = new ConcurrentModel();
        var view = taskController.getTaskList(parameterType, model);
        var actualTasks = model.getAttribute("tasks");
        var actualType = model.getAttribute("type");

        assertThat(view).isEqualTo("/tasks/list");
        assertThat(actualType).isEqualTo(parameterType);
        assertThat(actualTasks)
                .usingRecursiveComparison()
                .withComparatorForType(
                        Comparator.comparing(o -> o.truncatedTo(ChronoUnit.SECONDS)),
                        LocalDateTime.class
                )
                .isEqualTo(expectedTasks);
    }

    @Test
    public void whenFindNewThenReturnNewList() {
        var task2 = new Task(2, "test2", now(), true);
        var expectedTasks = List.of(task2);
        when(taskService.findAll()).thenReturn(expectedTasks);
        var parameterType = "new";

        var model = new ConcurrentModel();
        var view = taskController.getTaskList(parameterType, model);
        var actualTasks = model.getAttribute("tasks");
        var actualType = model.getAttribute("type");

        assertThat(view).isEqualTo("/tasks/list");
        assertThat(actualType).isEqualTo(parameterType);
        assertThat(actualTasks)
                .usingRecursiveComparison()
                .withComparatorForType(
                        Comparator.comparing(o -> o.truncatedTo(ChronoUnit.SECONDS)),
                        LocalDateTime.class
                )
                .isEqualTo(expectedTasks);
    }

    @Test
    public void whenGetTaskByIdThenReturnTask() {
        var task = new Task(1, "test1", now(), false);
        when(taskService.findById(1)).thenReturn(Optional.of(task));

        var model = new ConcurrentModel();
        var view = taskController.getTask(1, "read", model);
        var actualTask = model.getAttribute("task");
        var actualType = model.getAttribute("type");

        assertThat(view).isEqualTo("/tasks/_id");
        assertThat(actualTask)
                .usingRecursiveComparison()
                .withComparatorForType(
                        Comparator.comparing(o -> o.truncatedTo(ChronoUnit.SECONDS)),
                        LocalDateTime.class
                )
                .isEqualTo(task);
        assertThat(actualType).isEqualTo("read");
    }

    @Test
    public void whenGetTaskByNonExistentIdThenReturnNewTask() {
        when(taskService.findById(1)).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = taskController.getTask(1, null, model);
        var actualTask = model.getAttribute("task");
        var actualType = model.getAttribute("type");

        assertThat(view).isEqualTo("/tasks/_id");
        assertThat(actualTask)
                .usingRecursiveComparison()
                .withComparatorForType(
                        Comparator.comparing(o -> o.truncatedTo(ChronoUnit.SECONDS)),
                        LocalDateTime.class
                )
                .isEqualTo(new Task());
        assertThat(actualType).isEqualTo("edit");
    }

    @Test
    public void whenSaveTaskThenRedirectToTasks() {
        var task = new Task(1, "test1", now(), false);
        var view = taskController.saveTask(task);

        assertThat(view).isEqualTo("redirect:/tasks");
        verify(taskService).save(task);
    }

    @Test
    public void whenDeleteTaskThenRedirectToTasks() {
        var view = taskController.deleteTask(1);

        assertThat(view).isEqualTo("redirect:/tasks");
        verify(taskService).deleteById(1);
    }

    @Test
    public void whenDoneTaskThenRedirectToTasks() {
        var view = taskController.doneTask(1);

        assertThat(view).isEqualTo("redirect:/tasks");
        verify(taskService).doneById(1);
    }

}
