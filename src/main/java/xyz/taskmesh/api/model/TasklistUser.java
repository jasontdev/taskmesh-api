package xyz.taskmesh.api.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class TasklistUser {
    private String tasklistId;
    private String userId;

    @DynamoDbPartitionKey
    public String getTasklistId() {
        return tasklistId;
    }
    public void setTasklistId(String tasklistId) {
        this.tasklistId = tasklistId;
    }

    @DynamoDbSortKey
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
