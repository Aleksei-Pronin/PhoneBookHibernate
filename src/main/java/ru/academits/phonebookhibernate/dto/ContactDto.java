package ru.academits.phonebookhibernate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactDto {
    private Integer id;

    @Size(max = 100, message = "{contact.surname.max}")
    @NotBlank(message = "{contact.surname.required}")
    private String surname;

    @Size(max = 100, message = "{contact.name.max}")
    @NotBlank(message = "{contact.name.required}")
    private String name;

    @Size(max = 20, message = "{contact.phone.max}")
    @NotBlank(message = "{contact.phone.required}")
    private String phone;
}