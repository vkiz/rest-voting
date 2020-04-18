package com.vkiz.voting;

import com.vkiz.voting.model.User;

public class AuthorizedUser extends org.springframework.security.core.userdetails.User {

    private static final long serialVersionUID = 1L;

    private User user;

    public AuthorizedUser(User user) {
        super(user.getEmail(), user.getPassword(), true, true, true, true, user.getRoles());
        this.user = user;
    }

    public Integer getId() {
        return user.getId();
    }

    public void update(User newUser) {
        user = newUser;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return user.toString();
    }
}
