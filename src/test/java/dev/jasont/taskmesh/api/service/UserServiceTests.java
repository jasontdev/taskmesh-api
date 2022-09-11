package dev.jasont.taskmesh.api.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import dev.jasont.taskmesh.api.entity.AuthenticatedUser;
import dev.jasont.taskmesh.api.entity.Task;
import dev.jasont.taskmesh.api.entity.Tasklist;
import dev.jasont.taskmesh.api.entity.User;
import dev.jasont.taskmesh.api.repository.TasklistRepository;
import dev.jasont.taskmesh.api.repository.UserRepository;
import dev.jasont.taskmesh.api.util.UnauthourizedException;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("dev")
public class UserServiceTests {

    private UserService userService;
    private UserRepository userRepository;
    private TasklistRepository tasklistRepository;

    public UserServiceTests(@Autowired UserService userService, @Autowired UserRepository userRepository,
            @Autowired TasklistRepository tasklistRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.tasklistRepository = tasklistRepository;
    }

    @BeforeEach
    public void clearDatabase() {
        userRepository.deleteAll();
    }

    @Test
    public void isInjectible() {
        Assertions.assertNotNull(userService);
    }

    @Test
    public void createUser() throws UnauthourizedException {
        var user = new User();
        var userId = "user_id";
        user.setId(userId);
        var authUser = new AuthenticatedUser(userId);

        var newUser = userService.createUser(authUser, user);
        Assertions.assertTrue(newUser.isPresent());
        Assertions.assertEquals(userId, newUser.get().getId());
    }

    @Test
    public void getUserThatExists() throws UnauthourizedException {
        var user = new User();
        var userId = "user_id";
        user.setId(userId);
        var authUser = new AuthenticatedUser(userId);

        var task = new Task();
        task.setName("Test task 1");
        var tasklist = new Tasklist();
        tasklist.setName("Test tasklist");
        tasklist.addUser(user);
        var newUser = userRepository.save(user);
        tasklist = newUser.getTasklists().get(0);
        tasklist.addTask(task);
        tasklistRepository.save(tasklist);

        var savedUser = userService.getUser(authUser, userId);
        Assertions.assertTrue(savedUser.isPresent());
        Assertions.assertEquals(userId, savedUser.get().getId());
    }

    @Test
    public void getUserThatDoesNotExist() throws UnauthourizedException {
        var userId = "user_id";
        var authUser = new AuthenticatedUser(userId);
        var savedUser = userService.getUser(authUser, userId);
        Assertions.assertFalse(savedUser.isPresent());
    }

    @Test
    public void getUserNotAuthorized() {
        var user = new User();
        var userId = "user_id";
        var authUser = new AuthenticatedUser( "wrong_user_id" );
        user.setId(userId);
        userRepository.save(user);

        Assertions.assertThrows(UnauthourizedException.class,
                () -> userService.getUser(authUser, userId));
    }
}
