package ru.academits.phonebookhibernate.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.academits.phonebookhibernate.dto.BaseResponse;
import ru.academits.phonebookhibernate.service.MessageService;

import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {
    private final MessageService messageService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(System.lineSeparator()));
        log.warn("Validation failed: {}", message);
        return BaseResponse.error(message);
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse handleAll(Exception e) {
        log.error("Unexpected error", e);
        return BaseResponse.error(messageService.getMessage("server.error"));
    }
}