package com.vkiz.voting.service;

import com.vkiz.voting.DishTestData;
import com.vkiz.voting.model.Dish;
import com.vkiz.voting.util.exception.NotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.List;

import static com.vkiz.voting.DishTestData.DATE_2020_04_05;
import static com.vkiz.voting.DishTestData.DATE_2020_04_07;
import static com.vkiz.voting.DishTestData.DISH01;
import static com.vkiz.voting.DishTestData.DISH_1_ID;
import static com.vkiz.voting.DishTestData.RESTAURANT_1_DISHES;
import static com.vkiz.voting.DishTestData.RESTAURANT_1_MENU_BETWEEN_START_DATE_AND_END_DATE;
import static com.vkiz.voting.DishTestData.RESTAURANT_1_MENU_ON_2020_04_05;
import static com.vkiz.voting.DishTestData.RESTAURANT_2_DISHES;
import static com.vkiz.voting.DishTestData.RESTAURANT_3_DISHES;
import static com.vkiz.voting.DishTestData.assertMatch;
import static com.vkiz.voting.RestaurantTestData.RESTAURANT_1_ID;
import static com.vkiz.voting.RestaurantTestData.RESTAURANT_2_ID;
import static com.vkiz.voting.RestaurantTestData.RESTAURANT_3_ID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DishServiceTest extends AbstractServiceTest {

    @Autowired
    private DishService service;

    @Test
    void create() {
        Dish newDish = DishTestData.getNew();
        Dish created = service.create(newDish, RESTAURANT_1_ID);
        Integer newId = created.getId();
        newDish.setId(newId);
        assertMatch(created, newDish);
        assertMatch(service.get(newId, RESTAURANT_1_ID), newDish);
    }

    @Test
    void delete() {
        service.delete(DISH_1_ID, RESTAURANT_1_ID);
        assertThrows(NotFoundException.class, () ->
                service.get(DISH_1_ID, RESTAURANT_1_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.delete(1, RESTAURANT_1_ID));
    }

    @Test
    void deleteNotOwn() {
        assertThrows(NotFoundException.class, () ->
                service.delete(DISH_1_ID, RESTAURANT_2_ID));
    }

    @Test
    void get() {
        assertMatch(service.get(DISH_1_ID, RESTAURANT_1_ID), DISH01);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.get(1, RESTAURANT_1_ID));
    }

    @Test
    void getNotOwn() {
        assertThrows(NotFoundException.class, () ->
                service.get(DISH_1_ID, RESTAURANT_2_ID));
    }

    @Test
    void getMenuOnDate() {
        final List<Dish> menuOnDate = service.getMenuOnDate(
                LocalDate.of(2020, 4, 5), RESTAURANT_1_ID);
        assertMatch(menuOnDate, RESTAURANT_1_MENU_ON_2020_04_05);
    }

    @Test
    void update() {
        Dish updated = DishTestData.getUpdated();
        service.update(updated, RESTAURANT_1_ID);
        assertMatch(service.get(DISH_1_ID, RESTAURANT_1_ID), updated);
    }

    @Test
    void updateNotFound() {
        Dish updated = DishTestData.getUpdated();
        updated.setId(1);
        NotFoundException e = assertThrows(NotFoundException.class, () -> service.update(updated, RESTAURANT_1_ID));
        assertEquals(e.getMessage(), "Not found entity with id=" + 1);
    }

    @Test
    void updateNotOwn() {
        NotFoundException e = assertThrows(NotFoundException.class, () -> service.update(new Dish(DISH01), RESTAURANT_2_ID));
        assertEquals(e.getMessage(), "Not found entity with id=" + DISH_1_ID);
    }

    @Test
    void createWithException() {
        validateRootCause(() -> service.create(
                new Dish(null, null, "dishName", 100), RESTAURANT_1_ID),
                ConstraintViolationException.class);
        validateRootCause(() -> service.create(
                new Dish(null, LocalDate.now(), "  ", 100), RESTAURANT_1_ID),
                ConstraintViolationException.class);
        validateRootCause(() -> service.create(
                new Dish(null, LocalDate.now(), "o", 100), RESTAURANT_1_ID),
                ConstraintViolationException.class);
        validateRootCause(() -> service.create(
                new Dish(null, LocalDate.now(), "dishName", 0), RESTAURANT_1_ID),
                ConstraintViolationException.class);
    }
}
