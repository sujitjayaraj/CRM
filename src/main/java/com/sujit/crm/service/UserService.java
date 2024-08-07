package com.sujit.crm.service;

import com.sujit.crm.entity.User;

import java.util.List;

public interface UserService {

    /**
     * Returns User with given email
     *
     * @param email the email of the user
     * @return User {@link com.sujit.crm.entity.User} with given email.
     */
    User findByEmail(String email);

    /**
     * Saves given {@link com.sujit.crm.entity.User} to database.
     *
     * @param user the {@link com.sujit.crm.entity.User} to be saved.
     */
    void saveUser(User user);

    /**
     * Returns a {@link java.util.List} of all {@link com.sujit.crm.entity.User} objects.
     *
     * @return a {@link java.util.List} of all {@link com.sujit.crm.entity.User} objects.
     */
    List<User> findAll();

    /**
     * Returns a {@link java.util.List} of all {@link com.sujit.crm.entity.User} objects with given supervisor.
     *
     * @param user the supervisor {@link com.sujit.crm.entity.User}.
     * @return a {@link java.util.List} of all {@link com.sujit.crm.entity.User} objects with given supervisor.
     */
    List<User> findBySupervisor(User user);

    /**
     * Returns maximum value of contract that a given {@link com.sujit.crm.entity.User} can accept from all his roles.
     *
     * @param user the {@link com.sujit.crm.entity.User} for whom the maximum contract value is to be calculated.
     * @return maximum value of contract that a given {@link com.sujit.crm.entity.User} can accept from all his roles.
     */
    Double getMaxContractValue(User user);
}
