package com.example.multitenancy.tenant.service;

import com.example.multitenancy.tenant.model.User;
import com.example.multitenancy.tenant.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author Xutingzhou
 */
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(long id) {
        return userRepository.findById(id).orElse(new User().setName("AAAAAAAa"));
    }

    @Transactional
    @Override
    public User addUser(String name) {
        return userRepository.save(new User().setName(name));
    }
}
