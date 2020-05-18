package com.vkiz.voting.web.controller;

import com.vkiz.voting.model.Restaurant;
import com.vkiz.voting.service.RestaurantService;
import com.vkiz.voting.util.exception.NotFoundException;
import com.vkiz.voting.web.AbstractControllerTest;
import com.vkiz.voting.web.json.JsonUtil;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.vkiz.voting.RestaurantTestData.RESTAURANT_1_ID;
import static com.vkiz.voting.RestaurantTestData.assertMatch;
import static com.vkiz.voting.RestaurantTestData.getNew;
import static com.vkiz.voting.RestaurantTestData.getUpdated;
import static com.vkiz.voting.TestUtil.readFromJson;
import static com.vkiz.voting.TestUtil.userHttpBasic;
import static com.vkiz.voting.UserTestData.ADMIN;
import static com.vkiz.voting.web.controller.AdminRestaurantRestController.REST_URL;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminRestaurantRestControllerTest extends AbstractControllerTest {
    private static final String REST_TEST_URL = REST_URL;

    @Autowired
    private RestaurantService restaurantService;

    @Test
    void createWithLocation() throws Exception {
        Restaurant newRestaurant = getNew();
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_TEST_URL)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)))
                .andDo(print())
                .andExpect(status().isCreated());

        Restaurant created = readFromJson(action, Restaurant.class);
        Integer newId = created.getId();
        newRestaurant.setId(newId);
        assertMatch(created, newRestaurant);
        assertMatch(restaurantService.get(newId), newRestaurant);
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_TEST_URL + "/" + RESTAURANT_1_ID)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> restaurantService.get(RESTAURANT_1_ID));
    }

    @Test
    void update() throws Exception {
        Restaurant updated = getUpdated();
        mockMvc.perform(MockMvcRequestBuilders.put(REST_TEST_URL + "/" + RESTAURANT_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(restaurantService.get(RESTAURANT_1_ID), updated);
    }
}
