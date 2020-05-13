package com.vkiz.voting.web.controller;

import com.vkiz.voting.model.Dish;
import com.vkiz.voting.service.DishService;
import com.vkiz.voting.to.DishTo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static com.vkiz.voting.util.ValidationUtil.assureIdConsistent;
import static com.vkiz.voting.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminDishRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDishRestController {

    static final String REST_URL = "/admin/restaurants";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final DishService dishService;

    @Autowired
    public AdminDishRestController(DishService dishService) {
        this.dishService = dishService;
    }

    @PostMapping(value = "/{restaurantId}/dishes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody DishTo dishTo, @PathVariable int restaurantId) {
        log.info("create {} for restaurant with id={}", dishTo, restaurantId);
        checkNew(dishTo);
        Dish created = dishService.create(dishTo, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/" + restaurantId + "/dishes" + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{restaurantId}/dishes/{dishId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restaurantId, @PathVariable int dishId) {
        log.info("delete dish {} of restaurant with id={}", dishId, restaurantId);
        dishService.delete(dishId, restaurantId);
    }

    @PutMapping(value = "/{restaurantId}/dishes/{dishId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody DishTo dishTo,
                       @PathVariable int restaurantId,
                       @PathVariable int dishId) {
        assureIdConsistent(dishTo, dishId);
        log.info("update {} with id={} of restaurant with id={}", dishTo, dishId, restaurantId);
        dishService.update(dishTo, restaurantId);
    }
}
