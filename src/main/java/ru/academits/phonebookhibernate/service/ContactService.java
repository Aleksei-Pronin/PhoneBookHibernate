package ru.academits.phonebookhibernate.service;

import ru.academits.phonebookhibernate.entity.Contact;

import java.util.List;

public interface ContactService {
    List<Contact> get(String term);

    void create(Contact contact);

    void update(int contactId, Contact contact);

    void delete(int contactId);

    void delete(List<Integer> contactIds);
}