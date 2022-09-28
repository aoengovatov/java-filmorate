package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NotNull(message = "User не может быть null")
public class User {

    private int id;

    @Email(message = "Не соответствует формату e-mail")
    @NotBlank(message = "E-mail не может быть пустым")
    private String email;

    @NotBlank(message = "Логин не может быть пустым")
    private String login;

    private String name;

    @NotNull(message = "Дата рождения не может быть null")
    @PastOrPresent(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthday;
}