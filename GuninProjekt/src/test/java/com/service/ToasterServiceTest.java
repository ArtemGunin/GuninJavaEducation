package com.service;

import com.model.Phone;
import com.model.PhoneManufacture;
import com.model.Toaster;
import com.model.ToasterManufacture;
import com.repository.ToasterRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.argThat;

class ToasterServiceTest {

    private ToasterService target;
    private ToasterRepository repository;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(ToasterRepository.class);
        target = new ToasterService(repository);
    }

    @Test
    void createAndSaveToasters_negativeCount() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> target.createAndSaveToasters(-1));
    }

    @Test
    void createAndSaveToasters_zeroCount() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> target.createAndSaveToasters(0));
    }

    @Test
    void createAndSaveToasters() {
        target.createAndSaveToasters(2);
        Mockito.verify(repository).saveAll(Mockito.anyList());
    }

    @Test
    void printAll() {
        target.printAll();
        Mockito.verify(repository).getAll();
    }

    @Test
    void saveToaster() {
        final Toaster toaster = new Toaster("Title", 100, 1000.0,
                "Model",1375, ToasterManufacture.GORENJE);
        target.saveToaster(toaster);

        ArgumentCaptor<Toaster> argument = ArgumentCaptor.forClass(Toaster.class);
        Mockito.verify(repository).save(argument.capture());
        Assertions.assertEquals("Title", argument.getValue().getTitle());
    }

    @Test
    void saveToaster_zeroCount() {
        final Toaster toaster = new Toaster("Title", 0, 1000.0,
                "Model", 700, ToasterManufacture.PHILIPS);
        target.saveToaster(toaster);

        ArgumentCaptor<Toaster> argument = ArgumentCaptor.forClass(Toaster.class);
        Mockito.verify(repository).save(argument.capture());
        Assertions.assertEquals("Title", argument.getValue().getTitle());
        Assertions.assertEquals(-1, argument.getValue().getCount());
    }

    @Test
    void countOfPrintAllCalls() {
        target = Mockito.mock(ToasterService.class);
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
    void getAll_CallReaMethod() {
        target.getAll();
        Mockito.doCallRealMethod().when(repository).getAll();
        Mockito.verify(repository).getAll();
    }

    @Test
    void deleteToaster_ArgumentMatcher() {
        Toaster toaster = new Toaster("Title-1", 300, 700.0,
                "Model-1", 900, ToasterManufacture.PHILIPS);
        Toaster otherToaster = new Toaster("Title-2", 200, 1000.0,
                "Model-2", 1100, ToasterManufacture.TEFAL);
        target.saveToaster(toaster);
        target.saveToaster(otherToaster);
        target.deleteToaster(toaster);

        ArgumentMatcher<String> hasToasterWithId =
                matchesToasterId -> matchesToasterId.equals(otherToaster.getId());
        Mockito.when(repository.hasToaster(argThat(hasToasterWithId))).thenReturn(true);
        Assertions.assertFalse(repository.hasToaster(toaster.getId()));
        Assertions.assertTrue(repository.hasToaster(otherToaster.getId()));
    }

    @Test
    void deleteToaster_ArgumentCaptor() {
        Toaster toaster = new Toaster("Title-1", 300, 700.0,
                "Model-1", 850, ToasterManufacture.VITEK);
        Toaster otherToaster = new Toaster("Title-2", 200, 1000.0,
                "Model-2", 1000, ToasterManufacture.PHILIPS);
        target.saveToaster(toaster);
        target.saveToaster(otherToaster);
        target.deleteToaster(toaster);

        ArgumentCaptor<Toaster> toasterArgumentCaptor =
                ArgumentCaptor.forClass(Toaster.class);
        Mockito.when(repository.update(toasterArgumentCaptor.capture())).thenReturn(true);
        Assertions.assertFalse(repository.hasToaster(toaster.getId()));
    }

    @Test
    void updateToaster_throwing() {
        Toaster toaster = new Toaster("Title", 300, 700,
                "Model", 975, ToasterManufacture.VITEK);
        Toaster otherToaster = new Toaster("Title-1", 200, 1000.0,
                "Model-1", 135, ToasterManufacture.TEFAL);
        target.saveToaster(otherToaster);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> target.updateToaster(toaster));
    }
}
