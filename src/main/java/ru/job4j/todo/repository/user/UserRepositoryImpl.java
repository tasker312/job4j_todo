package ru.job4j.todo.repository.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;
import ru.job4j.todo.repository.CrudRepository;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final CrudRepository crud;

    @Override
    public Optional<User> save(User user) {
        try {
            crud.run(session -> session.persist(user));
            return Optional.of(user);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        try {
            return crud.optional(
                    "from User where login = :login and password = :password",
                    User.class,
                    Map.of(
                            "login", login,
                            "password", password
                    )
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

}
