package com.repository;

import com.model.Phone;
import com.model.PhoneManufacture;
import com.model.Toaster;
import com.model.ToasterManufacture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

class ToasterRepositoryTest {

    private ToasterRepository target;

    private Toaster toaster;

    @BeforeEach
    void setUp() {
        final Random random = new Random();
        target = new ToasterRepository();
        toaster = new Toaster(
                "Title-" + random.nextInt(1000),
                random.nextInt(500),
                random.nextDouble() * 1000,
                "Model-" + random.nextInt(10),
                1000 + random.nextInt(2000),
                ToasterManufacture.PHILIPS
        );
    }

    @Test
    void save() {
        target.save(toaster);
        final List<Toaster> toasters = target.getAll();
        Assertions.assertEquals(1, toasters.size());
        Assertions.assertEquals(toasters.get(0).getId(), toaster.getId());
    }

    @Test
    void save_putNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.save(null));
        final List<Toaster> actualResult = target.getAll();
        Assertions.assertEquals(0, actualResult.size());
    }

    @Test
    void saveAll_singleToaster() {
        target.saveAll(Collections.singletonList(toaster));
        final List<Toaster> toasters = target.getAll();
        Assertions.assertEquals(1, toasters.size());
        Assertions.assertEquals(toasters.get(0).getId(), toaster.getId());
    }

    @Test
    void saveAll_noToaster() {
        target.saveAll(Collections.emptyList());
        final List<Toaster> toasters = target.getAll();
        Assertions.assertEquals(0, toasters.size());
    }

    @Test
    void saveAll_manyToasters() {
        final Toaster otherToaster = new Toaster("Title", 300, 700.0,
                "Model", 2000, ToasterManufacture.PHILIPS);
        target.saveAll(List.of(toaster, otherToaster));
        final List<Toaster> toasters = target.getAll();
        Assertions.assertEquals(2, toasters.size());
        Assertions.assertEquals(toasters.get(0).getId(), toaster.getId());
        Assertions.assertEquals(toasters.get(1).getId(), otherToaster.getId());
    }

    @Test
    void saveAll_hasNull() {
        final List<Toaster> toasters = new ArrayList<>();
        toasters.add(toaster);
        toasters.add(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.saveAll(toasters));
        final List<Toaster> actualResult = target.getAll();
        Assertions.assertEquals(1, actualResult.size());
    }

    @Test
    void update() {
        final String newTitle = "new title";
        target.save(toaster);
        toaster.setTitle(newTitle);

        final boolean result = target.update(toaster);

        Assertions.assertTrue(result);
        final List<Toaster> actualResult = target.getAll();
        Assertions.assertEquals(1, actualResult.size());
        Assertions.assertEquals(newTitle, actualResult.get(0).getTitle());
        Assertions.assertEquals(toaster.getId(), actualResult.get(0).getId());
        Assertions.assertEquals(toaster.getCount(), actualResult.get(0).getCount());
    }

    @Test
    void update_noToaster() {
        target.save(toaster);
        final Toaster noToaster = new Toaster("Title", 200, 1500,
                "Model", 2000, ToasterManufacture.PHILIPS);
        final boolean result = target.update(noToaster);

        Assertions.assertFalse(result);
        final List<Toaster> actualResult = target.getAll();
        Assertions.assertEquals(1, actualResult.size());
        Assertions.assertEquals(toaster.getId(), actualResult.get(0).getId());
        Assertions.assertEquals(toaster.getCount(), actualResult.get(0).getCount());
    }

    @Test
    void delete() {
        target.save(toaster);
        final boolean result = target.delete(toaster.getId());
        Assertions.assertTrue(result);
        final List<Toaster> actualResult = target.getAll();
        Assertions.assertEquals(0, actualResult.size());
    }

    @Test
    void delete_noToaster() {
        target.save(toaster);
        final Toaster noToaster = new Toaster("Title", 400, 1200,
                "Model", 2000, ToasterManufacture.PHILIPS);
        final boolean result = target.delete(noToaster.getId());
        Assertions.assertFalse(result);
        final List<Toaster> actualResult = target.getAll();
        Assertions.assertEquals(1, actualResult.size());
    }

    @Test
    void getAll() {
        target.save(toaster);
        final List<Toaster> actualResult = target.getAll();
        Assertions.assertEquals(1, actualResult.size());
    }

    @Test
    void getAll_noToasters() {
        final List<Toaster> actualResult = target.getAll();
        Assertions.assertEquals(0, actualResult.size());
    }

    @Test
    void findById() {
        target.save(toaster);
        final Optional<Toaster> optionalToaster = target.findById(toaster.getId());
        Assertions.assertTrue(optionalToaster.isPresent());
        final Toaster actualToaster = optionalToaster.get();
        Assertions.assertEquals(toaster.getId(), actualToaster.getId());
    }

    @Test
    void getToasterByIndex() {
        target.save(toaster);
        final Toaster result = target.getToasterByIndex(0);
        Assertions.assertEquals(toaster.getId(), result.getId());
    }

    @Test
    void getToasterByIndex_biggerIndex() {
        target.saveAll(Collections.singletonList(toaster));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> target.getToasterByIndex(1));
    }

    @Test
    void getToasterByIndex_negativeIndex() {
        target.saveAll(Collections.singletonList(toaster));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> target.getToasterByIndex(-1));
    }
    @Test
    void hasToaster() {
        target.save(toaster);
        Assertions.assertTrue(target.hasToaster(toaster.getId()));
    }

    @Test
    void hasToaster_Negative() {
        Toaster otherToaster = new Toaster("Title-2", 200, 1000.0,
                "Model-2", 1400, ToasterManufacture.PHILIPS);
        target.save(toaster);
        Assertions.assertFalse(target.hasToaster(otherToaster.getId()));
    }
}
