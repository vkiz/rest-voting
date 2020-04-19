package com.vkiz.voting.to;

import com.vkiz.voting.HasDate;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

public class VoteTo extends BaseTo implements HasDate, Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDate date;

    @Range(min = 1)
    @NotNull
    private Integer restaurantId;

    public VoteTo() {
    }

    public VoteTo(Integer id, LocalDate date, Integer restaurantId) {
        super(id);
        this.date = date;
        this.restaurantId = restaurantId;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", restaurantId=" + restaurantId +
                '}';
    }
}
