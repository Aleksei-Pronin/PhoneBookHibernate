package ru.academits.phonebookhibernate.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.academits.phonebookhibernate.entity.Contact;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
    List<Contact> findBySurnameContainingIgnoreCaseOrNameContainingIgnoreCaseOrPhoneContainingIgnoreCase(
            String surname, String name, String phone);

    boolean existsByPhoneIgnoreCase(String phone);

    boolean existsByPhoneIgnoreCaseAndIdNot(String phone, int contactId);

    default List<Contact> findByTerm(String term) {
        return findBySurnameContainingIgnoreCaseOrNameContainingIgnoreCaseOrPhoneContainingIgnoreCase(
                term, term, term);
    }
}