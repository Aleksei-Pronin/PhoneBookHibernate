package ru.academits.phonebookhibernate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.academits.phonebookhibernate.dao.ContactRepository;
import ru.academits.phonebookhibernate.dto.BaseResponse;
import ru.academits.phonebookhibernate.entity.Contact;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactServiceImpl implements ContactService {
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
    public BaseResponse create(Contact contact) {
        if (contactRepository.existsByPhoneIgnoreCase(contact.getPhone())) {
            return BaseResponse.error("Уже есть другой контакт с таким номером");
        }

        contactRepository.save(contact);
        log.info("Contact created, id={}", contact.getId());
        return BaseResponse.ok();
    }

    @Override
    public BaseResponse update(Contact contact, int contactId) {
        Contact existingContact = contactRepository.findById(contactId)
                .orElse(null);

        if (existingContact == null) {
            return BaseResponse.error("Контакт не найден или был удален");
        }

        if (contactRepository.existsByPhoneIgnoreCaseAndIdNot(contact.getPhone(), contactId)) {
            return BaseResponse.error("Уже есть другой контакт с таким номером");
        }

        existingContact.setSurname(contact.getSurname());
        existingContact.setName(contact.getName());
        existingContact.setPhone(contact.getPhone());

        contactRepository.save(existingContact);
        log.info("Contact updated, id={}", contactId);
        return BaseResponse.ok();
    }

    @Override
    public BaseResponse delete(int contactId) {
        contactRepository.deleteById(contactId);
        log.info("Contact deleted, id={}", contactId);
        return BaseResponse.ok();
    }

    @Override
    public BaseResponse delete(List<Integer> contactIds) {
        contactRepository.deleteAllById(contactIds);
        log.info("Contacts deleted, ids={}", contactIds);
        return BaseResponse.ok();
    }
}