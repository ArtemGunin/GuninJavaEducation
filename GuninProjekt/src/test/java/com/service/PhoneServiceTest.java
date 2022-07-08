package com.service;

import com.model.Phone;
import com.model.PhoneManufacture;
import com.repository.PhoneRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

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
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.createAndSavePhones(-1));
    }

    @Test
    void createAndSavePhones_zeroCount() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.createAndSavePhones(0));
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
    void countOfPrintAllCalls() {
        target = Mockito.mock(PhoneService.class);
        target.getAll();
        target.printAll();
        target.printAll();
        target.printAll();
        Mockito.verify(target, Mockito.times(3)).printAll();
    }

    @Test
    void getAll() {
        target.getAll();
        Mockito.verify(repository).getAll();
    }

    @Test
    void deletePhone_ArgumentMatcher() {
        PhoneRepository phoneRepository = new PhoneRepository();
        PhoneService phoneService = new PhoneService(phoneRepository);
        phoneService.createAndSavePhones(2);
        phoneService.deletePhone(phoneService.usePhoneWithIndex(1));
        final List<Phone> actualResult = phoneRepository.getAll();
        Assertions.assertEquals(1, actualResult.size());
    }

    @Test
    void updatePhone_throwing() {
        PhoneService phoneService = Mockito.mock(PhoneService.class);
        Phone phone = new Phone("Title", 300, 700, "Model", PhoneManufacture.SAMSUNG);
        Mockito.doThrow(new IllegalStateException("The phone is missing in the database"))
                .when(phoneService).updatePhone(phone);
    }

    @Test
    void updatePhone_ArgumentCaptor() {
        PhoneRepository phoneRepository = new PhoneRepository();
        PhoneService phoneService = new PhoneService(phoneRepository);
        ArgumentCaptor<Phone> phoneArgumentCaptor = ArgumentCaptor.forClass(Phone.class);
        phoneService.createAndSavePhones(1);
        phoneService = Mockito.mock(PhoneService.class);
        phoneService.updatePhone(phoneRepository.getPhoneByIndex(0));
        Mockito.verify(phoneService).updatePhone(phoneArgumentCaptor.capture());
        Assertions.assertEquals(phoneRepository.getAll(), phoneArgumentCaptor.getAllValues());
    }
}