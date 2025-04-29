package ru.job4j.todo.service.user;

import ru.job4j.todo.dto.TimeZoneDTO;
import ru.job4j.todo.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService {

    Optional<User> save(User user);

    Optional<User> findByLoginAndPassword(String email, String password);

    Collection<TimeZoneDTO> getTimeZones();

}
