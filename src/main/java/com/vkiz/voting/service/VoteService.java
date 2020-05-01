package com.vkiz.voting.service;

import com.vkiz.voting.model.Vote;
import com.vkiz.voting.repository.vote.DataJpaVoteRepository;
import com.vkiz.voting.to.VoteTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

import static com.vkiz.voting.util.ToUtils.createFromTo;
import static com.vkiz.voting.util.ValidationUtil.checkDateAndIsVoteChangeTimeExpired;
import static com.vkiz.voting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VoteService {

    private final DataJpaVoteRepository repository;

    @Autowired
    public VoteService(DataJpaVoteRepository repository) {
        this.repository = repository;
    }

    public Vote create(VoteTo voteTo, int userId) {
        return create(createFromTo(voteTo), userId, voteTo.getRestaurantId());
    }

    public Vote create(Vote vote, int userId, int restaurantId) {
        return save(checkDateAndIsVoteChangeTimeExpired(vote, false), userId, restaurantId);
    }

    public void delete(int id, int userId) {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public Vote get(int id, int userId) {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    public List<Vote> getAll(int userId) {
        return repository.getAll(userId);
    }

    public void update(VoteTo voteTo, int userId) {
        update(createFromTo(voteTo), userId, voteTo.getRestaurantId());
    }

    public void update(Vote vote, int userId, int restaurantId) {
        save(checkDateAndIsVoteChangeTimeExpired(vote, true), userId, restaurantId);
    }

    private Vote save(Vote vote, int userId, int restaurantId) {
        Assert.notNull(vote, "vote must not be null");
        return checkNotFoundWithId(repository.save(vote, userId, restaurantId), vote.getId());
    }
}
