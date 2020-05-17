package com.vkiz.voting.service;

import com.vkiz.voting.UserTestData;
import com.vkiz.voting.model.Role;
import com.vkiz.voting.model.User;
import com.vkiz.voting.util.exception.NotFoundException;

import org.hsqldb.HsqlException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static com.vkiz.voting.UserTestData.ADMIN;
import static com.vkiz.voting.UserTestData.ADMIN_ID;
import static com.vkiz.voting.UserTestData.USER;
import static com.vkiz.voting.UserTestData.USER_ID;
import static com.vkiz.voting.UserTestData.assertMatch;
import static com.vkiz.voting.UserTestData.getNew;
import static com.vkiz.voting.UserTestData.getUpdated;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceTest extends AbstractServiceTest {

    @Autowired
    private UserService service;

    @Test
    void create() {
        User newUser = UserTestData.getNew();
        User created = service.create(newUser);
        Integer newId = created.getId();
        newUser.setId(newId);
        assertMatch(created, newUser);
        final User actual = service.get(newId);
        assertMatch(actual, newUser);
    }

    @Test
    void createWithEmptyRoles() {
        User newUser = UserTestData.getNewWithEmptyRoles();
        User created = service.create(newUser);
        Integer newId = created.getId();
        newUser.setId(newId);
        assertMatch(created, newUser);
        assertMatch(service.get(newId), newUser);
    }

    @Test
    void createWithTwoRoles() {
        User newUser = new User(ADMIN);
        User created = service.create(newUser);
        Integer newId = created.getId();
        newUser.setId(newId);
        assertMatch(created, newUser);
        assertMatch(service.get(newId), newUser);
    }

    @Test
    void delete() {
        service.delete(USER_ID);
        assertThrows(NotFoundException.class, () ->
                service.delete(USER_ID));
    }

    @Test
    void deletedNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.delete(1));
    }

    @Test
    void get() {
        User user = service.get(ADMIN_ID);
        assertMatch(user, ADMIN);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.get(1));
    }

    @Test
    void getByEmail() {
        User user = service.getByEmail("user@gmail.com");
        assertMatch(user, USER);
    }

    @Test
    void getAll() {
        List<User> all = service.getAll();
        assertMatch(all, ADMIN, USER);
    }

    @Test
    void update() {
        User updated = getUpdated();
        service.update(new User(updated));
        assertMatch(service.get(USER_ID), updated);
    }

    @Test
    void updateNotFound() {
        User notExist = getNew();
        notExist.setId(1);
        NotFoundException e = assertThrows(NotFoundException.class, () ->
                service.update(notExist));
        assertEquals(e.getMessage(), "Not found entity with id=" + 1);
    }

    @Test
    void createWithException() {
        validateRootCause(() -> service.create(new User(null, "  ", "password", Role.ROLE_USER)), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "user@gmail.com", " ", Role.ROLE_USER)), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "user@gmail.com", "user", Role.ROLE_USER)), HsqlException.class);
    }
}
