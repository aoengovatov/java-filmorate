package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.*;
import java.util.Date;

@Data
@RequiredArgsConstructor
@NotNull
public class Film {
    private int id;

    @NotNull
    @NotBlank
    private final String name;

    @Size(max=200)
    private final String description;

    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd")
    private final Date releaseDate;

    @Positive
    private final int duration;
}