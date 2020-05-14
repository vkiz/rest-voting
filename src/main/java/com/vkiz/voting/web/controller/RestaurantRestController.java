package com.vkiz.voting.web.controller;

import com.vkiz.voting.model.Dish;
import com.vkiz.voting.model.Restaurant;
import com.vkiz.voting.service.DishService;
import com.vkiz.voting.service.RestaurantService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = RestaurantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantRestController {

    static final String REST_URL = "/restaurants";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final RestaurantService restaurantService;

    private final DishService dishService;

    @Autowired
    public RestaurantRestController(RestaurantService restaurantService, DishService dishService) {
        this.restaurantService = restaurantService;
        this.dishService = dishService;
    }

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("getAll restaurants");
        return restaurantService.getAll();
    }

    @GetMapping("/menus")
    public List<Restaurant> getAllTodayMenus() {
        log.info("getAllTodayMenus");
        return restaurantService.getAllWithTodayMenu();
    }

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        log.info("get restaurant {}", id);
        return restaurantService.get(id);
    }

    @GetMapping("/{id}/dishes")
    public List<Dish> getAllDishes(@PathVariable int id) {
        log.info("getAllDishes of restaurant {}", id);
        return dishService.getAll(id);
    }

    @GetMapping("/{id}/dishes/{dishId}")
    public Dish getDish(@PathVariable int id, @PathVariable int dishId) {
        log.info("get dish {} of restaurant {}", dishId, id);
        return dishService.get(dishId, id);
    }
}
