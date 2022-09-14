package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.*;
import java.util.Date;

@Data
@RequiredArgsConstructor
@NotNull(message = "Film не может быть null")
public class Film {
    private int id;

    @NotNull(message = "Название фильма не может быть null")
    @NotBlank(message = "Название фильма не может быть пустым")
    private final String name;


    @Size(max=200, message = "Описание фильма не может быть больше 200 знаков")
    private final String description;

    @NotNull(message = "Дата фильма не может быть null")
    @JsonFormat(pattern="yyyy-MM-dd")
    private final Date releaseDate;

    @Positive(message = "Продолжительнось фильма должна быть положительной")
    private final int duration;
}