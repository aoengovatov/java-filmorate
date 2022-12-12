package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;

import javax.validation.ConstraintViolationException;
import java.net.http.HttpHeaders;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<List<String>> handleIncorrectParameterException(IncorrectParameterException e) {
        log.warn(e.getMessage());
        List<String> errors = new ArrayList<>();
        errors.add(e.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<List<String>> handleFilmNotFoundException(FilmNotFoundException e) {
        log.warn(e.getMessage());
        List<String> errors = new ArrayList<>();
        errors.add(e.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<List<String>> handleUserNotFoundException(UserNotFoundException e) {
        log.warn(e.getMessage());
        List<String> errors = new ArrayList<>();
        errors.add(e.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<List<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn(e.getMessage());
        List<String> errors = new ArrayList<>();
        errors.add(e.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<List<String>> handleConstraintViolationException(ConstraintViolationException e) {
        log.warn(e.getMessage());
        List<String> errors = new ArrayList<>();
        errors.add(e.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<List<String>> handleThrowableException(Throwable e) {
        log.warn(e.getMessage());
        List<String> errors = new ArrayList<>();
        errors.add(e.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}