package com.vkiz.voting.web.controller;

import com.vkiz.voting.model.Vote;
import com.vkiz.voting.service.VoteService;
import com.vkiz.voting.to.VoteTo;
import com.vkiz.voting.util.exception.IllegalRequestDataException;
import com.vkiz.voting.util.exception.NotFoundException;
import com.vkiz.voting.web.AbstractControllerTest;
import com.vkiz.voting.web.json.JsonUtil;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static com.vkiz.voting.RestaurantTestData.RESTAURANT_1_ID;
import static com.vkiz.voting.RestaurantTestData.RESTAURANT_2_ID;
import static com.vkiz.voting.TestUtil.readFromJson;
import static com.vkiz.voting.TestUtil.userHttpBasic;
import static com.vkiz.voting.UserTestData.ADMIN;
import static com.vkiz.voting.UserTestData.ADMIN_ID;
import static com.vkiz.voting.UserTestData.USER;
import static com.vkiz.voting.UserTestData.USER_ID;
import static com.vkiz.voting.VoteTestData.USER_VOTES;
import static com.vkiz.voting.VoteTestData.VOTE_1_ID;
import static com.vkiz.voting.VoteTestData.VOTE_1_USER_RESTAURANT_1;
import static com.vkiz.voting.VoteTestData.VOTE_5_NOW_ADMIN_RESTAURANT_1;
import static com.vkiz.voting.VoteTestData.assertMatch;
import static com.vkiz.voting.VoteTestData.contentJson;
import static com.vkiz.voting.VoteTestData.contentJsonWithRestaurant;
import static com.vkiz.voting.util.ToUtils.createFromTo;
import static com.vkiz.voting.util.ValidationUtil.isVoteChangeTimeExpired;
import static com.vkiz.voting.web.controller.VoteRestController.REST_URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteRestControllerTest extends AbstractControllerTest {

    @Autowired
    VoteService voteService;

    @Test
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJsonWithRestaurant(USER_VOTES));
    }

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "/" + VOTE_1_ID)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(VOTE_1_USER_RESTAURANT_1));
    }

    @Test
    void createWithLocation() throws Exception {
        VoteTo newVoteTo = new VoteTo(null, LocalDate.now(), RESTAURANT_1_ID);
        Vote newVote = createFromTo(newVoteTo);
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newVoteTo)))
                .andDo(print())
                .andExpect(status().isCreated());

        Vote created = readFromJson(action, Vote.class);
        Integer newId = created.getId();
        newVote.setId(newId);
        assertMatch(created, newVote);
        assertMatch(voteService.get(newId, USER_ID), newVote);
    }

    @Test
    void update() throws Exception {
        Integer updatedId = VOTE_5_NOW_ADMIN_RESTAURANT_1.getId();
        VoteTo updatedTo = new VoteTo(updatedId, LocalDate.now(), RESTAURANT_2_ID);
        if (!isVoteChangeTimeExpired()) {
            Vote updated = createFromTo(updatedTo);
            updated.setId(updatedId);
            mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + "/" + updatedId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(userHttpBasic(ADMIN))
                    .content(JsonUtil.writeValue(updatedTo)))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            Vote actual = voteService.get(updatedId, ADMIN_ID);
            assertEquals(VOTE_5_NOW_ADMIN_RESTAURANT_1.getDate(), actual.getDate());
            assertMatch(actual, updated);
        } else {
            assertThrows(IllegalRequestDataException.class, () -> voteService.update(updatedTo, ADMIN_ID));
        }
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + "/" + VOTE_1_ID)
                .with(userHttpBasic(USER)))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> voteService.get(VOTE_1_ID, USER_ID));

    }
}
