package com.vkiz.voting.service;

import com.vkiz.voting.model.Vote;
import com.vkiz.voting.util.exception.IllegalRequestDataException;
import com.vkiz.voting.util.exception.NotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static com.vkiz.voting.RestaurantTestData.RESTAURANT_1_ID;
import static com.vkiz.voting.RestaurantTestData.RESTAURANT_2;
import static com.vkiz.voting.RestaurantTestData.RESTAURANT_2_ID;
import static com.vkiz.voting.UserTestData.ADMIN_ID;
import static com.vkiz.voting.UserTestData.USER_ID;
import static com.vkiz.voting.VoteTestData.ADMIN_VOTES;
import static com.vkiz.voting.VoteTestData.USER_VOTES;
import static com.vkiz.voting.VoteTestData.VOTE_1_ID;
import static com.vkiz.voting.VoteTestData.VOTE_1_USER_RESTAURANT_1;
import static com.vkiz.voting.VoteTestData.assertMatch;
import static com.vkiz.voting.VoteTestData.getNew;
import static com.vkiz.voting.util.ValidationUtil.isVoteChangeTimeExpired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VoteServiceTest extends AbstractServiceTest {

    @Autowired
    private VoteService service;

    @Test
    void create() {
        Vote newVote = getNew();
        Vote created = service.create(newVote, USER_ID, RESTAURANT_1_ID);
        Integer newId = created.getId();
        newVote.setId(newId);
        assertMatch(created, newVote);
        assertMatch(service.get(newId, USER_ID), newVote);
    }

    @Test
    void delete() {
        service.delete(VOTE_1_ID, USER_ID);
        assertThrows(NotFoundException.class, () ->
                service.get(VOTE_1_ID, USER_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.delete(1, USER_ID));
    }

    @Test
    void deleteNotOwn() {
        assertThrows(NotFoundException.class, () ->
                service.delete(VOTE_1_ID, RESTAURANT_1_ID));
    }

    @Test
    void get() {
        Vote actual = service.get(VOTE_1_ID, USER_ID);
        assertMatch(actual, VOTE_1_USER_RESTAURANT_1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.get(1, USER_ID));
    }

    @Test
    void getNotOwn() {
        assertThrows(NotFoundException.class, () ->
                service.get(VOTE_1_ID, ADMIN_ID));
    }

    @Test
    void getAll() {
        assertMatch(service.getAll(USER_ID), USER_VOTES);
        assertMatch(service.getAll(ADMIN_ID), ADMIN_VOTES);
    }

    @Test
    void update() {
        Vote newVote = getNew();
        Vote created = service.create(newVote, USER_ID, RESTAURANT_1_ID);
        assertMatch(newVote, created);
        Vote updated = new Vote(created);
        updated.setRestaurant(RESTAURANT_2);
        if (isVoteChangeTimeExpired()) {
            IllegalRequestDataException e = assertThrows(IllegalRequestDataException.class, () -> update(updated));
            assertEquals(e.getMessage(), "Vote change time expired at 11:00 AM");
        } else {
            update(updated);
            assertMatch(service.get(created.getId(), USER_ID), updated);
        }
    }

    @Test
    void updateNotFound() {
        if (!isVoteChangeTimeExpired()) {
            Vote updated = getNew();
            updated.setId(1);
            NotFoundException e = assertThrows(NotFoundException.class, () -> update(updated));
            assertEquals(e.getMessage(), "Not found entity with id=" + 1);
        }
    }

    @Test
    void updateBackdate() {
        Vote updated = new Vote(VOTE_1_USER_RESTAURANT_1);
        IllegalRequestDataException e = assertThrows(IllegalRequestDataException.class, () ->
                service.update(updated, ADMIN_ID, RESTAURANT_2_ID));
        assertEquals(e.getMessage(), "Vote{id=" + updated.getId() + "} date=" + updated.getDate() + " must be today: " + LocalDate.now());
    }

    private void update(Vote vote) {
        service.update(vote, USER_ID, RESTAURANT_2_ID);
    }
}
