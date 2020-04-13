package com.vkiz.voting.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.voting.View;
import org.hibernate.annotations.BatchSize;
import org.springframework.util.CollectionUtils;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email", name = "users_unique_email_idx")})
public class User extends AbstractBaseEntity {

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotBlank
    @Size(max = 100)
    private String email;

    @Column(name = "password", nullable = false)
    @NotBlank
    @Size(min = 5, max = 100)
    @JsonView(View.JsonPassword.class)
    private String password;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    @BatchSize(size = 200)
    private Set<Role> roles;

    protected User() {
    }

    public User(User u) {
        this(u.getId(), u.getEmail(), u.getPassword(), u.getRoles());
    }

    public User(Integer id, String email, String password, Role role, Role... roles) {
        this(id, email, password, EnumSet.of(role, roles));
    }

    public User(Integer id, String email, String password, Collection<Role> roles) {
        super(id);
        this.email = email.toLowerCase();
        this.password = password;
        setRoles(roles);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public String getPassword() {
        return password;
    }

    public User setRoles(Collection<Role> roles) {
        this.roles = CollectionUtils.isEmpty(roles) ? EnumSet.noneOf(Role.class) : EnumSet.copyOf(roles);
        return this;
    }

    public User addRole(Role... roles) {
        if (CollectionUtils.isEmpty(this.roles)) {
            this.roles = EnumSet.noneOf(Role.class);
        }
        this.roles.addAll(List.of(roles));
        return this;
    }

    public User removeRole(Role role) {
        roles.remove(role);
        return this;
    }

    public User clearRoles() {
        roles.clear();
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email=" + email +
                ", roles=" + roles +
                '}';
    }
}
