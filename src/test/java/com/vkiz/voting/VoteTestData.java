package com.vkiz.voting;

import com.vkiz.voting.model.Vote;

import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDate;
import java.util.List;

import static com.vkiz.voting.DishTestData.DATE_2020_04_05;
import static com.vkiz.voting.DishTestData.DATE_2020_04_07;
import static com.vkiz.voting.RestaurantTestData.RESTAURANT_1;
import static com.vkiz.voting.RestaurantTestData.RESTAURANT_2;
import static com.vkiz.voting.RestaurantTestData.RESTAURANT_3;
import static com.vkiz.voting.TestUtil.readFromJsonMvcResult;
import static com.vkiz.voting.TestUtil.readListFromJsonMvcResult;
import static com.vkiz.voting.UserTestData.ADMIN;
import static com.vkiz.voting.UserTestData.USER;
import static com.vkiz.voting.model.AbstractBaseEntity.START_SEQ;

import static org.assertj.core.api.Assertions.assertThat;

public class VoteTestData {

    public static final int VOTE_1_ID = START_SEQ + 26;

    public static final Vote VOTE_1_USER_RESTAURANT_1 = new Vote(VOTE_1_ID, DATE_2020_04_05, USER, RESTAURANT_1);
    public static final Vote VOTE_2_ADMIN_RESTAURANT_1 = new Vote(VOTE_1_ID + 1, DATE_2020_04_05, ADMIN, RESTAURANT_1);
    public static final Vote VOTE_3_USER_RESTAURANT_2 = new Vote(VOTE_1_ID + 2, DATE_2020_04_07, USER, RESTAURANT_2);
    public static final Vote VOTE_4_ADMIN_RESTAURANT_3 = new Vote(VOTE_1_ID + 3, DATE_2020_04_07, ADMIN, RESTAURANT_3);

    public static final Vote VOTE_5_NOW_ADMIN_RESTAURANT_1 = new Vote(VOTE_1_ID + 4, LocalDate.now(), USER, RESTAURANT_1);

    public static final List<Vote> USER_VOTES = List.of(VOTE_3_USER_RESTAURANT_2, VOTE_1_USER_RESTAURANT_1);
    public static final List<Vote> ADMIN_VOTES = List.of(
            VOTE_5_NOW_ADMIN_RESTAURANT_1, VOTE_4_ADMIN_RESTAURANT_3, VOTE_2_ADMIN_RESTAURANT_1);

    public static Vote getNew() {
        return new Vote(null, LocalDate.now(), USER, RESTAURANT_1);
    }

    public static void assertMatch(Vote actual, Vote expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "user", "restaurant");
    }

    public static void assertMatch(Iterable<Vote> actual, Vote... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<Vote> actual, Iterable<Vote> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("user", "restaurant").isEqualTo(expected);
    }

    public static void assertMatchWithRestaurant(Iterable<Vote> actual, Iterable<Vote> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("user").isEqualTo(expected);
    }

    public static ResultMatcher contentJsonWithRestaurant(Iterable<Vote> expected) {
        return result -> assertMatchWithRestaurant(readListFromJsonMvcResult(result, Vote.class), expected);
    }

    public static ResultMatcher contentJson(Vote expected) {
        return result -> assertMatch(readFromJsonMvcResult(result, Vote.class), expected);
    }
}
