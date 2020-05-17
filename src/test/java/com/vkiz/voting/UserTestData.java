package com.vkiz.voting;

import com.vkiz.voting.model.Role;
import com.vkiz.voting.model.User;

import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Collections;
import java.util.List;

import static com.vkiz.voting.TestUtil.readFromJsonMvcResult;
import static com.vkiz.voting.TestUtil.readListFromJsonMvcResult;
import static com.vkiz.voting.model.AbstractBaseEntity.START_SEQ;
import static org.assertj.core.api.Assertions.assertThat;

public class UserTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;

    public static final User USER = new User(
            USER_ID, "user@gmail.com", "user", Role.ROLE_USER);

    public static final User ADMIN = new User(
            ADMIN_ID, "admin@gmail.com", "admin", Role.ROLE_ADMIN, Role.ROLE_USER);

    public static User getNew() {
        return new User(
                null, "new@gmail.com", "new", Collections.singleton(Role.ROLE_USER));
    }

    public static User getNewWithEmptyRoles() {
        return new User(
                null, "empty@gmail.com", "empty", Collections.emptySet());
    }

    public static User getUpdated() {
        User updated = new User(USER);
        updated.setEmail("newEmail@gmail.com");
        return updated;
    }

    public static void assertMatch(User actual, User expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static void assertMatch(Iterable<User> actual, User... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<User> actual, Iterable<User> expected) {
        assertThat(actual).usingDefaultElementComparator().isEqualTo(expected);
    }

    public static ResultMatcher contentJson(User... expected) {
        return result -> assertMatch(readListFromJsonMvcResult(result, User.class), List.of(expected));
    }

    public static ResultMatcher contentJson(User expected) {
        return result -> assertMatch(readFromJsonMvcResult(result, User.class), expected);
    }
}
