package xyz.taskmesh.api.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import xyz.taskmesh.api.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskRepository {

    @Value("${database.tablename}")
    private String tablename;

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    public TaskRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
    }

    public Optional<Task> create(Task task) {
        var table = dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(Task.class));
        try {
            table.putItem(task);
            return Optional.of(task);
        } catch (DynamoDbException e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Task> update(Task task) {
        var table = dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(Task.class));
        try {
            table.updateItem(task);
            return Optional.of(task);
        } catch (DynamoDbException e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Task> delete(Task task) {
        var table = dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(Task.class));
        try {
            table.deleteItem(task);
            return Optional.of(task);
        } catch (DynamoDbException e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    public List<Task> findByTasklistId(String taskListId) {
        var table = dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(Task.class));

        try {
            var query = table.query(QueryConditional.sortBeginsWith(Key.builder().partitionValue(taskListId).sortValue("task_").build()));
            return query.items().stream().toList();
        } catch (DynamoDbException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }
}
