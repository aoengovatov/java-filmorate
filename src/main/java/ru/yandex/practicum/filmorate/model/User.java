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
@NotNull
public class User {
    private int id;

    @Email
    @NotBlank
    private final String email;

    @NotBlank
    private final String login;

    private String name;

    @NotNull
    @Past
    @JsonFormat(pattern="yyyy-MM-dd")
    private final Date birthday;
}