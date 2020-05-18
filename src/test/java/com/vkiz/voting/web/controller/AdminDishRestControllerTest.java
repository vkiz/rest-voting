package com.vkiz.voting.web.controller;

import com.vkiz.voting.model.Dish;
import com.vkiz.voting.service.DishService;
import com.vkiz.voting.to.DishTo;
import com.vkiz.voting.util.exception.NotFoundException;
import com.vkiz.voting.web.AbstractControllerTest;
import com.vkiz.voting.web.json.JsonUtil;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.vkiz.voting.DishTestData.DISH13;
import static com.vkiz.voting.DishTestData.DISH_1_ID;
import static com.vkiz.voting.DishTestData.assertMatch;
import static com.vkiz.voting.RestaurantTestData.RESTAURANT_1_ID;
import static com.vkiz.voting.TestUtil.readFromJson;
import static com.vkiz.voting.TestUtil.userHttpBasic;
import static com.vkiz.voting.UserTestData.ADMIN;
import static com.vkiz.voting.util.ToUtils.createFromTo;
import static com.vkiz.voting.web.controller.AdminDishRestController.REST_URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminDishRestControllerTest extends AbstractControllerTest {
    private static final String REST_TEST_URL = REST_URL + "/" + RESTAURANT_1_ID + "/dishes";

    @Autowired
    private DishService dishService;

    @Test
    void createWithLocation() throws Exception {
        DishTo newDishTo = new DishTo(null, null, "newDish", 100);
        Dish newDish = createFromTo(newDishTo);
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_TEST_URL)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDishTo)))
                .andDo(print())
                .andExpect(status().isCreated());

        Dish created = readFromJson(action, Dish.class);
        Integer newId = created.getId();
        newDish.setId(newId);
        assertMatch(created, newDish);
        assertMatch(dishService.get(newId, RESTAURANT_1_ID), newDish);
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_TEST_URL + "/" + DISH_1_ID)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> dishService.get(DISH_1_ID, RESTAURANT_1_ID));
    }

    @Test
    void update() throws Exception {
        Integer updatedId = DISH13.getId();
        DishTo updatedTo = new DishTo(null, null, "updated", 150);
        Dish updated = createFromTo(updatedTo);
        updated.setId(updatedId);
        mockMvc.perform(MockMvcRequestBuilders.put(REST_TEST_URL + "/" + updatedId)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        Dish actual = dishService.get(updatedId, RESTAURANT_1_ID);
        assertEquals(DISH13.getDate(), actual.getDate());
        assertMatch(actual, updated);
    }
}
