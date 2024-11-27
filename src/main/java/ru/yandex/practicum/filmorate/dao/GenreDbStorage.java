package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class GenreDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public void addGenere(Film film) {
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update("INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)",
                        film.getId(), genre.getId());
            }
        }
    }

    public void addFilmGenre(long filmId, Long genreId) {
        String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, genreId);
    }

    public Genre getGenreById(Long genreId) {
        if (genreId == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        Genre genre;
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM genres WHERE id = ?", genreId);
        if (genreRows.first()) {
            genre = new Genre(
                    genreRows.getLong("id"),
                    genreRows.getString("name")
            );
        } else {
            throw new NotFoundException("Жанр с ID=" + genreId + " не найден!");
        }
        return genre;
    }

    public List<Genre> getAllGenres() {
        List<Genre> genres = new ArrayList<>();
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM genres ORDER BY id");
        while (genreRows.next()) {
            genres.add(new Genre(genreRows.getLong("id"), genreRows.getString("name")));
        }
        return genres;
    }

    public void deleteGenre(Film film) {
        jdbcTemplate.update("DELETE FROM film_genres WHERE film_id = ?", film.getId());
    }

    public Set<Genre> getFilmGenres(Long genreId) {
        String sql = "SELECT * FROM film_genres WHERE genre_id = ?";
        return (Set<Genre>) jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(
                rs.getLong("id"),
                rs.getString("name")),
                genreId);

    }
}