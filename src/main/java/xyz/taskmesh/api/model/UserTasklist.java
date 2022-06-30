package xyz.taskmesh.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTasklist {

    private String userId;
    private String tasklistId;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("pk")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("sk")
    public String getTasklistId() {
        return tasklistId;
    }

    public void setTasklistId(String tasklistId) {
        this.tasklistId = tasklistId;
    }
}
