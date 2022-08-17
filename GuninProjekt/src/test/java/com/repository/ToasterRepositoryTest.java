package com.repository;

import com.model.product.Manufacturer;
import com.model.product.Toaster;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

class ToasterRepositoryTest {

    private static ToasterRepository target;

    private static Toaster toaster;

    @BeforeAll
    static void beforeAll() {
        final Random random = new Random();
        target = ToasterRepository.getInstance();
        toaster = new Toaster.ToasterBuilder()
                .setTitle("Title-" + random.nextInt(1000))
                .setCount(random.nextInt(500))
                .setPrice(random.nextDouble() * 1000)
                .setModel("Model-" + random.nextInt(10))
                .setPower(1000 + random.nextInt(2000))
                .setManufacturer(Manufacturer.PHILIPS)
                .build();
    }

    @BeforeEach
    void setUp() {
        target.getAll().clear();
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
        final Toaster otherToaster = new Toaster.ToasterBuilder()
                .setTitle("Title")
                .setCount(300)
                .setPrice(700.0)
                .setModel("Model")
                .setPower(2000)
                .setManufacturer(Manufacturer.PHILIPS)
                .build();
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
        final Toaster noToaster = new Toaster.ToasterBuilder()
                .setTitle("Title")
                .setCount(300)
                .setPrice(700.0)
                .setModel("Model")
                .setPower(2000)
                .setManufacturer(Manufacturer.PHILIPS)
                .build();
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
        final Toaster noToaster = new Toaster.ToasterBuilder()
                .setTitle("Title")
                .setCount(300)
                .setPrice(700.0)
                .setModel("Model")
                .setPower(2000)
                .setManufacturer(Manufacturer.PHILIPS)
                .build();
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
        final Optional<Toaster> result = target.getByIndex(0);
        Assertions.assertEquals(Optional.of(toaster), result);
    }

    @Test
    void getToasterByIndex_biggerIndex() {
        target.saveAll(Collections.singletonList(toaster));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> target.getByIndex(1));
    }

    @Test
    void getToasterByIndex_negativeIndex() {
        target.saveAll(Collections.singletonList(toaster));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> target.getByIndex(-1));
    }

    @Test
    void hasToaster() {
        target.save(toaster);
        Assertions.assertTrue(target.hasProduct(toaster.getId()));
    }

    @Test
    void hasToaster_Negative() {
        Toaster otherToaster = new Toaster.ToasterBuilder()
                .setTitle("Title")
                .setCount(300)
                .setPrice(700.0)
                .setModel("Model")
                .setPower(2000)
                .setManufacturer(Manufacturer.PHILIPS)
                .build();
        target.save(toaster);
        Assertions.assertFalse(target.hasProduct(otherToaster.getId()));
    }
}
