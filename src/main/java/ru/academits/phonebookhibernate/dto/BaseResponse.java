package ru.academits.phonebookhibernate.dto;

public record BaseResponse(boolean success, String message) {
    public static BaseResponse ok() {
        return new BaseResponse(true, null);
    }

    public static BaseResponse error(String message) {
        return new BaseResponse(false, message);
    }
}