package com.service;

import com.model.Phone;
import com.model.PhoneManufacture;
import com.repository.PhoneRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

import static org.mockito.ArgumentMatchers.*;

class PhoneServiceTest {

    private PhoneService target;
    private PhoneRepository repository;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(PhoneRepository.class);
        target = new PhoneService(repository);
    }

    @Test
    void createAndSavePhones_negativeCount() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> target.createAndSavePhones(-1));
    }

    @Test
    void createAndSavePhones_zeroCount() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> target.createAndSavePhones(0));
    }

    @Test
    void createAndSavePhones() {
        target.createAndSavePhones(2);
        Mockito.verify(repository).saveAll(Mockito.anyList());
    }

    @Test
    void printAll() {
        target.printAll();
        Mockito.verify(repository).getAll();
    }

    @Test
    void savePhone() {
        final Phone phone = new Phone("Title", 100, 1000.0,
                "Model", PhoneManufacture.APPLE);
        target.savePhone(phone);

        ArgumentCaptor<Phone> argument = ArgumentCaptor.forClass(Phone.class);
        Mockito.verify(repository).save(argument.capture());
        Assertions.assertEquals("Title", argument.getValue().getTitle());
    }

    @Test
    void savePhone_zeroCount() {
        final Phone phone = new Phone("Title", 0, 1000.0,
                "Model", PhoneManufacture.APPLE);
        target.savePhone(phone);

        ArgumentCaptor<Phone> argument = ArgumentCaptor.forClass(Phone.class);
        Mockito.verify(repository).save(argument.capture());
        Assertions.assertEquals("Title", argument.getValue().getTitle());
        Assertions.assertEquals(-1, argument.getValue().getCount());
    }

    @Test
    void countOfPrintAllCalls() {
        target.savePhone(new Phone("Title-2", 200, 1000.0,
                "Model-2", PhoneManufacture.SAMSUNG));
        target.getAll();
        target.printAll();
        target.printAll();
        target.printAll();
        Mockito.verify(repository, Mockito.times(4)).getAll();
    }

    @Test
    void getAll() {
        target.getAll();
        Mockito.verify(repository).getAll();
    }

    @Test
    void getAll_CallReaMethod() {
        target.getAll();
        Mockito.doCallRealMethod().when(repository).getAll();
        Mockito.verify(repository).getAll();
    }

    @Test
    void deletePhone_ArgumentMatcher() {
        Phone phone = new Phone("Title-1", 300, 700.0,
                "Model-1", PhoneManufacture.SONY);
        Phone otherPhone = new Phone("Title-2", 200, 1000.0,
                "Model-2", PhoneManufacture.SAMSUNG);
        target.savePhone(phone);
        target.savePhone(otherPhone);
        target.deletePhone(phone);

        ArgumentMatcher<String> hasPhoneWithId =
                matchesPhoneId -> matchesPhoneId.equals(otherPhone.getId());
        Mockito.when(repository.hasPhone(argThat(hasPhoneWithId))).thenReturn(true);
        Assertions.assertFalse(repository.hasPhone(phone.getId()));
        Assertions.assertTrue(repository.hasPhone(otherPhone.getId()));
    }

    @Test
    void deletePhone_ArgumentCaptor() {
        Phone phone = new Phone("Title-1", 300, 700.0,
                "Model-1", PhoneManufacture.SONY);
        Phone otherPhone = new Phone("Title-2", 200, 1000.0,
                "Model-2", PhoneManufacture.SAMSUNG);
        target.savePhone(phone);
        target.savePhone(otherPhone);
        target.deletePhone(phone);

        ArgumentCaptor<Phone> phoneArgumentCaptor = ArgumentCaptor.forClass(Phone.class);
        Mockito.when(repository.update(phoneArgumentCaptor.capture())).thenReturn(true);
        Assertions.assertFalse(repository.hasPhone(phone.getId()));
    }

    @Test
    void updatePhone_throwing() {
        Phone phone = new Phone("Title", 300, 700,
                "Model", PhoneManufacture.SAMSUNG);
        Phone otherPhone = new Phone("Title-1", 200, 1000.0,
                "Model-1", PhoneManufacture.SONY);
        target.savePhone(otherPhone);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> target.updatePhone(phone));
    }
}
