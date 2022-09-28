package dev.jasont.taskmesh.api.util;

import java.util.List;

import dev.jasont.taskmesh.api.dto.StoredTask;
import dev.jasont.taskmesh.api.dto.StoredTasklist;
import dev.jasont.taskmesh.api.dto.TasklistUser;
import dev.jasont.taskmesh.api.entity.Tasklist;

public class TasklistMapper {
    
    public static StoredTasklist fromTasklist(Tasklist tasklist) {
        List<StoredTask> tasks = null;
        if(tasklist.hasTasks()) {
            tasks = TaskMapper.fromTasks(tasklist.getTasks());
        }

        // Tasklist should always have users
        List<TasklistUser> users = TasklistUserMapper.fromUsers(tasklist.getUsers());

        return new StoredTasklist(tasklist.getId(), tasklist.getName(), users, tasks);
    }
}
