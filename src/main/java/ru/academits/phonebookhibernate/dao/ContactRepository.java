package ru.academits.phonebookhibernate.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.academits.phonebookhibernate.entity.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
    List<Contact> findAllByOrderByIdAsc();

    List<Contact> findByUserIdOrderByIdAsc(Long userId);

    @Query(name = "Contact.findByTerm")
    List<Contact> findByTerm(String term);

    @Query(name = "Contact.findByUserIdAndTerm")
    List<Contact> findByUserIdAndTerm(Long userId, String term);

    Optional<Contact> findByIdAndUserId(Integer id, Long userId);

    boolean existsByPhoneIgnoreCaseAndUserId(String phone, Long userId);

    boolean existsByPhoneIgnoreCaseAndUserIdAndIdNot(String phone, Long userId, int contactId);

    @Modifying
    @Query(name = "Contact.deleteContactById")
    int deleteContactById(int contactId);

    @Modifying
    @Query(name = "Contact.deleteContactsByIds")
    int deleteContactsByIds(List<Integer> ids);

    @Modifying
    @Query(name = "Contact.deleteByIdAndUserId")
    int deleteByIdAndUserId(int contactId, Long userId);

    @Modifying
    @Query(name = "Contact.deleteAllByIdAndUserId")
    int deleteAllByIdAndUserId(List<Integer> ids, Long userId);
}