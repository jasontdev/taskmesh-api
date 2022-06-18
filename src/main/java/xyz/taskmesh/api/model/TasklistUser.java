package xyz.taskmesh.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class TasklistUser {
    private String tasklistId;
    private String userId;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("pk")
    public String getTasklistId() {
        return tasklistId;
    }
    public void setTasklistId(String tasklistId) {
        this.tasklistId = tasklistId;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("sk")
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
