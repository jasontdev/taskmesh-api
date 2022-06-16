package xyz.taskmesh.api.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import xyz.taskmesh.api.model.Task;

import java.util.UUID;

@Service
public class TaskRepository {

    @Value("$(dynamodb.tablename)")
    private String tablename;

    @Autowired
    DynamoDbEnhancedClient dynamoDbEnhancedClient;

    public Task save(Task task) {
        // should check to make sure task has a tasklistId
        task.setTaskId(UUID.randomUUID().toString());
        dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(Task.class)).putItem(task);

        return task;
    }
}
