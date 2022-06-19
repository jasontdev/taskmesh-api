package xyz.taskmesh.api.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import xyz.taskmesh.api.model.Task;

import java.util.Optional;

@Service
public class TaskRepository {

    @Value("${database.tablename}")
    private String tablename;

    @Autowired
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;

    public Optional<Task> create(Task newTasklist) {
        var table = dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(Task.class));
        try {
            table.putItem(newTasklist);
            return Optional.of(newTasklist);
        } catch (DynamoDbException e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }
}
