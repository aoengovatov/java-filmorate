package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NotNull(message = "Film не может быть null")
public class Film {
    private long id;

    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;

    @Size(max=200, message = "Описание фильма не может быть больше 200 знаков")
    private String description;

    @NotNull(message = "Дата фильма не может быть null")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительнось фильма должна быть положительной")
    private int duration;
}