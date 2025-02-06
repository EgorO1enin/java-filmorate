package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;


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
    private final DirectorStorage directorDbStorage;
    private final DirectorDbStorageImpl directorDbStorageImpl;


    public Film addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        // Подготовка данных для добавления фильма
        Map<String, Object> filmData = new HashMap<>();
        filmData.put("name", film.getName());
        filmData.put("description", film.getDescription());
        filmData.put("release_date", Date.valueOf(film.getReleaseDate()));
        filmData.put("duration", film.getDuration());
        filmData.put("rating_id", film.getMpa().getId());
        try {
            // Добавление фильма и получение его ID
            long filmId = simpleJdbcInsert.executeAndReturnKey(filmData).longValue();
            film.setId(filmId);
            // Добавление жанров
            if (film.getGenres() != null && !film.getGenres().isEmpty()) {
                for (Genre genre : film.getGenres()) {
                    genreDbStorage.addFilmGenre(filmId, genre.getId());
                }
            }
            // Добавление режиссёров
            if (film.getDirectors() != null && !film.getDirectors().isEmpty()) {
                for (Director director : film.getDirectors()) {
                    // Проверяем, существует ли режиссёр перед добавлением
                    if (director.getId() != null) {
                        directorDbStorage.addFilmDirector(filmId, director.getId());
                    }
                }
            }
            // Обновление MPA
            if (film.getMpa() != null && film.getMpa().getId() != null) {
                Mpa fullMpa = getRatingById(Math.toIntExact(film.getMpa().getId()));
                film.setMpa(fullMpa);
            }
            // Получение полных жанров
            if (film.getGenres() != null && !film.getGenres().isEmpty()) {
                LinkedHashSet<Genre> fullGenres = getFilmGenres(filmId);
                film.setGenres(fullGenres);
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
                getDirectorOfTheFilm(rs.getLong("id"))) // Р’РѕР·РІСЂР°С‰Р°РµРј null, РµСЃР»Рё director_id РїСѓСЃС‚РѕР№
        );
    }

    @Override
    public Film updateFilm(Film film) {
        if (film == null) {
            throw new ValidationException("Фильм не может быть пустым!");
        }

        String sqlQuery = "UPDATE films SET " +
                "name = ?, description = ?, release_date = ?, duration = ?, " +
                "rating_id = ? WHERE id = ?";
        // Обновляем фильм в базе данных
        if (jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()) != 0) {
            // Обновляем MPA
            film.setMpa(mpaService.getMpaById(film.getMpa().getId()));
            // Обновляем жанры
            updateGenres(film);
            // Обновляем режиссёров
            updateDirectors(film);
            return film;
        } else {
            throw new NotFoundException("Фильм с ID=" + film.getId() + " не найден!");
        }
    }

    private void updateGenres(Film film) {
        if (film.getGenres() != null) {
            Collection<Genre> sortedGenres = film.getGenres().stream()
                    .sorted(Comparator.comparing(Genre::getId))
                    .collect(Collectors.toList());
            film.setGenres(new LinkedHashSet<>(sortedGenres));

            for (Genre genre : film.getGenres()) {
                genre.setName(genreService.getGenreById(genre.getId()).getName());
            }
        }
        genreService.deleteGenere(film);
        genreService.addGenere(film);
    }

    private void updateDirectors(Film film) {
        if (film.getDirectors() != null) {
            // Сначала получаем текущих директоров из базы данных для данного фильма
            List<Director> currentDirectors = getDirectorsByFilmId(film.getId());
            // Сортируем директоров по ID
            List<Director> sortedNewDirectors = film.getDirectors().stream()
                    .sorted(Comparator.comparing(Director::getId))
                    .collect(Collectors.toList());
            // Обновляем список директоров
            List<Director> updatedDirectors = new ArrayList<>();
            for (Director director : sortedNewDirectors) {
                Director updatedDirector = directorService.getDirectorById(director.getId());
                if (updatedDirector != null) {
                    updatedDirectors.add(updatedDirector);
                }
            }
            // Обновляем директоров в связной таблице
            updateFilmDirectors(film.getId(), updatedDirectors);
            // Устанавливаем обновленный список директоров в фильм
            film.setDirectors(updatedDirectors);
        }
    }

    @Override
    public List<Director> getDirectorsByFilmId(Long filmId) {
        String sqlQuery = "SELECT d.id, d.name " +
                "FROM directors d " +
                "JOIN film_directors fd ON d.id = fd.director_id " +
                "WHERE fd.film_id = ?";

        return jdbcTemplate.query(sqlQuery, new Object[]{filmId}, (rs, rowNum) -> {
            Director director = new Director();
            director.setId(rs.getLong("id"));
            director.setName(rs.getString("name"));
            return director;
        });
    }

    @Override
    public void updateFilmDirectors(Long filmId, List<Director> directors) {
        // Удаляем старые связи
        directorDbStorage.deleteDirectorsByFilmId(filmId);
        // Добавляем новые связи
        for (Director director : directors) {
            directorDbStorage.addFilmDirector(filmId, director.getId());
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
            LinkedHashSet<Genre> genres = getFilmGenres(filmId);
            // Получаем список директоров
            List<Director> directors = getDirectorOfTheFilm(filmId);
            film = new Film(
                    filmRows.getLong("id"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getInt("duration"),
                    mpa,
                    genres,
                    directors // Если директоров нет, будет пустой список
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
    public LinkedHashSet<Genre> getFilmGenres(Long filmId) {
        String sql = "SELECT genre_id, name FROM film_genres" +
                " INNER JOIN genres ON genre_id = id WHERE film_id = ?";
        LinkedHashSet<Genre> genres = new LinkedHashSet<>(jdbcTemplate.query(sql,
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
    public List<Film> getPopularFilms(int count, Long genreId, Integer year) {
        StringBuilder sql = new StringBuilder(
                "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rating_id, " +
                        "COALESCE(COUNT(DISTINCT fl.user_id), 0) AS like_count " +
                        "FROM films f " +
                        "LEFT JOIN film_likes fl ON f.id = fl.film_id " +
                        "LEFT JOIN film_genres fg ON f.id = fg.film_id ");

        List<Object> params = new ArrayList<>();
        boolean whereAdded = false;

        if (genreId != null) {
            sql.append(" WHERE fg.genre_id = ?");
            params.add(genreId);
            whereAdded = true;
        }

        if (year != null) {
            sql.append(whereAdded ? " AND " : " WHERE ");
            sql.append("EXTRACT(YEAR FROM f.release_date) = ?");
            params.add(year);
        }

        sql.append(" GROUP BY f.id, f.name, f.description, f.release_date, f.duration, f.rating_id ");
        sql.append(" ORDER BY like_count DESC LIMIT ?");
        params.add(count);

        try {
            return jdbcTemplate.query(sql.toString(), (rs, rowNum) -> new Film(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDate("release_date").toLocalDate(),
                    rs.getInt("duration"),
                    new Mpa(rs.getLong("rating_id"), ""),
                    new LinkedHashSet<>(),
                    getDirectorOfTheFilm(rs.getLong("id"))
            ), params.toArray());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка получения популярных фильмов: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Film> getFilmsByDirector(Long directorId, String sortBy) {
        if (directorId == null || sortBy == null || sortBy.isEmpty()) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        String sql;
        if (sortBy.equals("year")) {
            sql = "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rating_id " +
                    "FROM films f " +
                    "JOIN film_directors fd ON f.id = fd.film_id " +
                    "WHERE fd.director_id = ? " +
                    "ORDER BY f.release_date ASC";
        } else if (sortBy.equals("likes")) {
            sql = "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rating_id, COUNT(fl.user_id) AS likes_count " +
                    "FROM films f " +
                    "JOIN film_directors fd ON f.id = fd.film_id " +
                    "LEFT JOIN film_likes fl ON f.id = fl.film_id " +
                    "WHERE fd.director_id = ? " +
                    "GROUP BY f.id " +
                    "ORDER BY likes_count DESC";
        } else {
            throw new ValidationException("Передан неверный аргумент!");
        }
        List<Film> film = jdbcTemplate.query(sql, new Object[]{directorId}, (rs, rowNum) -> new Film(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                getRatingById(rs.getInt("rating_id")),
                getFilmGenres(rs.getLong("id")),
                getDirectorOfTheFilm(rs.getLong("id"))
        ));
        return film;
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
    public List<Director> getDirectorOfTheFilm(Long filmId) {
        String sql = "SELECT director_id, name FROM film_directors" +
                " INNER JOIN directors ON director_id = id WHERE film_id = ?";
        List<Director> directors = jdbcTemplate.query(sql,
                new Object[]{filmId},
                (rs, rowNum) -> new Director(
                        rs.getLong("director_id"),
                        rs.getString("name")
                ));

        return directors;
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

    public List<Film> searchFilms(String query, List<String> searchBy) {
        return null;
    }
}

