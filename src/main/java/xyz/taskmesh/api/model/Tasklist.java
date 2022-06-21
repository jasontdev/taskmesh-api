package xyz.taskmesh.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tasklist {
    private String tasklistId;
    private String userId;
    private String name;
}
