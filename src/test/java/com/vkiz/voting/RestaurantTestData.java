package com.vkiz.voting;

import com.vkiz.voting.model.Dish;
import com.vkiz.voting.model.Restaurant;

import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.vkiz.voting.TestUtil.readFromJsonMvcResult;
import static com.vkiz.voting.TestUtil.readListFromJsonMvcResult;
import static com.vkiz.voting.model.AbstractBaseEntity.START_SEQ;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;

public class RestaurantTestData {

    public static final int RESTAURANT_1_ID = START_SEQ + 2;
    public static final int RESTAURANT_2_ID = START_SEQ + 3;
    public static final int RESTAURANT_3_ID = START_SEQ + 4;

    public static final Restaurant RESTAURANT_1 = new Restaurant(RESTAURANT_1_ID, "McDonalds");
    public static final Restaurant RESTAURANT_2 = new Restaurant(RESTAURANT_2_ID, "KFC");
    public static final Restaurant RESTAURANT_3 = new Restaurant(RESTAURANT_3_ID, "Subway");

    public static final List<Restaurant> RESTAURANTS = List.of(RESTAURANT_1, RESTAURANT_2, RESTAURANT_3);

    public static Restaurant getNew() {
        return new Restaurant(null, "newRestaurant");
    }

    public static Restaurant getUpdated() {
        Restaurant updated = new Restaurant(RESTAURANT_1);
        updated.setName("newRestaurantUpdated");
        return updated;
    }

    public static List<Dish> getDishesOnDate(int restaurantId, LocalDate date) {
        switch (restaurantId) {
            case (RESTAURANT_1_ID):
                return getOnDate(DishTestData.RESTAURANT_1_DISHES, date);
            case (RESTAURANT_2_ID):
                return getOnDate(DishTestData.RESTAURANT_2_DISHES, date);
            case (RESTAURANT_3_ID):
                return getOnDate(DishTestData.RESTAURANT_3_DISHES, date);
        }
        return null;
    }

    public static List<Restaurant> getRestaurantsWithTodayMenu() {
        RESTAURANT_1.setDishes(getDishesOnDate(RESTAURANT_1_ID, now()));
        RESTAURANT_2.setDishes(getDishesOnDate(RESTAURANT_2_ID, now()));
        RESTAURANT_3.setDishes(getDishesOnDate(RESTAURANT_3_ID, now()));
        return List.of(RESTAURANT_1, RESTAURANT_2, RESTAURANT_3);
    }

    public static void assertMatch(Restaurant actual, Restaurant expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "dishes");
    }

    public static void assertMatch(Iterable<Restaurant> actual, Restaurant... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<Restaurant> actual, Iterable<Restaurant> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("dishes").isEqualTo(expected);
    }

    public static void assertMatchWithDishes(Iterable<Restaurant> actual, Iterable<Restaurant> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }

    public static ResultMatcher contentJson(Restaurant... expected) {
        return result -> assertMatch(readListFromJsonMvcResult(result, Restaurant.class), List.of(expected));
    }

    public static ResultMatcher contentJson(Restaurant expected) {
        return result -> assertMatch(readFromJsonMvcResult(result, Restaurant.class), expected);
    }

    public static ResultMatcher contentJson(Iterable<Restaurant> expected) {
        return result -> assertMatch(readListFromJsonMvcResult(result, Restaurant.class), expected);
    }

    public static ResultMatcher contentJsonWithDishes(Iterable<Restaurant> expected) {
        return result -> assertMatchWithDishes(readListFromJsonMvcResult(result, Restaurant.class), expected);
    }

    private static List<Dish> getOnDate(List<Dish> dishes, LocalDate date) {
        return dishes
                .stream()
                .filter(dish -> dish.getDate().isEqual(date))
                .collect(Collectors.toList());
    }
}
