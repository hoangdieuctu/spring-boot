package com.hoangdieuctu.boot.elasticsearch.service;

import com.hoangdieuctu.boot.elasticsearch.model.User;
import com.hoangdieuctu.boot.elasticsearch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.getAll();
    }

    public List<User> getByName(String name) {
        return userRepository.getByName(name);
    }

    public User update(String id, String name) {
        User user = userRepository.get(id);
        if (user == null) {
            throw new NullPointerException("User not found");
        }

        user.setName(name);
        return userRepository.update(id, user);
    }

    public void delete(String id) {
        userRepository.delete(id);
    }
}
