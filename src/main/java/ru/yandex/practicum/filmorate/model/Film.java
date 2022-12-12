package ru.yandex.practicum.filmorate.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
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

    private int rate;

    @NotNull(message = "Рейтинг mpa не может быть null")
    @Schema(example = "{\"id\": 1}")
    private Mpa mpa;

    @Schema(example = "[{\"id\": 1}]")
    private Set<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
}