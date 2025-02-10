package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DirectorService {

    private final DirectorStorage directorStorage;

    public List<Director> getAllDirectors() {
        return directorStorage.getDirectors();
    }

    public Director getDirectorById(Long id) {
        return directorStorage.getDirectorById(id);
    }

    public Director addDirector(Director director) {
        directorStorage.addDirector(director);
        return director;
    }

    public Director updateDirector(Director director) {
        directorStorage.updateDirector(director);
        return director;
    }

    public void deleteDirector(Long id) {
        directorStorage.deleteDirector(id);
    }

}
