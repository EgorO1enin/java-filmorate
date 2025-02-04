package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorStorage {
    List<Director> getDirectors();

    Director getDirectorById(Long id);

    Director addDirector(Director director);

    Director updateDirector(Director director);

    void deleteDirector(Long id);

    void addFilmDirector(Long filmId, Long directorId);
}
