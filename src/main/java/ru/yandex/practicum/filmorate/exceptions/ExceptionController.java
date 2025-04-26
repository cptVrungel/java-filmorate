package ru.yandex.practicum.filmorate.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Optional;

@ControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Response handleNotFoundException(NotFoundException exc) {
        log.error("Ошибка при поиске: {}", exc.getMessage());
        return new Response(exc.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Response handleValidationException(ValidationException exc) {
        log.error("Ошибка при валидации: {}", exc.getMessage());
        return new Response(exc.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Response handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exc) {
        log.error("Некорректный ввод данных в запросе: {}", exc.getMessage());
        return new Response(exc.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Response> handleBasicException(MethodArgumentNotValidException exc) {
        String error = Optional.ofNullable(exc.getBindingResult().getFieldError()).map(error0 ->
                error0.getDefaultMessage()).orElse("Ошибка валидации");
        log.error("Ошибка при добавлении: {}", error);

        if (error.equals("Фильма с таким ID нет") || error.equals("Пользователя с таким ID нет !")) {
            return new ResponseEntity<>(new Response(error), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(new Response(error), HttpStatus.BAD_REQUEST);
        }
    }
}