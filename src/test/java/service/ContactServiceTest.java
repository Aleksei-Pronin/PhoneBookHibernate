package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import ru.academits.phonebookhibernate.dao.ContactRepository;
import ru.academits.phonebookhibernate.dto.BaseResponse;
import ru.academits.phonebookhibernate.entity.Contact;
import ru.academits.phonebookhibernate.service.ContactServiceImpl;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {
    @Mock
    private ContactRepository contactRepository;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private ContactServiceImpl contactService;

    private Contact contact;

    @BeforeEach
    void setUp() {
        contact = new Contact();
        contact.setId(1);
        contact.setSurname("Иванов");
        contact.setName("Иван");
        contact.setPhone("+7999123456");
    }

    // ---------- get ----------

    @Test
    void get_shouldReturnAllContactsWhenTermIsNull() {
        when(contactRepository.findAllByOrderByIdAsc()).thenReturn(List.of(contact));

        List<Contact> result = contactService.get(null);

        assertThat(result).containsExactly(contact);

        verify(contactRepository, times(1))
                .findAllByOrderByIdAsc();
        verify(contactRepository, never())
                .findByTerm(anyString());
    }

    @Test
    void get_shouldReturnAllContactsWhenTermIsBlank() {
        when(contactRepository.findAllByOrderByIdAsc()).thenReturn(List.of(contact));

        List<Contact> result = contactService.get("     ");

        assertThat(result).containsExactly(contact);

        verify(contactRepository, times(1))
                .findAllByOrderByIdAsc();
        verify(contactRepository, never())
                .findByTerm(anyString());
    }

    @Test
    void get_shouldSearchByTermWhenProvided() {
        when(contactRepository.findByTerm("Иван")).thenReturn(List.of(contact));

        List<Contact> result = contactService.get("    Иван  ");

        assertThat(result).containsExactly(contact);

        verify(contactRepository, times(1))
                .findByTerm("Иван");
        verify(contactRepository, never())
                .findAllByOrderByIdAsc();
    }

    @Test
    void get_shouldReturnEmptyListWhenNothingFound() {
        when(contactRepository.findByTerm("Василий"))
                .thenReturn(List.of());

        List<Contact> result = contactService.get("Василий");

        assertThat(result).isEmpty();

        verify(contactRepository, times(1))
                .findByTerm("Василий");
        verify(contactRepository, never())
                .findAllByOrderByIdAsc();
    }

    // ---------- create ----------

    @Test
    void create_shouldSaveContactWhenPhoneIsUnique() {
        when(contactRepository.existsByPhoneIgnoreCase(contact.getPhone()))
                .thenReturn(false);

        BaseResponse response = contactService.create(contact);

        assertThat(response).isEqualTo(BaseResponse.ok());

        verify(contactRepository, times(1))
                .existsByPhoneIgnoreCase(contact.getPhone());
        verify(contactRepository, times(1))
                .save(contact);
        verify(messageSource, never())
                .getMessage(anyString(), any(), any());
    }

    @Test
    void create_shouldNotSaveContactWhenPhoneAlreadyExists() {
        when(contactRepository.existsByPhoneIgnoreCase(contact.getPhone()))
                .thenReturn(true);
        when(messageSource.getMessage(eq("contact.phone.already-exists"), any(), any(Locale.class)))
                .thenReturn("Уже есть другой контакт с таким номером");

        BaseResponse response = contactService.create(contact);

        assertThat(response).isEqualTo(BaseResponse.error("Уже есть другой контакт с таким номером"));

        verify(contactRepository, times(1))
                .existsByPhoneIgnoreCase(contact.getPhone());
        verify(contactRepository, never())
                .save(any());
        verify(messageSource, times(1))
                .getMessage(eq("contact.phone.already-exists"), any(), any(Locale.class));
    }

    // ---------- update ----------

    @Test
    void update_shouldUpdateAndSaveContact() {
        Contact updatedContact = new Contact();
        updatedContact.setSurname("Петров");
        updatedContact.setName("Петр");
        updatedContact.setPhone("+7999654321");

        when(contactRepository.findById(1))
                .thenReturn(Optional.of(contact));
        when(contactRepository.existsByPhoneIgnoreCaseAndIdNot(updatedContact.getPhone(), 1))
                .thenReturn(false);

        BaseResponse response = contactService.update(updatedContact, 1);

        assertThat(response).isEqualTo(BaseResponse.ok());

        assertThat(contact.getSurname()).isEqualTo("Петров");
        assertThat(contact.getName()).isEqualTo("Петр");
        assertThat(contact.getPhone()).isEqualTo("+7999654321");

        verify(contactRepository, times(1))
                .findById(1);
        verify(contactRepository, times(1))
                .existsByPhoneIgnoreCaseAndIdNot(updatedContact.getPhone(), 1);
        verify(contactRepository, times(1))
                .save(contact);
    }

    @Test
    void update_shouldNotSaveContactWhenContactNotFound() {
        when(contactRepository.findById(2))
                .thenReturn(Optional.empty());
        when(messageSource.getMessage(eq("contact.not-found"), any(), any(Locale.class)))
                .thenReturn("Контакт не найден или был удален");

        BaseResponse response = contactService.update(contact, 2);

        assertThat(response).isEqualTo(BaseResponse.error("Контакт не найден или был удален"));

        verify(contactRepository, times(1))
                .findById(2);
        verify(contactRepository, never())
                .existsByPhoneIgnoreCaseAndIdNot(anyString(), anyInt());
        verify(contactRepository, never())
                .save(any());
        verify(messageSource, times(1))
                .getMessage(eq("contact.not-found"), any(), any(Locale.class));
    }

    @Test
    void update_shouldNotSaveContactWhenPhoneAlreadyExists() {
        Contact updatedContact = new Contact();
        updatedContact.setSurname("Петров");
        updatedContact.setName("Петр");
        updatedContact.setPhone("+7999000000");

        when(contactRepository.findById(1))
                .thenReturn(Optional.of(contact));
        when(contactRepository.existsByPhoneIgnoreCaseAndIdNot(updatedContact.getPhone(), 1))
                .thenReturn(true);
        when(messageSource.getMessage(eq("contact.phone.already-exists"), any(), any(Locale.class)))
                .thenReturn("Уже есть другой контакт с таким номером");

        BaseResponse response = contactService.update(updatedContact, 1);

        assertThat(response).isEqualTo(BaseResponse.error("Уже есть другой контакт с таким номером"));

        assertThat(contact.getSurname()).isEqualTo("Иванов");
        assertThat(contact.getName()).isEqualTo("Иван");
        assertThat(contact.getPhone()).isEqualTo("+7999123456");

        verify(contactRepository, times(1))
                .findById(1);
        verify(contactRepository, times(1))
                .existsByPhoneIgnoreCaseAndIdNot(updatedContact.getPhone(), 1);
        verify(contactRepository, never())
                .save(any());
        verify(messageSource, times(1))
                .getMessage(eq("contact.phone.already-exists"), any(), any(Locale.class));
    }

    // ---------- delete ----------

    @Test
    void delete_shouldDeleteContactById() {
        BaseResponse response = contactService.delete(1);

        assertThat(response).isEqualTo(BaseResponse.ok());

        verify(contactRepository, times(1))
                .deleteById(1);
        verify(contactRepository, never())
                .deleteAllById(any());
    }

    @Test
    void delete_shouldDeleteContactsByIds() {
        List<Integer> ids = List.of(1, 2, 3);

        BaseResponse response = contactService.delete(ids);

        assertThat(response).isEqualTo(BaseResponse.ok());

        verify(contactRepository, times(1))
                .deleteAllById(ids);
        verify(contactRepository, never())
                .deleteById(anyInt());
    }
}