package com.sujit.crm.service;

import com.sujit.crm.entity.User;

import java.util.List;

public interface UserService {
    /**
     * @param email user email
     * @return User object
     */
    User findByEmail(String email);

    /**Saves given User object
     *
     * @param user User object to be saved
     */
    void saveUser(User user);

    /**Returns a list of all users
     *
     * @return List of all users
     */
    List<User> findAll();

    /**Returns list of Users with the give user as their supervisor
     *
     * @param user User object
     * @return List of users managed by the given user as their supervisor
     */
    List<User> findBySupervisor(User user);

    /**Returns max value of contract that user can accept from all his roles
     *
     * @param user User object
     * @return max value of contract that user can accept
     */
    Double getMaxContractValue(User user);
}
