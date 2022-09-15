package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.*;
import java.util.Date;

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

    @NotNull(message = "Дата рождения не можер быть null")
    @PastOrPresent(message = "Дата рождения должна быть в прошлом")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date birthday;
}