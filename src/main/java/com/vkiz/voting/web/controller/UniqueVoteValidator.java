package com.vkiz.voting.web.controller;

import com.vkiz.voting.model.Vote;
import com.vkiz.voting.repository.vote.DataJpaVoteRepository;
import com.vkiz.voting.to.VoteTo;
import com.vkiz.voting.web.ExceptionInfoHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDate;

import static com.vkiz.voting.web.SecurityUtil.authUserId;

@Component
public class UniqueVoteValidator implements org.springframework.validation.Validator {

    private final DataJpaVoteRepository repository;

    @Autowired
    public UniqueVoteValidator(DataJpaVoteRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return VoteTo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        VoteTo voteTo = (VoteTo) target;
        Vote dbTodayVote = repository.getByDate(LocalDate.now(), authUserId());
        Integer restaurantId = voteTo.getRestaurantId();
        if (dbTodayVote != null
                && ((voteTo.getId() == null)
                || (restaurantId != null && restaurantId.equals(dbTodayVote.getRestaurant().getId())))) {
            errors.rejectValue("restaurantId", ExceptionInfoHandler.DUPLICATE_VOTE_ON_DATE_CODE);
        }
    }
}
