package ru.academits.phonebookhibernate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactDto {
    private Integer id;

    @NotBlank(message = "Необходимо заполнить фамилию")
    private String surname;

    @NotBlank(message = "Необходимо заполнить имя")
    private String name;

    @Size(max = 20, message = "Номер телефона не должен превышать 20 символов")
    @NotBlank(message = "Необходимо заполнить номер телефона")
    private String phone;
}