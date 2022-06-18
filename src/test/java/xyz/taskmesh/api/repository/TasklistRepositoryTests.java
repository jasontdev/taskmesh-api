package xyz.taskmesh.api.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import xyz.taskmesh.api.model.TasklistUser;

import java.util.UUID;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class TaskRepositoryTests {

    @Autowired
    TaskRepository taskRepository;

    @Test
    void taskRepositoryIsInjectible() {
        Assertions.assertNotNull(taskRepository);
    }

    void saveTasklist() {
        var userId = UUID.randomUUID().toString();
        var tasklistId = UUID.randomUUID().toString();

        var tasklistUser = new TasklistUser();
        tasklistUser.setUserId(userId);
        tasklistUser.setTasklistId(tasklistId);

               

        taskRepository.save()
    }
}
