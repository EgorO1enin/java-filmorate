package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.impl.MpaDbStorageImpl;

import java.util.List;

@Service
public class MpaService {
    private final MpaDbStorageImpl mpaDbStorage;

    public MpaService(final MpaDbStorageImpl mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public Mpa getMpaById(Long mpaId) {
        return mpaDbStorage.getMpaById(mpaId);
    }

    public List<Mpa> getAllMpas() {
        return mpaDbStorage.getAllMpas();
    }
}
