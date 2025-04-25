package ru.job4j.todo.repository.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private final SessionFactory sf;

    @Override
    public boolean create(Task task) {
        var session = sf.openSession();
        try {
            session.beginTransaction();
            task.setCreated(LocalDateTime.now());
            session.persist(task);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return false;
    }

    @Override
    public Optional<Task> findById(int id) {
        var session = sf.openSession();
        try {
            session.beginTransaction();
            var task = session.get(Task.class, id);
            session.getTransaction().commit();
            return Optional.ofNullable(task);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return Optional.empty();
    }

    @Override
    public Collection<Task> findAll() {
        var session = sf.openSession();
        try {
            session.beginTransaction();
            var tasks = session.createSelectionQuery("FROM Task ORDER BY id", Task.class).getResultList();
            session.getTransaction().commit();
            return tasks;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return List.of();
    }

    @Override
    public boolean updateTitleAndDescription(Task task) {
        var session = sf.openSession();
        try {
            session.beginTransaction();
            var affected = session.createMutationQuery("UPDATE Task SET title = :title, description = :description WHERE id = :id")
                    .setParameter("title", task.getTitle())
                    .setParameter("description", task.getDescription())
                    .setParameter("id", task.getId())
                    .executeUpdate();
            session.getTransaction().commit();
            return affected > 0;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        var session = sf.openSession();
        try {
            session.beginTransaction();
            var affected = session.createMutationQuery("DELETE FROM Task WHERE id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            session.getTransaction().commit();
            return affected > 0;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return false;
    }

    @Override
    public boolean doneById(int id) {
        var session = sf.openSession();
        try {
            session.beginTransaction();
            var affected = session.createMutationQuery("UPDATE Task SET done = true WHERE id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            session.getTransaction().commit();
            return affected > 0;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return false;
    }

}
