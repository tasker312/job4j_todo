package ru.job4j.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class TimeZoneDTO {

    @EqualsAndHashCode.Include
    private String id;

    private String offset;

}
