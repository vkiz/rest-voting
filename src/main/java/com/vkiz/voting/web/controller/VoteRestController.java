package com.vkiz.voting.web.controller;

import com.vkiz.voting.model.Vote;
import com.vkiz.voting.service.VoteService;
import com.vkiz.voting.to.VoteTo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
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
import java.util.List;

import static com.vkiz.voting.util.ValidationUtil.assureIdConsistent;
import static com.vkiz.voting.util.ValidationUtil.checkNew;
import static com.vkiz.voting.web.SecurityUtil.authUserId;

@RestController
@RequestMapping(value = VoteRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteRestController {

    static final String REST_URL = "/votes";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final VoteService service;

    private final UniqueVoteValidator voteValidator;

    @Autowired
    public VoteRestController(VoteService service, UniqueVoteValidator voteValidator) {
        this.service = service;
        this.voteValidator = voteValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(voteValidator);
    }

    @GetMapping
    public List<Vote> getAll() {
        log.info("getAll votes for user with id={}", authUserId());
        return service.getAll(authUserId());
    }

    @GetMapping("/{id}")
    public Vote get(@PathVariable int id) {
        log.info("get vote with id={}", id);
        return service.get(id, authUserId());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createWithLocation(@Valid @RequestBody VoteTo voteTo) {
        log.info("create vote for restaurant with id={}", voteTo.getRestaurantId());
        checkNew(voteTo);
        Vote created = service.create(voteTo, authUserId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody VoteTo voteTo, @PathVariable int id) {
        log.info("update vote with id={} in favour of restaurant with id={}", voteTo.getId(), voteTo.getRestaurantId());
        assureIdConsistent(voteTo, id);
        service.update(voteTo, authUserId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete vote with id={}", id);
        service.delete(id, authUserId());
    }
}
