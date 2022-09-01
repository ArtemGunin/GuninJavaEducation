package com.repository;

import com.model.product.Manufacturer;
import com.model.product.TV;
import com.repository.simple.TVRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

class TVRepositoryTest {

    private static TVRepository target;

    private static TV tv;

    @BeforeAll
    static void beforeAll() {
        final Random random = new Random();
        target = TVRepository.getInstance();
        tv = new TV(
                "Title-" + random.nextInt(1000),
                random.nextInt(500),
                random.nextDouble() * 1000,
                "Model-" + random.nextInt(10),
                Manufacturer.HISENSE,
                14 + random.nextInt(52)
        );
    }

    @BeforeEach
    void setUp() {
        target.getAll().clear();
    }

    @Test
    void save() {
        target.save(tv);
        final List<TV> tvs = target.getAll();
        Assertions.assertEquals(1, tvs.size());
        Assertions.assertEquals(tvs.get(0).getId(), tv.getId());
    }

    @Test
    void save_putNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.save(null));
        final List<TV> actualResult = target.getAll();
        Assertions.assertEquals(0, actualResult.size());
    }

    @Test
    void saveAll_singleTV() {
        target.saveAll(Collections.singletonList(tv));
        final List<TV> tvs = target.getAll();
        Assertions.assertEquals(1, tvs.size());
        Assertions.assertEquals(tvs.get(0).getId(), tv.getId());
    }

    @Test
    void saveAll_noTV() {
        target.saveAll(Collections.emptyList());
        final List<TV> tvs = target.getAll();
        Assertions.assertEquals(0, tvs.size());
    }

    @Test
    void saveAll_manyTVs() {
        final TV otherTV = new TV("Title", 300, 700.0,
                "Model", Manufacturer.HISENSE, 40);
        target.saveAll(List.of(tv, otherTV));
        final List<TV> tvs = target.getAll();
        Assertions.assertEquals(2, tvs.size());
        Assertions.assertEquals(tvs.get(0).getId(), tv.getId());
        Assertions.assertEquals(tvs.get(1).getId(), otherTV.getId());
    }

    @Test
    void saveAll_hasNull() {
        final List<TV> tvs = new ArrayList<>();
        tvs.add(tv);
        tvs.add(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.saveAll(tvs));
        final List<TV> actualResult = target.getAll();
        Assertions.assertEquals(1, actualResult.size());
    }

    @Test
    void update() {
        final String newTitle = "new title";
        target.save(tv);
        tv.setTitle(newTitle);

        final boolean result = target.update(tv);

        Assertions.assertTrue(result);
        final List<TV> actualResult = target.getAll();
        Assertions.assertEquals(1, actualResult.size());
        Assertions.assertEquals(newTitle, actualResult.get(0).getTitle());
        Assertions.assertEquals(tv.getId(), actualResult.get(0).getId());
        Assertions.assertEquals(tv.getCount(), actualResult.get(0).getCount());
    }

    @Test
    void update_noTV() {
        target.save(tv);
        final TV noTV = new TV("Title", 200, 1500,
                "Model", Manufacturer.HISENSE, 32);
        final boolean result = target.update(noTV);

        Assertions.assertFalse(result);
        final List<TV> actualResult = target.getAll();
        Assertions.assertEquals(1, actualResult.size());
        Assertions.assertEquals(tv.getId(), actualResult.get(0).getId());
        Assertions.assertEquals(tv.getCount(), actualResult.get(0).getCount());
    }

    @Test
    void delete() {
        target.save(tv);
        final boolean result = target.delete(tv.getId());
        Assertions.assertTrue(result);
        final List<TV> actualResult = target.getAll();
        Assertions.assertEquals(0, actualResult.size());
    }

    @Test
    void delete_noTV() {
        target.save(tv);
        final TV noTV = new TV("Title", 200, 1500,
                "Model", Manufacturer.HISENSE, 32);
        final boolean result = target.delete(noTV.getId());
        Assertions.assertFalse(result);
        final List<TV> actualResult = target.getAll();
        Assertions.assertEquals(1, actualResult.size());
    }

    @Test
    void getAll() {
        target.save(tv);
        final List<TV> actualResult = target.getAll();
        Assertions.assertEquals(1, actualResult.size());
    }

    @Test
    void getAll_noTV() {
        final List<TV> actualResult = target.getAll();
        Assertions.assertEquals(0, actualResult.size());
    }

    @Test
    void findById() {
        target.save(tv);
        final Optional<TV> optionalPhone = target.getById(tv.getId());
        Assertions.assertTrue(optionalPhone.isPresent());
        final TV actualTV = optionalPhone.get();
        Assertions.assertEquals(tv.getId(), actualTV.getId());
    }

    @Test
    void getTVByIndex() {
        target.save(tv);
        final Optional<TV> result = target.getByIndex(0);
        Assertions.assertEquals(Optional.ofNullable(tv), result);
    }

    @Test
    void getTVByIndex_biggerIndex() {
        target.saveAll(Collections.singletonList(tv));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> target.getByIndex(1));
    }

    @Test
    void getPhoneByIndex_negativeIndex() {
        target.saveAll(Collections.singletonList(tv));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> target.getByIndex(-1));
    }

    @Test
    void hasTV() {
        target.save(tv);
        Assertions.assertTrue(target.hasProduct(tv.getId()));
    }

    @Test
    void hasTV_Negative() {
        TV otherTV = new TV("Title-2", 200, 1000.0, "Model-2", Manufacturer.SAMSUNG, 51);
        target.save(tv);
        Assertions.assertFalse(target.hasProduct(otherTV.getId()));
    }
}
