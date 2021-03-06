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
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
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

    @Test
    public void findTasksByTasklistId() {
        var tasklistId = "tasklist_" + UUID.randomUUID();
        var taskIdOne = "task_" + UUID.randomUUID();
        var taskIdTwo = "task_" + UUID.randomUUID();

        var taskOne = new Task(tasklistId, taskIdOne, "Testing task 1");
        var taskTwo = new Task(tasklistId, taskIdTwo, "Testing task 2");

        taskRepository.create(taskOne);
        taskRepository.create(taskTwo);

        var savedTasks = dynamoDbEnhancedClient
                .table(tablename, TableSchema.fromBean(Task.class))
                .query(QueryConditional.sortBeginsWith(Key.builder().partitionValue(tasklistId).sortValue("task_").build()))
                .items().stream().toList();

        Assertions.assertTrue(savedTasks.stream().allMatch(task -> task.equals(taskOne) || task.equals(taskTwo)));
        Assertions.assertEquals(2, (long) savedTasks.size());
    }

    @Test
    public void updateTask() {
        var tasklistId = "tasklist_" + UUID.randomUUID();
        var taskId = "task_" + UUID.randomUUID();
        var task = new Task(tasklistId, taskId, "This task needs to be updated");

        var table = dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(Task.class));
        table.putItem(task);

        task.setName("Task has been updated");
        taskRepository.update(task);

        var updatedItem = table.getItem(Key.builder().partitionValue(tasklistId).sortValue(taskId).build());
        Assertions.assertEquals("Task has been updated", updatedItem.getName());
    }

    @Test
    public void deleteTask() {
        var tasklistId = "tasklist_" + UUID.randomUUID();
        var taskId = "task_" + UUID.randomUUID();
        var task = new Task(tasklistId, taskId, "This task needs to be deleted");

        var table = dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(Task.class));
        table.putItem(task);

        taskRepository.delete(task);
        var deletedItem = table.getItem(task);
        Assertions.assertNull(deletedItem);
    }

    @Test
    public void deleteTaskWrongId() {
        var tasklistId = "tasklist_" + UUID.randomUUID();
        var taskId = "task_" + UUID.randomUUID();
        var task = new Task(tasklistId, taskId, "Task does not exist in table");

        var table = dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(Task.class));
        Assertions.assertDoesNotThrow(() -> table.deleteItem(task));
    }
}