package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {

    public Mpa getMpaById(Long mpaId);

    public List<Mpa> getAllMpas();
}
