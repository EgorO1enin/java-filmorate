package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Component
public class FilmDbStorageImpl implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final LikesDbStorageImpl likeDbStorage;
    private final GenreDbStorageImpl genreDbStorage;
    private final MpaService mpaService;
    private final GenreService genreService;
    private final FilmRowMapper filmRowMapper;
    private final FilmRowMapper rowMapper;
    private final UserService userService;

    public FilmDbStorageImpl(final JdbcTemplate jdbcTemplate, LikesDbStorageImpl likeDbStorage, GenreDbStorageImpl genreDbStorage, MpaService mpaService, GenreService genreService, FilmRowMapper filmRowMapper, FilmRowMapper rowMapper, UserService userService) {
        this.jdbcTemplate = jdbcTemplate;
        this.likeDbStorage = likeDbStorage;
        this.genreDbStorage = genreDbStorage;
        this.mpaService = mpaService;
        this.genreService = genreService;
        this.filmRowMapper = filmRowMapper;
        this.rowMapper = rowMapper;
        this.userService = userService;
    }

    @Transactional
    public Film addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> filmData = Map.of(
                "name", film.getName(),
                "description", film.getDescription(),
                "release_date", Date.valueOf(film.getReleaseDate()),
                "duration", film.getDuration(),
                "rating_id", film.getMpa().getId()
        );
        try {
            long filmId = simpleJdbcInsert.executeAndReturnKey(filmData).longValue();
            film.setId(filmId);
            if (film.getGenres() != null && !film.getGenres().isEmpty()) {
                for (Genre genre : film.getGenres()) {
                    genreDbStorage.addFilmGenre(filmId, genre.getId());
                }
            }
            return film;
        } catch (Exception e) {
            throw new NotFoundException("Ошибка при добавлении фильма");
        }
    }

    @Override
    public List<Film> getFilms() {
        String sql = "SELECT * FROM films";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Film(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_Date").toLocalDate(),
                rs.getInt("duration"),
                getRatingById(rs.getInt("rating_id")),
                getFilmGenres(rs.getLong("id")))
        );
    }

    @Override
    public Film updateFilm(Film film) {
        if (film == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        String sqlQuery = "UPDATE films SET " +
                "name = ?, description = ?, release_date = ?, duration = ?, " +
                "rating_id = ? WHERE id = ?";
        if (jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()) != 0) {
            film.setMpa(mpaService.getMpaById(film.getMpa().getId()));
            if (film.getGenres() != null) {
                Collection<Genre> sortGenres = film.getGenres().stream()
                        .sorted(Comparator.comparing(Genre::getId))
                        .collect(toList());
                film.setGenres(new LinkedHashSet<>(sortGenres));
                for (Genre genre : film.getGenres()) {
                    genre.setName(genreService.getGenreById(genre.getId()).getName());
                }
            }
            genreService.deleteGenere(film);
            genreService.addGenere(film);
            return film;
        } else {
            throw new NotFoundException("Фильм с ID=" + film.getId() + " не найден!");
        }
    }

    @Override
    public Film getFilm(Long filmId) {
        if (filmId == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        Film film;
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE id = ?", filmId);
        if (filmRows.first()) {
            Mpa mpa = getRatingById(filmRows.getInt("rating_id"));
            Set<Genre> genres = getFilmGenres(filmId);
            film = new Film(
                    filmRows.getLong("id"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getInt("duration"),
                    mpa,
                    genres);
        } else {
            throw new NotFoundException("Фильм с ID=" + filmId + " не найден!");
        }
        if (film.getGenres().isEmpty()) {
            film.setGenres(null);
        }
        return film;
    }

    public Set<Genre> getFilmGenres(Long filmId) {
        String sql = "SELECT genre_id, name FROM film_genres" +
                " INNER JOIN genres ON genre_id = id WHERE film_id = ?";
        Set<Genre> genres = new HashSet<>(jdbcTemplate.query(sql,
                (rs, rowNum) -> new Genre(rs.getLong("genre_id"), rs.getString("name")), filmId));
        return genres;
    }

    public Mpa getRatingById(Integer rId) {
        if (rId == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        Mpa mpa;
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM ratings_mpa WHERE id = ?", rId);
        if (mpaRows.first()) {
            mpa = new Mpa(
                    mpaRows.getLong("id"),
                    mpaRows.getString("name")
            );
        } else {
            throw new NotFoundException("Рейтинг с ID=" + rId + " не найден!");
        }
        return mpa;
    }

    public List<Film> getPopularFilms(int count) {
        String sql = "SELECT * FROM FILMS f LEFT JOIN RATINGS_MPA m ON f.RATING_ID = m.ID" +
                " LEFT JOIN ( SELECT FILM_ID, COUNT(FILM_ID) AS LIKES FROM FILM_LIKES GROUP BY FILM_ID) fl " +
                "ON f.ID = fl.FILM_ID ORDER BY LIKES DESC LIMIT ?";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new Film(
                        rs.getLong("film_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("release_date").toLocalDate(),
                        rs.getInt("duration"),
                        getRatingById(rs.getInt("rating_id")),
                        getFilmGenres(rs.getLong("film_id"))), count);

    }

    public List<Film> getCommonFilms(Long userId, Long friendId) {
        User firstUser = userService.getUserById(userId);
        User lastUser = userService.getUserById(friendId);
        if (firstUser == null || lastUser == null) {
            throw new ValidationException("First User or Second User not found");
        }

        String sql = "SELECT film_id FROM film_likes WHERE user_id = ?";
        List<Long> usersFilms = jdbcTemplate.queryForList(sql, Long.class, userId);
        List<Long> friendsFilms = jdbcTemplate.queryForList(sql, Long.class, friendId);
        return usersFilms.stream()
                .filter(friendsFilms::contains)
                .map(this::getFilm)
                .sorted(Comparator.comparing(this::getLikesCount).reversed())
                .toList();
    }

    public Integer getLikesCount(Film film) {
        String sql = "SELECT COUNT(film_id) FROM film_likes WHERE film_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, film.getId());
    }
}

