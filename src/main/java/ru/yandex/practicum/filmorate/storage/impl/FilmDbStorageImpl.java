package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.util.*;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Component
public class FilmDbStorageImpl implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final LikesDbStorageImpl likeDbStorage;
    private final GenreDbStorageImpl genreDbStorage;
    private final MpaService mpaService;
    private final GenreService genreService;
    private final FilmRowMapper filmRowMapper;
    private final FilmRowMapper rowMapper;
    private final DirectorService directorService;
    private final UserService userService;


    @Transactional
    public Film addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");

        // Создаем карту данных для вставки
        Map<String, Object> filmData = new HashMap<>();
        filmData.put("name", film.getName());
        filmData.put("description", film.getDescription());
        filmData.put("release_date", Date.valueOf(film.getReleaseDate()));
        filmData.put("duration", film.getDuration());
        filmData.put("rating_id", film.getMpa().getId());

        // Проверяем, есть ли идентификатор режиссера и добавляем его, если он существует
        if (film.getDirector() != null && film.getDirector().getId() != null) {
            filmData.put("director_id", film.getDirector().getId());
        }

        try {
            // Сохраняем фильм и получаем его ID
            long filmId = simpleJdbcInsert.executeAndReturnKey(filmData).longValue();
            film.setId(filmId);

            // Добавляем жанры, если они есть
            if (film.getGenres() != null && !film.getGenres().isEmpty()) {
                for (Genre genre : film.getGenres()) {
                    genreDbStorage.addFilmGenre(filmId, genre.getId());
                }
            }

            // Загружаем полные данные для MPA
            if (film.getMpa() != null && film.getMpa().getId() != null) {
                Mpa fullMpa = getRatingById(Math.toIntExact(film.getMpa().getId()));
                film.setMpa(fullMpa);
            }

            // Загружаем полные данные для жанров
            if (film.getGenres() != null && !film.getGenres().isEmpty()) {
                Set<Genre> fullGenres = getFilmGenres(filmId);
                film.setGenres(fullGenres);
            }

            // Загружаем полные данные для режиссера
            if (film.getDirector() != null && film.getDirector().getId() != null) {
                Director fullDirector = directorService.getDirectorById(film.getDirector().getId());
                film.setDirector(fullDirector);
            }

            return film;
        } catch (Exception e) {
            throw new NotFoundException("Ошибка при добавлении фильма: " + e.getMessage());
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
                getFilmGenres(rs.getLong("id")),
                Optional.of(rs.getLong("director_id"))
                        .filter(directorId -> directorId != 0)
                        .map(directorService::getDirectorById)
                        .orElse(null) // Возвращаем null, если director_id пустой
        ));
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
            Director director = directorService.getDirectorById(filmRows.getLong("director_id"));

            film = new Film(
                    filmRows.getLong("id"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getInt("duration"),
                    mpa,
                    genres,
                    director
            );
        } else {
            throw new NotFoundException("Фильм с ID=" + filmId + " не найден!");
        }
        if (film.getGenres().isEmpty()) {
            film.setGenres(null);
        }
        return film;
    }

    @Override
    public void removeFilm(long id) {
        if (getFilm(id) != null) {
            String sqlQuery = "DELETE FROM films WHERE id = ?";
            jdbcTemplate.update(sqlQuery, id);
        } else {
            throw new NotFoundException("Пользователь с ID=" + id + " не найден!");
        }
    }

    @Override
    public Set<Genre> getFilmGenres(Long filmId) {
        String sql = "SELECT genre_id, name FROM film_genres" +
                " INNER JOIN genres ON genre_id = id WHERE film_id = ?";
        Set<Genre> genres = new HashSet<>(jdbcTemplate.query(sql,
                (rs, rowNum) -> new Genre(rs.getLong("genre_id"), rs.getString("name")), filmId));
        return genres;
    }

    @Override
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

    @Override
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
                        getFilmGenres(rs.getLong("film_id")),
                        directorService.getDirectorById(rs.getLong("director_id"))), count);

    }

    @Override
    public List<Film> getFilmsByDirector(Long directorId, String sortBy) {
        if (directorId == null || sortBy == null || sortBy.isEmpty()) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        String sql;
        Object[] params = new Object[]{directorId};
        if (sortBy.equals("year")) {
            sql = "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rating_id " +
                    "FROM films f " +
                    "WHERE f.director_id = ? " +
                    "ORDER BY f.release_date DESC";
        } else if (sortBy.equals("likes")) {
            sql = "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rating_id, COUNT(fl.user_id) AS likes_count " +
                    "FROM films f " +
                    "LEFT JOIN film_likes fl ON f.id = fl.film_id " +
                    "WHERE f.director_id = ? " +
                    "GROUP BY f.id " +
                    "ORDER BY likes_count DESC";
        } else {
            throw new ValidationException("Передан неверный аргумент!");
        }

        return jdbcTemplate.query(sql, params,
                (rs, rowNum) -> new Film(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("release_date").toLocalDate(),
                        rs.getInt("duration"),
                        getRatingById(rs.getInt("rating_id")),
                        getFilmGenres(rs.getLong("id")),
                        directorService.getDirectorById(directorId)));
    }

    @Override
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

    @Override
    public Integer getLikesCount(Film film) {
        String sql = "SELECT COUNT(film_id) FROM film_likes WHERE film_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, film.getId());
    }

    @Override
    public Director getDirectorOfTheFilm(Long id) {
        if (id == null) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        Director director;
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE id = ?", id);
        if (filmRows.first()) {
            director = directorService.getDirectorById(filmRows.getLong("director_id"));

        } else {
            throw new NotFoundException("У фильма с ID=" + id + " не найден режисер!");
        }
        return director;
    }

    public List<Film> getUserRecommendations(Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        String userSql = "SELECT user_id FROM film_likes " +
                "WHERE film_id IN (SELECT film_id FROM film_likes WHERE user_id = ?) AND user_id <> ?" +
                "GROUP BY user_id " +
                "ORDER BY COUNT(film_id)";
        List<Long> usersIds = jdbcTemplate.queryForList(userSql, Long.class, userId, userId);
        if (usersIds.isEmpty()) {
            return List.of();
        }
        Long recommendUserId = usersIds.getFirst();
        String filmSql = "SELECT film_id FROM film_likes WHERE user_id = ? AND film_id NOT IN " +
                "(SELECT film_id FROM film_likes WHERE user_id = ?)";
        List<Long> filmIds = jdbcTemplate.queryForList(filmSql, Long.class, recommendUserId, userId);
        return filmIds.stream()
                .map(this::getFilm)
                .toList();
    }
}


