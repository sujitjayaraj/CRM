package com.sujit.crm.service;

import com.sujit.crm.entity.Role;
import com.sujit.crm.entity.User;
import com.sujit.crm.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
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
        double max = 0.00;

        for (Role role: roles) {
            if (max < role.getMaxContractValue()) {
                max = role.getMaxContractValue();
            }
        }

        return max;
    }
}
