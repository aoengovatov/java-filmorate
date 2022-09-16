package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void createUserOne() throws Exception {
        User user = new User(1, "mail@mail.ru", "testLogin", "testName",
                LocalDate.of(1968,12,24));

        mockMvc
            .perform(post( "/users")
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isOk());
    }

    @Test
    void createUserTwo() throws Exception {
        User user = new User(2, "mail2@mail.ru", "testLogin2", "testName2",
                LocalDate.of(1977,12,24));

        mockMvc
                .perform(post( "/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void createUserWithBadEmail() throws Exception {
        User user = new User(1, "@mailmail.ru", "testLogin", "testName",
                LocalDate.of(1968,12,24));

        mockMvc
            .perform(post( "/users")
                 .content(objectMapper.writeValueAsString(user))
                 .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserWithBadlLogin() throws Exception {
        User user = new User(1, "mail@mail.ru", "Bad Login", "testName",
                LocalDate.of(1968,12,24));

        mockMvc
            .perform(post( "/users")
            .content(objectMapper.writeValueAsString(user))
            .contentType(MediaType.APPLICATION_JSON)
       )
            .andExpect(status().is5xxServerError());
    }

    @Test
    void createUserWithFailBirthday() throws Exception {
        User user = new User(1, "mail@mail.ru", "Login", "testName",
                LocalDate.of(2048,12,24));

        mockMvc
            .perform(post( "/users")
            .content(objectMapper.writeValueAsString(user))
            .contentType(MediaType.APPLICATION_JSON)
       )
            .andExpect(status().isBadRequest());
    }

    @Test
    void createUserWithEmptyName() throws Exception {
        User user = new User(1, "mail@mail.ru", "Login", "",
                LocalDate.of(1985,12,24));

        mockMvc
                .perform(post( "/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void createUserWithBlankName() throws Exception {
        User user = new User(1, "mail@mail.ru", "Login", "  ",
                LocalDate.of(1985,12,24));

        mockMvc
                .perform(post( "/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void createUserWithNullName() throws Exception {
        User user = new User(1, "mail@mail.ru", "Login", null,
                LocalDate.of(1985,12,24));

        mockMvc
                .perform(post( "/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void createUserNull() throws Exception {
        User user = null;

        mockMvc
                .perform(post( "/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser() throws Exception {
        createUserOne();
        User user = new User(1, "mail@mail.ru", "NEWLogin", "NEWtestName",
                LocalDate.of(1995,12,24));

        mockMvc
            .perform(put( "/users")
            .content(objectMapper.writeValueAsString(user))
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk());
    }

    @Test
    void updateUserWithBadId() throws Exception {
        User user = new User(-1, "mail@mail.ru", "NEWLogin", "NEWtestName",
                LocalDate.of(1995,12,24));

        mockMvc
                .perform(put( "/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is5xxServerError());
    }

    @Test
    void UserGetAll() throws Exception {
        createUserOne();
        createUserTwo();

        mockMvc
            .perform(get( "/users"))
            .andExpect(status().isOk());
    }
}