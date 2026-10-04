package ru.academits.phonebookhibernate.service;

import ru.academits.phonebookhibernate.dto.BaseResponse;
import ru.academits.phonebookhibernate.entity.Contact;

import java.util.List;

public interface ContactService {
    List<Contact> get(String term);

    BaseResponse create(Contact contact);

    BaseResponse update(Contact contact, int contactId);

    BaseResponse delete(int contactId);

    BaseResponse delete(List<Integer> contactIds);
}