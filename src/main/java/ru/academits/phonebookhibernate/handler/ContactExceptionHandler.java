package ru.academits.phonebookhibernate.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.academits.phonebookhibernate.dto.BaseResponse;
import ru.academits.phonebookhibernate.exception.ContactException;

@RestControllerAdvice
@Slf4j
public class ContactExceptionHandler {

    @ExceptionHandler(ContactException.class)
    public BaseResponse handleContactException(ContactException e) {
        log.warn("Contact operation failed: {}", e.getMessage());
        return BaseResponse.error(e.getMessage());
    }
}