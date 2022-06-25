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
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import xyz.taskmesh.api.model.Task;

import java.util.UUID;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class TaskRepositoryTests extends RepositoryTests<Task> {

    private final TaskRepository taskRepository;

    public TaskRepositoryTests(@Autowired TaskRepository taskRepository,
                               @Autowired DynamoDbEnhancedClient dynamoDbEnhancedClient,
                               @Value("${database.tablename}") String tablename) {
        super(dynamoDbEnhancedClient, tablename, Task.class);
        this.taskRepository = taskRepository;
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
        taskRepository.save(task);

        var savedTask = table.getItem(Key.builder().partitionValue(tasklistId).sortValue(taskId).build());

        Assertions.assertEquals(task, savedTask);
    }

    @Test
    public void findTasksByTasklistId() {
        var tasklistId = "tasklist_" + UUID.randomUUID();
        var taskIdOne = "task_" + UUID.randomUUID();
        var taskIdTwo = "task_" + UUID.randomUUID();

        var taskOne = new Task(tasklistId, taskIdOne, "Testing task 1");
        var taskTwo = new Task(tasklistId, taskIdTwo, "Testing task 2");

        taskRepository.save(taskOne);
        taskRepository.save(taskTwo);

        var savedTasks =
                table.query(QueryConditional.sortBeginsWith(Key.builder().partitionValue(tasklistId).sortValue("task_").build()))
                        .items().stream().toList();

        Assertions.assertTrue(savedTasks.stream().allMatch(task -> task.equals(taskOne) || task.equals(taskTwo)));
        Assertions.assertEquals(2, (long) savedTasks.size());
    }

    @Test
    public void updateTask() {
        var tasklistId = "tasklist_" + UUID.randomUUID();
        var taskId = "task_" + UUID.randomUUID();
        var task = new Task(tasklistId, taskId, "This task needs to be updated");

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
        Assertions.assertDoesNotThrow(() -> table.deleteItem(task));
    }
}