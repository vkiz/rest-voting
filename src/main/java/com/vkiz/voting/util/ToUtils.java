package com.vkiz.voting.util;

import com.vkiz.voting.model.Dish;
import com.vkiz.voting.model.Vote;
import com.vkiz.voting.to.DishTo;
import com.vkiz.voting.to.VoteTo;

public class ToUtils {

    public static Dish createFromTo(DishTo dishTo) {
        return new Dish(dishTo.getId(), dishTo.getDate(), dishTo.getName(), dishTo.getPrice());
    }

    public static Vote createFromTo(VoteTo voteTo) {
        return new Vote(voteTo.getId(), voteTo.getDate(), null, null);
    }
}
