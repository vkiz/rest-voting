package com.vkiz.voting.web.controller;

import com.vkiz.voting.DishTestData;
import com.vkiz.voting.service.RestaurantService;
import com.vkiz.voting.web.AbstractControllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.vkiz.voting.DishTestData.DISH01;
import static com.vkiz.voting.RestaurantTestData.RESTAURANT_1;
import static com.vkiz.voting.RestaurantTestData.RESTAURANT_1_ID;
import static com.vkiz.voting.RestaurantTestData.contentJson;
import static com.vkiz.voting.TestUtil.userHttpBasic;
import static com.vkiz.voting.UserTestData.USER;
import static com.vkiz.voting.web.controller.RestaurantRestController.REST_URL;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantRestControllerTest extends AbstractControllerTest {

    @Autowired
    RestaurantService restaurantService;

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "/" + RESTAURANT_1_ID)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(RESTAURANT_1));
    }

    @Test
    void getDish() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "/" + RESTAURANT_1_ID + "/dishes/" + DISH01.getId())
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DishTestData.contentJson(DISH01));
    }
}
