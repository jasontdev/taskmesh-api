package dev.jasont.taskmesh.api.dto;

import java.util.List;

public record NewTasklist(String name, List<TasklistUser> users, List<NewTask> tasks) {
    
}
