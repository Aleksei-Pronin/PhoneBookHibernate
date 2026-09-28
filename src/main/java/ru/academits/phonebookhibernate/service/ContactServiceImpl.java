package ru.academits.phonebookhibernate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.academits.phonebookhibernate.dao.ContactRepository;
import ru.academits.phonebookhibernate.entity.Contact;
import ru.academits.phonebookhibernate.exception.ContactException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactServiceImpl implements ContactService {
    private static final String PHONE_ALREADY_EXISTS = "Уже есть другой контакт с таким номером";
    private static final String CONTACT_NOT_FOUND = "Контакт не найден или был удален";

    private final ContactRepository contactRepository;

    @Override
    public List<Contact> get(String term) {
        if (term == null || term.isBlank()) {
            List<Contact> contacts = contactRepository.findAll();
            log.debug("Loaded {} contact(s)", contacts.size());
            return contacts;
        }

        List<Contact> contacts = contactRepository.findByTerm(term.trim());
        log.debug("Found {} contact(s)", contacts.size());
        return contacts;
    }

    @Override
    public void create(Contact contact) {
        if (contactRepository.existsByPhoneIgnoreCase(contact.getPhone())) {
            throw new ContactException(PHONE_ALREADY_EXISTS);
        }

        contactRepository.save(contact);
        log.info("Contact created, id={}", contact.getId());
    }

    @Override
    public void update(int contactId, Contact contact) {
        Contact existingContact = contactRepository.findById(contactId)
                .orElseThrow(() -> new ContactException(CONTACT_NOT_FOUND));

        if (contactRepository.existsByPhoneIgnoreCaseAndIdNot(contact.getPhone(), contactId)) {
            throw new ContactException(PHONE_ALREADY_EXISTS);
        }

        existingContact.setSurname(contact.getSurname());
        existingContact.setName(contact.getName());
        existingContact.setPhone(contact.getPhone());

        contactRepository.save(existingContact);
        log.info("Contact updated, id={}", contactId);
    }

    @Override
    public void delete(int contactId) {
        contactRepository.deleteById(contactId);
        log.info("Contact deleted, id={}", contactId);
    }

    @Override
    public void delete(List<Integer> contactIds) {
        contactRepository.deleteAllById(contactIds);
        log.info("Contacts deleted, ids={}", contactIds);
    }
}