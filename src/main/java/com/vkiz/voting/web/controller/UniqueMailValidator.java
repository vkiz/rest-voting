package com.vkiz.voting.web.controller;

import com.vkiz.voting.model.User;
import com.vkiz.voting.repository.user.DataJpaUserRepository;
import com.vkiz.voting.web.ExceptionInfoHandler;
import com.vkiz.voting.web.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import static com.vkiz.voting.web.SecurityUtil.authUserId;

@Component
public class UniqueMailValidator implements org.springframework.validation.Validator {

    private final DataJpaUserRepository repository;

    @Autowired
    public UniqueMailValidator(DataJpaUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (SecurityUtil.safeGet() != null) {
            user.setId(authUserId());
        }
        User dbUser = repository.getByEmail(user.getEmail().toLowerCase());
        if (dbUser != null && dbUser.getId() != null && !dbUser.getId().equals(user.getId())) {
            errors.rejectValue("email", ExceptionInfoHandler.DUPLICATE_USER_EMAIL_CODE);
        }
    }
}
