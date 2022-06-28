package xyz.taskmesh.api.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.internal.operations.BatchGetItemOperation;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import xyz.taskmesh.api.model.Metadata;
import xyz.taskmesh.api.model.Task;
import xyz.taskmesh.api.model.Tasklist;
import xyz.taskmesh.api.model.User;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public class TasklistRepository {

    final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    final String tablename;

    public TasklistRepository(@Autowired DynamoDbEnhancedClient dynamoDbEnhancedClient, @Value("${database.tablename}") String tablename) {
        this.tablename = tablename;
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
    }

    public void save(Tasklist tasklist) {
        // create metadata object
        var metadata = new Metadata();
        metadata.setTasklistId(tasklist.getTasklistId());
        metadata.setTitle(tasklist.getTitle());
        var metadataTable = dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(Metadata.class));
        var userTable =
                dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(User.class));
        var writeBatches = new ArrayList<WriteBatch>();

        writeBatches.add(WriteBatch.builder(Metadata.class)
                .mappedTableResource(metadataTable)
                .addPutItem(PutItemEnhancedRequest.builder(Metadata.class).item(metadata).build()).build());

        if (tasklist.getUsers().size() > 0) {
            tasklist.getUsers().forEach(user -> writeBatches.add(
                    WriteBatch.builder(User.class).mappedTableResource(userTable).addPutItem(user).build()));
        }

        if (tasklist.getTasks().size() > 0) {
            var taskTable = dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(Task.class));
            tasklist.getTasks().forEach(
                    task -> writeBatches.add(WriteBatch.builder(Task.class).mappedTableResource(taskTable).addPutItem(task).build())
            );
        }

        var batchWriteResult =
                dynamoDbEnhancedClient.batchWriteItem(BatchWriteItemEnhancedRequest.builder().writeBatches(writeBatches).build());
    }

    Optional<Tasklist> findById(String tasklistId) {
        var metadataTable = dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(Metadata.class));
        var metadata = metadataTable.getItem(Key.builder().partitionValue(tasklistId).sortValue("metadata").build());
        if(metadata == null)
            return Optional.empty();

        var tasklist = new Tasklist();
        tasklist.setTasklistId(metadata.getTasklistId());
        tasklist.setTitle(metadata.getTitle());

        var userTable = dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(User.class));
        var users = userTable
                .query(QueryConditional.sortBeginsWith(Key.builder().partitionValue(tasklistId).sortValue("user_").build()))
                .items().stream().toList();

        if(users.stream().count() > 0)
            tasklist.setUsers(users);

        var taskTable = dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(Task.class));
        var tasks = taskTable
                .query(QueryConditional.sortBeginsWith(Key.builder().partitionValue(tasklistId).sortValue("task_").build()))
                .items().stream().toList();

        if(tasks.stream().count() > 0)
            tasklist.setTasks(tasks);

        return Optional.of(tasklist);
    }
}
