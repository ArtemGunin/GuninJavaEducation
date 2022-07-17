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

import java.util.Optional;

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
        final Phone phone = new Phone("Title-1", 300, 700.0,
                "Model-1", PhoneManufacture.SONY);
        final Phone otherPhone = new Phone("Title-2", 200, 1000.0,
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
        final Phone phone = new Phone("Title-1", 300, 700.0,
                "Model-1", PhoneManufacture.SONY);
        final Phone otherPhone = new Phone("Title-2", 200, 1000.0,
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
        final Phone phone = new Phone("Title", 300, 700,
                "Model", PhoneManufacture.SAMSUNG);
        final Phone otherPhone = new Phone("Title-1", 200, 1000.0,
                "Model-1", PhoneManufacture.SONY);
        target.savePhone(otherPhone);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> target.updatePhone(phone));
    }

    @Test
    void resetPhoneIfPresent() {
        final Phone phone = new Phone("Title", 300, 700,
                "Model", PhoneManufacture.SAMSUNG);
        target.savePhone(phone);
        Mockito.when(repository.findById(phone.getId())).thenReturn(Optional.of(phone));
        target.resetPhoneIfPresent(phone.getId());
        Assertions.assertEquals(0, phone.getCount());
        Assertions.assertEquals(0.0, phone.getPrice());
        Assertions.assertEquals("Custom", phone.getTitle());
    }

    @Test
    void resetPhoneIfPresent_NotPresent() {
        final Phone phone = new Phone("Title", 300, 700,
                "Model", PhoneManufacture.SAMSUNG);
        final Phone equalPhone = new Phone(phone.getId(), "Title", 300, 700,
                "Model", PhoneManufacture.SAMSUNG);
        target.savePhone(phone);
        target.resetPhoneIfPresent("123");
        Mockito.verify(repository).findById("123");
        Assertions.assertEquals(equalPhone.getCount(), phone.getCount());
        Assertions.assertEquals(equalPhone.getPrice(), phone.getPrice());
        Assertions.assertEquals(equalPhone.getTitle(), phone.getTitle());
    }

    @Test
    void phoneIsPresent_True() {
        final Phone phone = new Phone("Title", 300, 700,
                "Model", PhoneManufacture.SAMSUNG);
        target.savePhone(phone);
        Mockito.when(repository.findById(phone.getId())).thenReturn(Optional.of(phone));
        Assertions.assertTrue(target.phoneIsPresent(phone.getId()));
    }

    @Test
    void phoneIsPresent_False() {
        final Phone phone = new Phone("Title", 300, 700,
                "Model", PhoneManufacture.SAMSUNG);
        target.savePhone(phone);
        Mockito.when(repository.findById(phone.getId())).thenReturn(Optional.of(phone));
        Assertions.assertFalse(target.phoneIsPresent("123"));
    }

    @Test
    void getPhoneOrCreate_GetActual() {
        final Phone phone = new Phone("Title", 300, 700,
                "Model", PhoneManufacture.SAMSUNG);
        target.savePhone(phone);
        Mockito.when(repository.findById(phone.getId())).thenReturn(Optional.of(phone));
        Assertions.assertEquals(phone, target.getPhoneOrCreate(phone.getId()));
    }

    @Test
    void getPhoneOrCreate_Create() {
        Mockito.when(repository.findById("123")).thenReturn(Optional.empty());
        Phone createdPhone = target.getPhoneOrCreate("123");
        Phone testPhone = target.createPhoneWithId("123");
        Assertions.assertEquals(createdPhone.getId(), testPhone.getId());
        Assertions.assertEquals(createdPhone.getTitle(), testPhone.getTitle());
        Assertions.assertEquals(createdPhone.getCount(), testPhone.getCount());
        Assertions.assertEquals(createdPhone.getPrice(), testPhone.getPrice());
        Assertions.assertEquals(createdPhone.getModel(), testPhone.getModel());
        Assertions.assertEquals(createdPhone.getPhoneManufacturer(), testPhone.getPhoneManufacturer());
    }

    @Test
    void savePhoneOrCreate_Save() {
        final Phone phone = new Phone("Title", 300, 700,
                "Model", PhoneManufacture.SAMSUNG);
        target.savePhoneOrCreate(phone);
        ArgumentCaptor<Phone> argument = ArgumentCaptor.forClass(Phone.class);
        Mockito.verify(repository).save(argument.capture());
        Assertions.assertEquals(phone.getTitle(), argument.getValue().getTitle());
        Assertions.assertEquals(phone.getPhoneManufacturer(),
                argument.getValue().getPhoneManufacturer());
    }

    @Test
    void savePhoneOrCreate_Create() {
        target.savePhoneOrCreate(null);
        ArgumentCaptor<Phone> argument = ArgumentCaptor.forClass(Phone.class);
        Mockito.verify(repository).save(argument.capture());
        Assertions.assertEquals("Custom", argument.getValue().getTitle());
        Assertions.assertEquals(PhoneManufacture.SONY,
                argument.getValue().getPhoneManufacturer());
    }

    @Test
    void getPhoneWithModifiedId() {
        final Phone phone = new Phone("Title", 300, 700,
                "Model", PhoneManufacture.SAMSUNG);
        target.savePhone(phone);
        Mockito.when(repository.findById(phone.getId())).thenReturn(Optional.of(phone));
        Phone modifiedPhone = target.getPhoneWithModifiedId(phone.getId(), "123");
        Assertions.assertEquals("123", modifiedPhone.getId());
        Assertions.assertEquals("Title", modifiedPhone.getTitle());
        Assertions.assertEquals(700, modifiedPhone.getPrice());

    }

    @Test
    void getPhoneWithModifiedId_throwing() {
        Mockito.when(repository.findById("123")).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> target.getPhoneWithModifiedId("123", "321"));
    }

    @Test
    void updatePhoneOrCreateDefault_Update() {
        final Phone phone = new Phone("Title", 300, 700,
                "Model", PhoneManufacture.SAMSUNG);
        final Phone equalIdPhone = new Phone(phone.getId(), "Title - 1", 100, 1300,
                "Model - 1", PhoneManufacture.APPLE);
        target.savePhone(phone);
        Mockito.when(repository.findById(phone.getId())).thenReturn(Optional.of(phone));
        Mockito.when(repository.update(equalIdPhone)).thenCallRealMethod();
        Mockito.when(repository.hasPhone(phone.getId())).thenReturn(true);
        target.updatePhoneOrCreateDefault(equalIdPhone);
        Assertions.assertEquals(equalIdPhone.getTitle(), phone.getTitle());
        Assertions.assertEquals(equalIdPhone.getCount(), phone.getCount());
        Assertions.assertEquals(equalIdPhone.getPrice(), phone.getPrice());
    }

    @Test
    void updatePhoneOrCreateDefault_IfCreateDefault() {
        final Phone otherPhone = new Phone("Title - 1", 100, 1300,
                "Model - 1", PhoneManufacture.APPLE);
        target.updatePhoneOrCreateDefault(otherPhone);
        ArgumentCaptor<Phone> argument = ArgumentCaptor.forClass(Phone.class);
        Mockito.verify(repository).save(argument.capture());
        Mockito.when(repository.hasPhone(otherPhone.getId())).thenReturn(false);
        Assertions.assertEquals("Custom", argument.getValue().getTitle());
        Assertions.assertEquals(0, argument.getValue().getCount());
        Assertions.assertEquals(0.0, argument.getValue().getPrice());
    }

    @Test
    void updatePhoneOrCreateDefault_CreateDefault() {
        target.updatePhoneOrCreateDefault(null);
        ArgumentCaptor<Phone> argument = ArgumentCaptor.forClass(Phone.class);
        Mockito.verify(repository).save(argument.capture());
        Assertions.assertEquals("Custom", argument.getValue().getTitle());
        Assertions.assertEquals(0, argument.getValue().getCount());
        Assertions.assertEquals(0.0, argument.getValue().getPrice());
    }

    @Test
    void getCoastOfPhones() {
        final Phone phone = new Phone("Title", 300, 700,
                "Model", PhoneManufacture.SAMSUNG);
        Mockito.when(repository.findById(phone.getId())).thenReturn(Optional.of(phone));
        final int expected = (int) (phone.getCount() * phone.getPrice());
        Assertions.assertEquals(expected, target.getCoastOfPhones(phone.getId()));
    }

    @Test
    void getCoastOfPhones_throwing() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () ->  target.getCoastOfPhones("123"));
    }

    @Test
    void phonePriceMiddleBounds_True() {
        final Phone phone = new Phone("Title", 300, 700,
                "Model", PhoneManufacture.SAMSUNG);
        target.savePhone(phone);
        Mockito.when(repository.findById(phone.getId())).thenReturn(Optional.of(phone));
        Assertions.assertTrue(target.phonePriceMiddleBounds(phone.getId(), 500, 1000));
    }

    @Test
    void phonePriceMiddleBounds_False() {
        final Phone phone = new Phone("Title", 300, 700,
                "Model", PhoneManufacture.SAMSUNG);
        target.savePhone(phone);
        Mockito.when(repository.findById(phone.getId())).thenReturn(Optional.of(phone));
        Assertions.assertFalse(target.phonePriceMiddleBounds(phone.getId(), 1500, 2000));
    }

    @Test
    void getPhoneWithIdOrCreatedIfPhoneMiss_PhonePresent() {
        final Phone phone = new Phone("Title", 300, 700,
                "Model", PhoneManufacture.SAMSUNG);
        target.savePhone(phone);
        Mockito.when(repository.findById(phone.getId())).thenReturn(Optional.of(phone));
        Phone actual = target.getPhoneWithIdOrCreatedIfPhoneMiss(phone.getId());
        Assertions.assertEquals(phone, actual);
    }

    @Test
    void getPhoneWithIdOrCreatedIfPhoneMiss_PhoneMiss() {
        Mockito.when(repository.findById(anyString())).thenReturn(Optional.empty());
        Phone actual = target.getPhoneWithIdOrCreatedIfPhoneMiss("123");
        Assertions.assertEquals("123", actual.getId());
        Assertions.assertEquals("Custom", actual.getTitle());
        Assertions.assertEquals(0, actual.getCount());
        Assertions.assertEquals(0.0, actual.getPrice());
        Assertions.assertEquals("Model", actual.getModel());
        Assertions.assertEquals(PhoneManufacture.SONY, actual.getPhoneManufacturer());
    }

    @Test
    void createAndSaveDefaultPhone() {
        Phone actual = target.createDefaultPhone();
        Assertions.assertEquals("Custom", actual.getTitle());
        Assertions.assertEquals(0, actual.getCount());
        Assertions.assertEquals(0.0, actual.getPrice());
        Assertions.assertEquals("Model", actual.getModel());
        Assertions.assertEquals(PhoneManufacture.SONY, actual.getPhoneManufacturer());
    }

    @Test
    void createAndSavePhoneWithId() {
        Phone actual = target.createPhoneWithId("123");
        Assertions.assertEquals("123", actual.getId());
        Assertions.assertEquals("Custom", actual.getTitle());
        Assertions.assertEquals(0, actual.getCount());
        Assertions.assertEquals(0.0, actual.getPrice());
        Assertions.assertEquals("Model", actual.getModel());
        Assertions.assertEquals(PhoneManufacture.SONY, actual.getPhoneManufacturer());
    }
}
