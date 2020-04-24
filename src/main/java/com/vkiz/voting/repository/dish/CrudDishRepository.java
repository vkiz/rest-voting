package com.vkiz.voting.repository.dish;

import com.vkiz.voting.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudDishRepository extends JpaRepository<Dish, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Dish d WHERE d.id=:id AND d.restaurant.id=:restaurantId")
    int delete(@Param("id") int id, @Param("restaurantId") int restaurantId);

    @Override
    @Transactional
    Dish save(Dish item);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=:restaurantId " +
            "ORDER BY d.date DESC, d.price ASC")
    List<Dish> getAll(@Param("restaurantId") int restaurantId);

    @Query("SELECT d from Dish d WHERE d.restaurant.id=:restaurantId " +
            "AND d.date=:date ORDER BY d.price ASC")
    List<Dish> getMenuOnDate(@Param("date") LocalDate date,
                             @Param("restaurantId") int restaurantId);

    @Query("SELECT d from Dish d WHERE d.restaurant.id=:restaurantId AND d.date >= :startDate " +
            "AND d.date < :endDate ORDER BY d.date DESC, d.price ASC")
    List<Dish> getBetweenInclusive(@Param("startDate") LocalDate startDate,
                                   @Param("endDate") LocalDate endDate,
                                   @Param("restaurantId") int restaurantId);
}
