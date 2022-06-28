package xyz.taskmesh.api.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class Metadata {

    private String tasklistId;
    private final String type = "metadata";
    private String title;

    @DynamoDbSortKey
    @DynamoDbAttribute("sk")
    public String getMetadataId() {
        return "metadata";
    }

    public void setMetadataId(String metadataId) {
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("pk")
    public String getTasklistId() {
        return tasklistId;
    }

    public void setTasklistId(String tasklistId) {
        this.tasklistId = tasklistId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
