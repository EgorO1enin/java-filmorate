package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(ValidationException e) {
        return new ErrorResponse("ValidationException", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleException(NotFoundException e) {
        return new ErrorResponse("NotFoundException", e.getMessage());
    }
}
