package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Date;

@Data
@RequiredArgsConstructor
@NotNull(message = "User не может быть null")
public class User {
    private int id;

    @Email(message = "Не соответствует формату e-mail")
    @NotBlank(message = "E-mail не может быть пустым")
    private final String email;

    @NotBlank(message = "Логин не может быть пустым")
    private final String login;

    private String name;

    @NotNull(message = "Дата рождения не можер быть null")
    @Past(message = "Дата рождения должна быть в прошлом")
    @JsonFormat(pattern="yyyy-MM-dd")
    private final Date birthday;
}