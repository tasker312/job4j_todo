package ru.job4j.todo.repository.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private final SessionFactory sf;

    @Override
    public void create(Task task) {
        var session = sf.openSession();
        try {
            session.beginTransaction();
            task.setCreated(LocalDateTime.now());
            session.persist(task);
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public Optional<Task> findById(int id) {
        var session = sf.openSession();
        session.beginTransaction();
        var task = session.get(Task.class, id);
        session.getTransaction().commit();
        session.close();
        return Optional.ofNullable(task);
    }

    @Override
    public Collection<Task> findAll() {
        var session = sf.openSession();
        session.beginTransaction();
        var tasks = session.createSelectionQuery("FROM Task ORDER BY id", Task.class).getResultList();
        session.getTransaction().commit();
        session.close();
        return tasks;
    }

    @Override
    public void update(Task task) {
        var session = sf.openSession();
        try {
            session.beginTransaction();
            session.merge(task);
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteById(int id) {
        var session = sf.openSession();
        session.beginTransaction();
        var task = findById(id);
        task.ifPresent(session::remove);
        session.getTransaction().commit();
        session.close();
    }

}
