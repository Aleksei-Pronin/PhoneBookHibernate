package ru.academits.phonebookhibernate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
    private final MessageSource messageSource;

    @Override
    public List<Contact> get(String term) {
        List<Contact> contacts;

        if (term == null || term.isBlank()) {
            contacts = contactRepository.findAllByOrderByIdAsc();
        } else {
            contacts = contactRepository.findByTerm(term.trim());
        }

        log.debug("Loaded {} contact(s)", contacts.size());
        return contacts;
    }

    @Override
    public BaseResponse create(Contact contact) {
        if (contactRepository.existsByPhoneIgnoreCase(contact.getPhone())) {
            return BaseResponse.error(getMessage("contact.phone.already-exists"));
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
            return BaseResponse.error(getMessage("contact.not-found"));
        }

        if (contactRepository.existsByPhoneIgnoreCaseAndIdNot(contact.getPhone(), contactId)) {
            return BaseResponse.error(getMessage("contact.phone.already-exists"));
        }

        existingContact.updateFrom(contact);

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

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }
}