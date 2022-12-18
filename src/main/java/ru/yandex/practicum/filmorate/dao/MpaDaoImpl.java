package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component
public class MpaDaoImpl implements MpaDao{

    private JdbcTemplate jdbcTemplate;

    public MpaDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Mpa> getAll() {
        String sqlQuery = "select * from mpa";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMpa);
    }

    @Override
    public Optional<Mpa> getById(int mpaId) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from mpa where mpa_id = ?", mpaId);
        if(genreRows.next()) {
            Mpa mpa = new Mpa(
                    genreRows.getInt("mpa_id"),
                    genreRows.getString("mpa_name"));

            log.info("Найден mpa: {} {}", mpa.getId(), mpa.getName());

            return Optional.of(mpa);
        } else {
            log.info("Mpa с id: {} не найден.", mpaId);
            return Optional.empty();
        }
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("mpa_id"))
                .name(resultSet.getString("mpa_name"))
                .build();
    }
}