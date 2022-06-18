package xyz.taskmesh.api.model;

import lombok.Data;

@Data
public class Tasklist {
    private String tasklistId;
    private String userId;
    private String name;
    private String[] tasks;
}
