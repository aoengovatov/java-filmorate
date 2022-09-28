package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
@NotNull(message = "User не может быть null")
public class User {

    private int id;

    @Email(message = "Не соответствует формату e-mail")
    @NotBlank(message = "E-mail не может быть пустым")
    @NonNull
    private String email;

    @NotBlank(message = "Логин не может быть пустым")
    @NonNull
    private String login;

    @NonNull
    private String name;

    @NotNull(message = "Дата рождения не может быть null")
    @PastOrPresent(message = "Дата рождения должна быть в прошлом")
    @NonNull
    private LocalDate birthday;
}