package com.service;

import com.model.TV;
import com.model.TVManufacture;
import com.repository.TVRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

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
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.createAndSaveTVs(-1));
    }

    @Test
    void createAndSaveTVs_zeroCount() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.createAndSaveTVs(0));
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
    void deleteTV_ArgumentMatcher() {
        TVRepository tvRepository = new TVRepository();
        TVService tvService = new TVService(tvRepository);
        tvService.createAndSaveTVs(2);
        tvService.deleteTV(tvService.useTVWithIndex(1));
        final List<TV> actualResult = tvRepository.getAll();
        Assertions.assertEquals(1, actualResult.size());
    }

    @Test
    void updateTV_throwing() {
        TVService tvService = Mockito.mock(TVService.class);
        TV tv = new TV("Title", 300, 700, "Model", TVManufacture.SAMSUNG, 32);
        Mockito.doThrow(new IllegalStateException("The tv is missing in the database"))
                .when(tvService).updateTV(tv);
    }

    @Test
    void updateTV_ArgumentCaptor() {
        TVRepository tvRepository = new TVRepository();
        TVService tvService = new TVService(tvRepository);
        ArgumentCaptor<TV> tvArgumentCaptor = ArgumentCaptor.forClass(TV.class);
        tvService.createAndSaveTVs(1);
        tvService = Mockito.mock(TVService.class);
        tvService.updateTV(tvRepository.getTVByIndex(0));
        Mockito.verify(tvService).updateTV(tvArgumentCaptor.capture());
        Assertions.assertEquals(tvRepository.getAll(), tvArgumentCaptor.getAllValues());
    }

}