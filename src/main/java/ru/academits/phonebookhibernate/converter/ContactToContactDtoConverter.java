package ru.academits.phonebookhibernate.converter;

import org.springframework.stereotype.Service;
import ru.academits.phonebookhibernate.dto.ContactDto;
import ru.academits.phonebookhibernate.entity.Contact;

@Service
public class ContactToContactDtoConverter implements Converter<Contact, ContactDto> {
    @Override
    public ContactDto convert(Contact source) {
        ContactDto contactDto = new ContactDto();

        contactDto.setId(source.getId());
        contactDto.setSurname(source.getSurname());
        contactDto.setName(source.getName());
        contactDto.setPhone(source.getPhone());

        return contactDto;
    }
}