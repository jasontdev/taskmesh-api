package xyz.taskmesh.api.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import xyz.taskmesh.api.model.Metadata;
import xyz.taskmesh.api.model.Task;
import xyz.taskmesh.api.model.Tasklist;
import xyz.taskmesh.api.model.User;

import java.util.ArrayList;

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
}
