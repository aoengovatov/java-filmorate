package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void createFilm() throws Exception {
        Film film = new Film(1, "Аватар", "Описание фильма",
                LocalDate.of(1968,12,24), 180);

        mockMvc
                .perform(post( "/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void createFilmNull() throws Exception {
        Film film = null;

        mockMvc
                .perform(post( "/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFilmWithEmptyName() throws Exception {
        Film film = new Film(1, "", "Описание фильма",
                LocalDate.of(1968,12,24), 180);

        mockMvc
                .perform(post( "/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFilmWithFailDescription() throws Exception {
        Film film = new Film(1, "", "Пятеро друзей ( комик-группа «Шарло»), " +
                "приезжают в город Бризуль. Здесь они хотят разыскать господина Огюста Куглова, " +
                "который задолжал им деньги, а именно 20 миллионов. о Куглов, который за время " +
                "«своего отсутствия», стал кандидатом Коломбани.",
                LocalDate.of(1968,12,24), 180);

        mockMvc
                .perform(post( "/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFilmWithFailReleaseDate() throws Exception {
        Film film = new Film(1, "Аватар", "Описание",
                LocalDate.of(1890,12,24), 180);

        mockMvc
                .perform(post( "/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is5xxServerError());
    }

    @Test
    void createFilmWithFailDuration() throws Exception {
        Film film = new Film(1, "Аватар", "Описание",
                LocalDate.of(1990,12,24), -180);

        mockMvc
                .perform(post( "/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateFilm() throws Exception {
        createFilm();
        Film film = new Film(1, "Аватар2", "Описание",
                LocalDate.of(1995,12,24), 180);

        mockMvc
                .perform(put( "/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void updateFilmWithFailId() throws Exception {
        createFilm();
        Film film = new Film(-1, "Аватар2", "Описание",
                LocalDate.of(1995,12,24), 180);

        mockMvc
                .perform(put( "/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is5xxServerError());
    }

    @Test
    void FilmGetAll() throws Exception {
        createFilm();

        mockMvc
                .perform(get( "/films"))
                .andExpect(status().isOk());
    }
}