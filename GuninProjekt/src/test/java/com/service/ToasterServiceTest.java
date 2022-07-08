package com.service;

import com.model.Toaster;
import com.model.ToasterManufacture;
import com.repository.ToasterRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

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
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.createAndSaveToasters(-1));
    }

    @Test
    void createAndSaveToasters_zeroCount() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.createAndSaveToasters(0));
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
    void deleteToaster_ArgumentMatcher() {
        ToasterRepository toasterRepository = new ToasterRepository();
        ToasterService toasterService = new ToasterService(toasterRepository);
        toasterService.createAndSaveToasters(2);
        toasterService.deleteToaster(toasterService.useToasterWithIndex(1));
        final List<Toaster> actualResult = toasterRepository.getAll();
        Assertions.assertEquals(1, actualResult.size());
    }

    @Test
    void updateToaster_throwing() {
        ToasterService toasterService = Mockito.mock(ToasterService.class);
        Toaster toaster = new Toaster("Title", 300, 700, "Model", 2000, ToasterManufacture.PHILIPS);
        Mockito.doThrow(new IllegalStateException("The phone is missing in the database"))
                .when(toasterService).updateToaster(toaster);
    }

    @Test
    void updateToaster_ArgumentCaptor() {
        ToasterRepository toasterRepository = new ToasterRepository();
        ToasterService toasterService = new ToasterService(toasterRepository);
        ArgumentCaptor<Toaster> toasterArgumentCaptor = ArgumentCaptor.forClass(Toaster.class);
        toasterService.createAndSaveToasters(1);
        toasterService = Mockito.mock(ToasterService.class);
        toasterService.updateToaster(toasterRepository.getToasterByIndex(0));
            Mockito.verify(toasterService).updateToaster(toasterArgumentCaptor.capture());
        Assertions.assertEquals(toasterRepository.getAll(), toasterArgumentCaptor.getAllValues());
    }

}