package ru.job4j.todo.util.timezone;

import ru.job4j.todo.dto.TimeZoneDTO;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

import java.time.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.TimeZone;

public class TimeZonesOperations {

    public static Collection<TimeZoneDTO> getTimeZonesDTO() {
        return Arrays.stream(TimeZone.getAvailableIDs())
                .map(TimeZone::getTimeZone)
                .sorted(Comparator.comparing(TimeZone::getRawOffset))
                .map(z -> new TimeZoneDTO(
                                z.getID(),
                                Instant.now().atZone(z.toZoneId())
                                        .getOffset()
                                        .getId()
                                        .replaceAll("Z", "+00:00")
                        )
                )
                .toList();
    }

    public static LocalDateTime getUTCDateTime() {
        return Instant.now().atOffset(ZoneOffset.UTC).toLocalDateTime();
    }

    public static LocalDateTime getUserZoneDateTime(LocalDateTime utcDateTime, ZoneId zoneId) {
        return ZonedDateTime.of(utcDateTime, ZoneOffset.UTC).withZoneSameInstant(zoneId).toLocalDateTime();
    }

    public static void setTimeZoneToTasks(Collection<Task> tasks, User user) {
        var zone = "".equals(user.getTimezone()) ? "UTC" : user.getTimezone();
        var zoneId = ZoneId.of(zone);
        tasks.forEach(task -> task.setCreated(getUserZoneDateTime(task.getCreated(), zoneId)));
    }

}
