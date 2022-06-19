package xyz.taskmesh.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class Tasklist {
    private String tasklistId;
    private String userId;
    private String name;
    private ArrayList<String> tasks;

    public Tasklist(String tasklistId, String userId, String name) {
        this.tasklistId = tasklistId;
        this.userId = userId;
        this.name = name;
        this.tasks = new ArrayList<>();
    }
}
