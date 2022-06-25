package xyz.taskmesh.api.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import xyz.taskmesh.api.model.TasklistUser;

import java.util.UUID;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class TasklistUserRepositoryTests extends RepositoryTests<TasklistUser> {

    private final TasklistUserRepository tasklistUserRepository;

    public TasklistUserRepositoryTests(@Autowired TasklistUserRepository tasklistUserRepository,
                                       @Autowired DynamoDbEnhancedClient dynamoDbEnhancedClient,
                                       @Value("${database.tablename}") String tablename) {
        super(dynamoDbEnhancedClient, tablename, TasklistUser.class);
        this.tasklistUserRepository = tasklistUserRepository;
    }

    @Test
    public void isInjectible() {
        Assertions.assertNotNull(tasklistUserRepository);
    }

    @Test
    public void saveTasklistUser() {
        var tasklistId = "tasklist_" + UUID.randomUUID();
        var userId = "user_" + UUID.randomUUID();
        var tasklistUser = new TasklistUser(tasklistId, userId);

        tasklistUserRepository.save(tasklistUser);
        Assertions.assertEquals(tasklistUser.getTasklistId(), table.getItem(tasklistUser).getTasklistId());
    }

    @Test
    public void findByTasklistId() {
        var tasklistId = "tasklist_" + UUID.randomUUID();
        var userIdOne = "user_" + UUID.randomUUID();
        var tasklistUserOne = new TasklistUser(tasklistId, userIdOne);
        var userIdTwo = "user_" + UUID.randomUUID();
        var tasklistUserTwo = new TasklistUser(tasklistId, userIdTwo);

        table.putItem(tasklistUserOne);
        table.putItem(tasklistUserTwo);

        var tasklistUsers = tasklistUserRepository.findAllByTasklistId(tasklistId);
        Assertions.assertTrue(tasklistUsers.stream()
                .allMatch(item -> item.getUserId().equals(userIdOne) | item.getUserId().equals(userIdTwo)));
    }
}
