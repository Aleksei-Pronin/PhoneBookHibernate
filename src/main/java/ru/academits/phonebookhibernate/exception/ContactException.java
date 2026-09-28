package ru.academits.phonebookhibernate.exception;

public class ContactException extends RuntimeException {
    public ContactException(String message) {
        super(message);
    }
}