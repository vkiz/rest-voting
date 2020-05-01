package com.vkiz.voting.service;

import com.vkiz.voting.model.Restaurant;
import com.vkiz.voting.repository.restaurant.DataJpaRestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import static com.vkiz.voting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RestaurantService {

    private final DataJpaRestaurantRepository repository;

    @Autowired
    public RestaurantService(DataJpaRestaurantRepository repository) {
        this.repository = repository;
    }

    @CacheEvict(value = {"restaurants", "allRestaurantsWithTodayMenu"}, allEntries = true)
    public Restaurant create(Restaurant restaurant) {
        return save(restaurant);
    }

    @CacheEvict(value = {"restaurants", "allRestaurantsWithTodayMenu"}, allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id), id);
    }

    public Restaurant get(int id) {
        return checkNotFoundWithId(repository.get(id), id);
    }

    @Cacheable("restaurants")
    public List<Restaurant> getAll() {
        return repository.getAll();
    }

    @CacheEvict(value = {"restaurants", "allRestaurantsWithTodayMenu"}, allEntries = true)
    public void update(Restaurant restaurant) {
        save(restaurant);
    }

    @Cacheable("allRestaurantsWithTodayMenu")
    public List<Restaurant> getAllWithTodayMenu() {
        return repository.getAllWithDishesOnDate(LocalDate.now());
    }

    private Restaurant save(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        return checkNotFoundWithId(repository.save(restaurant), restaurant.getId());
    }
}
