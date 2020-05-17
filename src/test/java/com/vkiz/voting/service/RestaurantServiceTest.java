package com.vkiz.voting.service;

import com.vkiz.voting.DishTestData;
import com.vkiz.voting.RestaurantTestData;
import com.vkiz.voting.model.Restaurant;
import com.vkiz.voting.util.exception.NotFoundException;

import org.hsqldb.HsqlException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.List;

import static com.vkiz.voting.RestaurantTestData.RESTAURANTS;
import static com.vkiz.voting.RestaurantTestData.RESTAURANT_1;
import static com.vkiz.voting.RestaurantTestData.RESTAURANT_1_ID;
import static com.vkiz.voting.RestaurantTestData.assertMatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RestaurantServiceTest extends AbstractServiceTest {

    @Autowired
    private RestaurantService service;

    @Test
    void create() {
        Restaurant newRestaurant = RestaurantTestData.getNew();
        Restaurant created = service.create(newRestaurant);
        Integer newId = created.getId();
        newRestaurant.setId(newId);
        assertMatch(created, newRestaurant);
        assertMatch(service.get(newId), newRestaurant);
    }

    @Test
    void delete() {
        service.delete(RESTAURANT_1_ID);
        assertThrows(NotFoundException.class, () ->
                service.get(RESTAURANT_1_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.delete(1));
    }

    @Test
    void get() {
        assertMatch(service.get(RESTAURANT_1_ID), RESTAURANT_1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.get(1));
    }

    @Test
    void update() {
        Restaurant updated = RestaurantTestData.getUpdated();
        service.update(updated);
        assertMatch(service.get(RESTAURANT_1_ID), updated);
    }

    @Test
    void updateNotFound() {
        NotFoundException e = assertThrows(NotFoundException.class, () ->
                service.update(new Restaurant(1, "NotExist")));
        assertEquals(e.getMessage(), "Not found entity with id=" + 1);
    }

    @Test
    void createWithException() {
        validateRootCause(() -> service.create(new Restaurant(null, "  ")), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Restaurant(null, "McDonalds")), HsqlException.class);
    }
}
