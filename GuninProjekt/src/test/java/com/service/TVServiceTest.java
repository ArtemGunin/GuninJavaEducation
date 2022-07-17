package com.service;

import com.model.TV;
import com.model.TVManufacture;
import com.model.Toaster;
import com.model.ToasterManufacture;
import com.repository.TVRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.argThat;

class TVServiceTest {

    private TVService target;
    private TVRepository repository;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(TVRepository.class);
        target = new TVService(repository);
    }

    @Test
    void createAndSaveTVs_negativeCount() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> target.createAndSaveTVs(-1));
    }

    @Test
    void createAndSaveTVs_zeroCount() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> target.createAndSaveTVs(0));
    }

    @Test
    void createAndSaveTVs() {
        target.createAndSaveTVs(2);
        Mockito.verify(repository).saveAll(Mockito.anyList());
    }

    @Test
    void printAll() {
        target.printAll();
        Mockito.verify(repository).getAll();
    }

    @Test
    void saveTV() {
        final TV tv = new TV("Title", 100, 1000.0,
                "Model", TVManufacture.HISENSE,1375);
        target.saveTV(tv);

        ArgumentCaptor<TV> argument = ArgumentCaptor.forClass(TV.class);
        Mockito.verify(repository).save(argument.capture());
        Assertions.assertEquals("Title", argument.getValue().getTitle());
    }

    @Test
    void saveTV_zeroCount() {
        final TV tv = new TV("Title", 0, 1000.0,
                "Model", TVManufacture.SONY, 700);
        target.saveTV(tv);

        ArgumentCaptor<TV> argument = ArgumentCaptor.forClass(TV.class);
        Mockito.verify(repository).save(argument.capture());
        Assertions.assertEquals("Title", argument.getValue().getTitle());
        Assertions.assertEquals(-1, argument.getValue().getCount());
    }

    @Test
    void countOfPrintAllCalls() {
        target = Mockito.mock(TVService.class);
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
    void deleteTV_ArgumentMatcher() {
        TV tv = new TV("Title-1", 300, 700.0,
                "Model-1", TVManufacture.SAMSUNG, 900);
        TV otherTV = new TV("Title-2", 200, 1000.0,
                "Model-2", TVManufacture.SONY, 1100);
        target.saveTV(tv);
        target.saveTV(otherTV);
        target.deleteTV(tv);

        ArgumentMatcher<String> hasTVWithId =
                matchesTVId -> matchesTVId.equals(otherTV.getId());
        Mockito.when(repository.hasTV(argThat(hasTVWithId))).thenReturn(true);
        Assertions.assertFalse(repository.hasTV(tv.getId()));
        Assertions.assertTrue(repository.hasTV(otherTV.getId()));
    }

    @Test
    void deleteTV_ArgumentCaptor() {
        TV tv = new TV("Title-1", 300, 700.0,
                "Model-1", TVManufacture.HISENSE, 850);
        TV otherTV = new TV("Title-2", 200, 1000.0,
                "Model-2", TVManufacture.SONY, 1000);
        target.saveTV(tv);
        target.saveTV(otherTV);
        target.deleteTV(tv);

        ArgumentCaptor<TV> tvArgumentCaptor =
                ArgumentCaptor.forClass(TV.class);
        Mockito.when(repository.update(tvArgumentCaptor.capture())).thenReturn(true);
        Assertions.assertFalse(repository.hasTV(tv.getId()));
    }

    @Test
    void updateTV_throwing() {
        TV tv = new TV("Title", 300, 700,
                "Model", TVManufacture.SONY, 975);
        TV otherTV = new TV("Title-1", 200, 1000.0,
                "Model-1", TVManufacture.SAMSUNG, 135);
        target.saveTV(otherTV);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> target.updateTV(tv));
    }
}
