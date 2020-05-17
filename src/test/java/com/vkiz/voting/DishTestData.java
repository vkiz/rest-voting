package com.vkiz.voting;

import com.vkiz.voting.model.Dish;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.vkiz.voting.TestUtil.readFromJsonMvcResult;
import static com.vkiz.voting.TestUtil.readListFromJsonMvcResult;
import static com.vkiz.voting.model.AbstractBaseEntity.START_SEQ;

import static java.time.LocalDate.now;
import static java.time.LocalDate.of;
import static org.assertj.core.api.Assertions.assertThat;

public class DishTestData {
    public static final LocalDate DATE_2020_04_05 = LocalDate.of(2020, 4, 5);
    public static final LocalDate DATE_2020_04_07 = LocalDate.of(2020, 4, 7);

    public static final int DISH_1_ID = START_SEQ + 5;

    public static final Dish DISH01 = new Dish(DISH_1_ID, of(2020, 4, 5), "Hamburger", 150);
    public static final Dish DISH02 = new Dish(DISH_1_ID + 1, of(2020, 4, 5), "Cheeseburger", 150);

    public static final Dish DISH03 = new Dish(DISH_1_ID + 2, of(2020, 4, 5), "Fruit salad", 100);
    public static final Dish DISH04 = new Dish(DISH_1_ID + 3, of(2020, 4, 5), "Salad", 100);

    public static final Dish DISH05 = new Dish(DISH_1_ID + 4, of(2020, 4, 5), "French fries", 100);
    public static final Dish DISH06 = new Dish(DISH_1_ID + 5, of(2020, 4, 5), "Chicken nuggets", 150);

    public static final Dish DISH07 = new Dish(DISH_1_ID + 6, of(2020, 4, 7), "Apple pie", 100);
    public static final Dish DISH08 = new Dish(DISH_1_ID + 7, of(2020, 4, 7), "Cookie", 100);

    public static final Dish DISH09 = new Dish(DISH_1_ID + 8, of(2020, 4, 7), "Croissant", 150);
    public static final Dish DISH10 = new Dish(DISH_1_ID + 9, of(2020, 4, 7), "Hamburger", 160);

    public static final Dish DISH11 = new Dish(DISH_1_ID + 10, of(2020, 4, 7), "Cheeseburger", 160);
    public static final Dish DISH12 = new Dish(DISH_1_ID + 11, of(2020, 4, 7), "Fruit salad", 110);

    public static final Dish DISH13 = new Dish(DISH_1_ID + 12, now(), "Hamburger", 150);
    public static final Dish DISH14 = new Dish(DISH_1_ID + 13, now(), "Cheeseburger", 150);
    public static final Dish DISH15 = new Dish(DISH_1_ID + 14, now(), "Fruit salad", 100);

    public static final Dish DISH16 = new Dish(DISH_1_ID + 15, now(), "Salad", 100);
    public static final Dish DISH17 = new Dish(DISH_1_ID + 16, now(), "French fries", 100);
    public static final Dish DISH18 = new Dish(DISH_1_ID + 17, now(), "Chicken nuggets", 150);

    public static final Dish DISH19 = new Dish(DISH_1_ID + 18, now(), "Apple pie", 100);
    public static final Dish DISH20 = new Dish(DISH_1_ID + 19, now(), "Cookie", 100);
    public static final Dish DISH21 = new Dish(DISH_1_ID + 20, now(), "Croissant", 150);

    public static final List<Dish> RESTAURANT_1_DISHES = List.of(
            DISH14, DISH13, DISH15, DISH08, DISH07, DISH02, DISH01);

    public static final List<Dish> RESTAURANT_2_DISHES = List.of(
            DISH17, DISH16, DISH18, DISH10, DISH09, DISH04, DISH03);

    public static final List<Dish> RESTAURANT_3_DISHES = List.of(
            DISH20, DISH19, DISH21, DISH11, DISH12, DISH05, DISH06);

    public static final List<Dish> RESTAURANT_1_MENU_ON_2020_04_05 = RESTAURANT_1_DISHES
            .stream()
            .filter(dish -> dish.getDate().isEqual(DATE_2020_04_05))
            .collect(Collectors.toList());

    public static final List<Dish> RESTAURANT_1_MENU_BETWEEN_START_DATE_AND_END_DATE = RESTAURANT_1_DISHES
            .stream()
            .filter(dish ->
                    dish.getDate().isAfter(DATE_2020_04_05.minusDays(1)) && dish.getDate().isBefore(DATE_2020_04_07.plusDays(1)))
            .collect(Collectors.toList());

    public static Dish getNew() {
        return new Dish(null, now(), "newDish", 100);
    }

    public static Dish getUpdated() {
        Dish updated = new Dish(DISH01);
        updated.setName("nameUpdated");
        return updated;
    }

    public static void assertMatch(Dish actual, Dish expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Dish> actual, Dish... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<Dish> actual, Iterable<Dish> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("restaurant").isEqualTo(expected);
    }

    public static ResultMatcher contentJson(Dish expected) {
        return result -> assertMatch(readFromJsonMvcResult(result, Dish.class), expected);
    }

    public static ResultMatcher contentJson(Iterable<Dish> expected) {
        return result -> assertMatch(readListFromJsonMvcResult(result, Dish.class), expected);
    }
}
