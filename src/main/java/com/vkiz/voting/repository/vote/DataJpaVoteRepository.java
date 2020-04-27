package com.vkiz.voting.repository.vote;

import com.vkiz.voting.model.Vote;
import com.vkiz.voting.repository.restaurant.CrudRestaurantRepository;
import com.vkiz.voting.repository.user.CrudUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DataJpaVoteRepository {

    private final CrudVoteRepository crudVoteRepository;
    private final CrudUserRepository crudUserRepository;
    private final CrudRestaurantRepository crudRestaurantRepository;

    @Autowired
    public DataJpaVoteRepository(CrudVoteRepository crudVoteRepository,
                                 CrudUserRepository crudUserRepository,
                                 CrudRestaurantRepository crudRestaurantRepository) {
        this.crudVoteRepository = crudVoteRepository;
        this.crudUserRepository = crudUserRepository;
        this.crudRestaurantRepository = crudRestaurantRepository;
    }

    @Transactional
    public Vote save(Vote vote, int userId, int restaurantId) {
        if (!vote.isNew() && get(vote.getId(), userId) == null) {
            return null;
        }
        vote.setUser(crudUserRepository.getOne(userId));
        vote.setRestaurant(crudRestaurantRepository.findById(restaurantId).orElse(null));
        return crudVoteRepository.save(vote);
    }

    public boolean delete(int id, int userId) {
        return crudVoteRepository.delete(id, userId) != 0;
    }

    public Vote get(int id, int userId) {
        return crudVoteRepository
                .findById(id)
                .filter(vote -> vote.getUser().getId() == userId)
                .orElse(null);
    }

    public List<Vote> getAll(int userId) {
        return crudVoteRepository.getAll(userId);
    }

    public Vote getByDate(LocalDate date, int userId) {
        return crudVoteRepository.getByDate(date, userId);
    }
}
