package ru.academits.phonebookhibernate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.academits.phonebookhibernate.converter.ContactDtoToContactConverter;
import ru.academits.phonebookhibernate.converter.ContactToContactDtoConverter;
import ru.academits.phonebookhibernate.dto.BaseResponse;
import ru.academits.phonebookhibernate.dto.ContactDto;
import ru.academits.phonebookhibernate.service.ContactService;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;
    private final ContactToContactDtoConverter contactToContactDtoConverter;
    private final ContactDtoToContactConverter contactDtoToContactConverter;

    @GetMapping
    public List<ContactDto> getContacts(@RequestParam(required = false) String term) {
        return contactToContactDtoConverter.convert(contactService.get(term));
    }

    @PostMapping
    public BaseResponse createContact(@Valid @RequestBody ContactDto contact) {
        return contactService.create(contactDtoToContactConverter.convert(contact));
    }

    @PutMapping("/{id}")
    public BaseResponse updateContact(@Valid @RequestBody ContactDto contact, @PathVariable int id) {
        return contactService.update(contactDtoToContactConverter.convert(contact), id);
    }

    @DeleteMapping("/{id}")
    public BaseResponse deleteContact(@PathVariable int id) {
        return contactService.delete(id);
    }

    @DeleteMapping
    public BaseResponse deleteContacts(@RequestBody List<Integer> contactIds) {
        return contactService.delete(contactIds);
    }
}