package dev.jasont.taskmesh.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Service;

import dev.jasont.taskmesh.api.entity.User;
import dev.jasont.taskmesh.api.repository.UserRepository;
import dev.jasont.taskmesh.api.util.UnauthorizedException;

@Service
public class UserService {
    
    private UserRepository userRepository;

    public UserService(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> createUser(OAuth2AuthenticatedPrincipal token, User user) throws UnauthorizedException {
        if(token.getAttribute("sub") != user.getId())
           throw new UnauthorizedException("user.id does not match token subject");

        return Optional.ofNullable(userRepository.save(user));
    }

    public Optional<User> getUser(String id) {
        return userRepository.findById(id);
    }
}
