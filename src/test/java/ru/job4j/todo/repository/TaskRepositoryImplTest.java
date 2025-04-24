package ru.job4j.todo.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.task.TaskRepositoryImpl;
import ru.job4j.todo.repository.task.TaskRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

class TaskRepositoryImplTest {

    private static TaskRepository taskRepository;

    private static final StandardServiceRegistry REGISTRY = new StandardServiceRegistryBuilder().configure().build();

    private static final SessionFactory SF = new MetadataSources(REGISTRY).buildMetadata().buildSessionFactory();

    @BeforeAll
    public static void initRepositories() {
        taskRepository = new TaskRepositoryImpl(SF);
    }

    @AfterEach
    public void destroyRepositories() {
        var tasks = taskRepository.findAll();
        for (var task : tasks) {
            taskRepository.deleteById(task.getId());
        }
    }

    @Test
    public void whenCreateThenTaskIsSaved() {
        var task = new Task(0, "test", now(), false);
        taskRepository.create(task);

        var taskList = taskRepository.findAll();

        assertThat(taskList)
                .usingRecursiveComparison()
                .withComparatorForType(
                        Comparator.comparing(o -> o.truncatedTo(ChronoUnit.SECONDS)),
                        LocalDateTime.class
                )
                .isEqualTo(List.of(task));
    }

    @Test
    public void whenFindByIdThenReturnTask() {
        var task = new Task(0, "test", now(), false);
        taskRepository.create(task);

        var actualTask = taskRepository.findById(task.getId());

        assertThat(actualTask).isPresent().get()
                .usingRecursiveComparison()
                .withComparatorForType(
                        Comparator.comparing(o -> o.truncatedTo(ChronoUnit.SECONDS)),
                        LocalDateTime.class
                )
                .isEqualTo(task);
    }

    @Test
    public void whenFindByNonExistentIdThenReturnOptionalEmpty() {

        var actualTask = taskRepository.findById(-1);

        assertThat(actualTask)
                .isEmpty();
    }

    @Test
    public void whenFindAllThenReturnAllTasks() {
        var task1 = new Task(0, "test1", now(), false);
        taskRepository.create(task1);
        var task2 = new Task(0, "test", now(), false);
        taskRepository.create(task2);
        var expectedTasks = List.of(task1, task2);

        var actualTasks = taskRepository.findAll();

        assertThat(actualTasks)
                .hasSize(expectedTasks.size())
                .usingRecursiveComparison()
                .withComparatorForType(
                        Comparator.comparing(o -> o.truncatedTo(ChronoUnit.SECONDS)),
                        LocalDateTime.class
                )
                .isEqualTo(expectedTasks);
    }

    @Test
    public void whenUpdateTaskThenReturnUpdatedTask() {
        var task = new Task(0, "test description", now(), false);
        taskRepository.create(task);

        task.setDone(true);
        taskRepository.update(task);
        var actualTask = taskRepository.findById(task.getId());

        assertThat(actualTask).isPresent().get()
                .usingRecursiveComparison()
                .withComparatorForType(
                        Comparator.comparing(o -> o.truncatedTo(ChronoUnit.SECONDS)),
                        LocalDateTime.class
                )
                .isEqualTo(task);
    }

    @Test
    public void whenDeleteByIdThenTaskIsDeleted() {
        var task = new Task();
        task.setDescription("test description");
        task.setCreated(now());
        task.setDone(true);
        taskRepository.create(task);

        taskRepository.deleteById(task.getId());
        var taskList = taskRepository.findAll();

        assertThat(taskList).isEmpty();
    }

}
