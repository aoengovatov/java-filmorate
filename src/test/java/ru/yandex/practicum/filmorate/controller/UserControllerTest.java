package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.text.SimpleDateFormat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    void createUserOne() throws Exception {
        User user = new User(1, "mail@mail.ru", "testLogin", "testName",formatter.parse("1968-12-24"));

        mockMvc
            .perform(post( "/users")
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isOk());
    }

    @Test
    void createUserTwo() throws Exception {
        User user = new User(2, "mail2@mail.ru", "testLogin2", "testName2",formatter.parse("1977-12-24"));

        mockMvc
                .perform(post( "/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void createUserWithBadEmail() throws Exception {
        User user = new User(1, "@mailmail.ru", "testLogin", "testName",formatter.parse("1968-12-24"));

        mockMvc
            .perform(post( "/users")
                 .content(objectMapper.writeValueAsString(user))
                 .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserWithBadlLogin() throws Exception {
        User user = new User(1, "mail@mail.ru", "Bad Login", "testName",formatter.parse("1968-12-24"));

        mockMvc
            .perform(post( "/users")
            .content(objectMapper.writeValueAsString(user))
            .contentType(MediaType.APPLICATION_JSON)
       )
            .andExpect(status().is5xxServerError());
    }

    @Test
    void createUserWithFailBirthday() throws Exception {
        User user = new User(1, "mail@mail.ru", "Login", "testName",formatter.parse("2048-12-24"));

        mockMvc
            .perform(post( "/users")
            .content(objectMapper.writeValueAsString(user))
            .contentType(MediaType.APPLICATION_JSON)
       )
            .andExpect(status().isBadRequest());
    }

    @Test
    void createUserWithEmptyName() throws Exception {
        User user = new User(1, "mail@mail.ru", "Login", "",formatter.parse("1985-12-24"));

        mockMvc
                .perform(post( "/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void createUserWithFailName() throws Exception {
        User user = new User(1, "mail@mail.ru", "Login", "  ",formatter.parse("1985-12-24"));

        mockMvc
                .perform(post( "/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is5xxServerError());
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
        User user = new User(1, "mail@mail.ru", "NEWLogin", "NEWtestName",formatter.parse("1995-12-24"));

        mockMvc
            .perform(put( "/users")
            .content(objectMapper.writeValueAsString(user))
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk());
    }

    @Test
    void updateUserWithBadId() throws Exception {
        User user = new User(-1, "mail@mail.ru", "NEWLogin", "NEWtestName",formatter.parse("1995-12-24"));

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