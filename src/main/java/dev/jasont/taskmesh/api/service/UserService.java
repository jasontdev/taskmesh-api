package dev.jasont.taskmesh.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.jasont.taskmesh.api.entity.User;
import dev.jasont.taskmesh.api.repository.UserRepository;
import dev.jasont.taskmesh.api.util.UnauthourizedException;

@Service
public class UserService {
    
    private UserRepository userRepository;

    public UserService(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> createUser(String requestingUserId, User user) throws UnauthourizedException {
        if(requestingUserId != user.getId())
            throw new UnauthourizedException();
        return Optional.ofNullable(userRepository.save(user));
    }

    public Optional<User> getUser(String requestingUserId, String id) throws UnauthourizedException {
        if(requestingUserId != id)
            throw new UnauthourizedException();

        return userRepository.findById(id);
    }
}