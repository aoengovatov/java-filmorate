package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@RequiredArgsConstructor
@NotNull(message = "Film не может быть null")
public class Film {
    private int id;

    @NonNull
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;

    @NonNull
    @Size(max=200, message = "Описание фильма не может быть больше 200 знаков")
    private String description;

    @NonNull
    @NotNull(message = "Дата фильма не может быть null")
    private LocalDate releaseDate;

    @NonNull
    @Positive(message = "Продолжительнось фильма должна быть положительной")
    private int duration;
}