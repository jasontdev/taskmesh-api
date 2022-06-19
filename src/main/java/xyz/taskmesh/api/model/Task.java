package xyz.taskmesh.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private String tasklistId;
    private String taskId;
    private String name;
    private Boolean isComplete;

    public Task(String tasklistId, String taskId, String name) {
        this.tasklistId = tasklistId;
        this.taskId = taskId;
        this.name = name;
    }

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
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getComplete() {
        return isComplete;
    }

    public void setComplete(Boolean complete) {
        isComplete = complete;
    }
}
