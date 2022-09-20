package dev.jasont.taskmesh.api.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import dev.jasont.taskmesh.api.entity.Tasklist;
import dev.jasont.taskmesh.api.entity.User;
import dev.jasont.taskmesh.api.repository.UserRepository;
import dev.jasont.taskmesh.api.util.AuthenticatedUser;
import dev.jasont.taskmesh.api.util.UnauthourizedException;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class TasklistServiceTests {

    private TasklistService tasklistService;
    private UserRepository userRepository;

    public TasklistServiceTests(@Autowired TasklistService tasklistService, @Autowired UserRepository userRepository) {
        this.tasklistService = tasklistService;
        this.userRepository = userRepository;
    }

    @BeforeEach
    public void setup() {
        userRepository.deleteAll(); // clean-up from previous tests

        var user = new User();
        user.setId("user");
        userRepository.save(user);
    }

    @Test
    public void isInjectible() {
        Assertions.assertNotNull(tasklistService, "Failed to inject tasklistService");
    }

    @Test
    public void createEmptyTasklist() throws UnauthourizedException {
        var authenticatedUser = new AuthenticatedUser("user");
        var tasklist = new Tasklist("Test tasklist");

        var user = userRepository.findById("user");
        Assertions.assertTrue(user.isPresent(), "Test user not found");

        tasklist.addUser(user.get());
        var savedTasklist = tasklistService.createTasklist(authenticatedUser, tasklist);
        Assertions.assertTrue(savedTasklist.isPresent(), "Failed to retreive tasklist");
    }
}
