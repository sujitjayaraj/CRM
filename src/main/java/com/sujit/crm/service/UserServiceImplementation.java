package com.sujit.crm.service;

import com.sujit.crm.entity.Role;
import com.sujit.crm.entity.User;
import com.sujit.crm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserServiceImplementation implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    public UserServiceImplementation(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    @Override
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        userRepository.save(user);
    }
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
    @Override
    public List<User> findBySupervisor(User user) {
        return userRepository.findBySupervisor(user);
    }
    @Override
    public Double getMaxContractValue(User user) {
        Set<Role> roles = user.getRoles();
        Double maxContractValue = 0.0;
        for(Role role : roles){
            if(role.getMaxContractValue() > maxContractValue){
                maxContractValue = role.getMaxContractValue();
            }
        }
        return maxContractValue;
    }
}