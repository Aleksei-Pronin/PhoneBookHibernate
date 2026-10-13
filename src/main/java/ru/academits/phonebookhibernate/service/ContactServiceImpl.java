package ru.academits.phonebookhibernate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.academits.phonebookhibernate.dao.ContactRepository;
import ru.academits.phonebookhibernate.dao.UserRepository;
import ru.academits.phonebookhibernate.dto.BaseResponse;
import ru.academits.phonebookhibernate.entity.Contact;
import ru.academits.phonebookhibernate.security.CurrentUser;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;
    private final CurrentUser currentUser;
    private final UserRepository userRepository;
    private final MessageService messageService;

    @Override
    @Transactional(readOnly = true)
    public List<Contact> get(String term) {
        List<Contact> contacts;

        if (currentUser.isAdmin()) {
            contacts = isNullOrBlank(term)
                    ? contactRepository.findAllByOrderByIdAsc()
                    : contactRepository.findByTerm(term.trim());
        } else {
            Long userId = currentUser.getId();

            contacts = isNullOrBlank(term)
                    ? contactRepository.findByUserIdOrderByIdAsc(userId)
                    : contactRepository.findByUserIdAndTerm(userId, term.trim());
        }

        log.debug("Loaded {} contact(s)", contacts.size());
        return contacts;
    }

    @Override
    @Transactional
    public BaseResponse create(Contact contact) {
        Long userId = currentUser.getId();

        if (contactRepository.existsByPhoneIgnoreCaseAndUserId(contact.getPhone(), userId)) {
            return BaseResponse.error(messageService.getMessage("contact.phone.already-exists"));
        }

        contact.setUser(userRepository.getReferenceById(userId));
        contactRepository.save(contact);
        log.info("Contact created, id={}", contact.getId());
        return BaseResponse.ok();
    }

    @Override
    @Transactional
    public BaseResponse update(Contact contact, int contactId) {
        Contact existingContact = currentUser.isAdmin()
                ? contactRepository.findById(contactId).orElse(null)
                : contactRepository.findByIdAndUserId(contactId, currentUser.getId()).orElse(null);

        if (existingContact == null) {
            return BaseResponse.error(messageService.getMessage("contact.not-found"));
        }

        Long contactOwnerId = existingContact.getUser().getId();

        if (contactRepository.existsByPhoneIgnoreCaseAndUserIdAndIdNot(contact.getPhone(), contactOwnerId, contactId)) {
            return BaseResponse.error(messageService.getMessage("contact.phone.already-exists"));
        }

        existingContact.updateFrom(contact);

        contactRepository.save(existingContact);
        log.info("Contact updated, id={}", contactId);
        return BaseResponse.ok();
    }

    @Override
    @Transactional
    public BaseResponse delete(int contactId) {
        int deletedContactsCount = currentUser.isAdmin()
                ? contactRepository.deleteContactById(contactId)
                : contactRepository.deleteByIdAndUserId(contactId, currentUser.getId());

        if (deletedContactsCount == 0) {
            return BaseResponse.error(messageService.getMessage("contact.not-found"));
        }

        log.info("Contact deleted, id={}", contactId);
        return BaseResponse.ok();
    }

    @Override
    @Transactional
    public BaseResponse delete(List<Integer> contactIds) {
        int deletedContactsCount = currentUser.isAdmin()
                ? contactRepository.deleteContactsByIds(contactIds)
                : contactRepository.deleteAllByIdAndUserId(contactIds, currentUser.getId());

        if (deletedContactsCount == 0) {
            return BaseResponse.error(messageService.getMessage("contact.not-found"));
        }

        log.info("Contacts deleted, ids={}", contactIds);
        return BaseResponse.ok();
    }

    private boolean isNullOrBlank(String term) {
        return term == null || term.isBlank();
    }
}