package ru.academits.phonebookhibernate.converter;

import org.springframework.stereotype.Service;
import ru.academits.phonebookhibernate.dto.ContactDto;
import ru.academits.phonebookhibernate.entity.Contact;

@Service
public class ContactDtoToContactConverter implements Converter<ContactDto, Contact> {
    @Override
    public Contact convert(ContactDto source) {
        Contact contact = new Contact();

        contact.setId(source.getId());
        contact.setSurname(source.getSurname().trim());
        contact.setName(source.getName().trim());
        contact.setPhone(source.getPhone().trim());

        return contact;
    }
}