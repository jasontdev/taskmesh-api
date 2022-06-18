package xyz.taskmesh.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Tasklist {
    private String tasklistId;
    private String userId;
    private String name;
    private String[] tasks;

    public Tasklist(String tasklistId, String userId, String name) {
        this.tasklistId = tasklistId;
        this.userId = userId;
        this.name = name;
    }
}
