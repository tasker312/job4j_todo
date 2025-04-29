package ru.job4j.todo.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.dto.TimeZoneDTO;
import ru.job4j.todo.model.User;
import ru.job4j.todo.repository.user.UserRepository;
import ru.job4j.todo.util.timezone.TimeZonesOperations;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return userRepository.findByLoginAndPassword(login, password);
    }

    @Override
    public Collection<TimeZoneDTO> getTimeZones() {
        return TimeZonesOperations.getTimeZonesDTO();
    }

}
