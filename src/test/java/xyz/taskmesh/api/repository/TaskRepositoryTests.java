package xyz.taskmesh.api.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import xyz.taskmesh.api.model.Task;

import java.util.UUID;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class TaskRepositoryTests {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Value("${database.tablename}")
    private String tablename;

    @BeforeEach
    public void init() {
        dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(Task.class)).createTable();
    }

    @AfterEach
    public void clean() {
        dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(Task.class)).deleteTable();
    }

    @Test
    public void isInjectible() {
        Assertions.assertNotNull(taskRepository);
    }

    @Test
    public void createTask() {
        var tasklistId = "tasklist_" + UUID.randomUUID();
        var taskId = "task_" + UUID.randomUUID();

        var task = new Task(tasklistId, taskId, "Testing task 1");
        taskRepository.create(task);

        var savedTask = dynamoDbEnhancedClient
                .table(tablename, TableSchema.fromBean(Task.class))
                .getItem(Key.builder().partitionValue(tasklistId).sortValue(taskId).build());

        Assertions.assertEquals(task, savedTask);
    }
}
