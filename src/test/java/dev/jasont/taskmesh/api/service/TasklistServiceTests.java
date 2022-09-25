package dev.jasont.taskmesh.api.service;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import dev.jasont.taskmesh.api.entity.Task;
import dev.jasont.taskmesh.api.entity.TasklistInput;
import dev.jasont.taskmesh.api.entity.User;
import dev.jasont.taskmesh.api.repository.TasklistRepository;
import dev.jasont.taskmesh.api.repository.UserRepository;
import dev.jasont.taskmesh.api.util.AuthenticatedUser;
import dev.jasont.taskmesh.api.util.UnauthourizedException;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class TasklistServiceTests {

    private TasklistService tasklistService;
    private UserRepository userRepository;
    private TasklistRepository tasklistRepository;

    public TasklistServiceTests(@Autowired TasklistService tasklistService, @Autowired UserRepository userRepository,
            @Autowired TasklistRepository tasklistRepository) {
        this.tasklistRepository = tasklistRepository;
        this.tasklistService = tasklistService;
        this.userRepository = userRepository;
    }

    @BeforeEach
    public void setup() {
        tasklistRepository.deleteAll();
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
        var tasklistInput = new TasklistInput("Test tasklist");
        tasklistInput.getUserIds().add("user");

        var savedTasklist = tasklistService.createTasklist(authenticatedUser, tasklistInput);
        Assertions.assertNotNull(savedTasklist);
        Assertions.assertEquals("user", savedTasklist.get().getUsers().get(0).getId());
    }

    @Test
    public void createTasklistWithTasks() throws UnauthourizedException {
        var authenticatedUser = new AuthenticatedUser("user");
        var tasks = new ArrayList<Task>();
        tasks.add( new Task("First task") );
        tasks.add( new Task("Second task") );
        tasks.add( new Task("Third task") );

        var tasklistInput = new TasklistInput("Test tasklist");
        tasklistInput.setTasks(tasks);
        tasklistInput.getUserIds().add("user");

        var savedTasklist = tasklistService.createTasklist(authenticatedUser, tasklistInput);
        Assertions.assertNotNull(savedTasklist);
        Assertions.assertEquals("user", savedTasklist.get().getUsers().get(0).getId());
        Assertions.assertEquals(3, savedTasklist.get().getTasks().size());
    }
}
