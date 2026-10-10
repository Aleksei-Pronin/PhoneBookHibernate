package ru.academits.phonebookhibernate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationUserDto {
    @Size(max = 100, message = "{user.name.max}")
    @NotBlank(message = "{user.name.required}")
    private String userName;

    @Size(max = 100, message = "{user.password.max}")
    @NotBlank(message = "{user.password.required}")
    private String password;
}