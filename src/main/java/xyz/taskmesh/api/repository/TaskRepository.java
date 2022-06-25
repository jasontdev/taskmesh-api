package xyz.taskmesh.api.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import xyz.taskmesh.api.model.Task;

import java.util.List;

@Repository
public class TaskRepository extends DynamoDbRepository<Task> {

    public TaskRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient,
                          @Value("${database.tablename}") String tablename) {
        super(dynamoDbEnhancedClient, tablename, Task.class);
    }

    public List<Task> findAllByTasklistId(String taskListId) {
        return super.findAllByKey(Key.builder().partitionValue(taskListId).sortValue("task_").build());
    }
}
