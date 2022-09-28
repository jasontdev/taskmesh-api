package dev.jasont.taskmesh.api.util;

import java.util.List;

import dev.jasont.taskmesh.api.dto.TasklistUser;
import dev.jasont.taskmesh.api.entity.User;

public class TasklistUserMapper {
    
    public static TasklistUser fromUser(User user) {
        return new TasklistUser(user.getId());
    }

    public static List<TasklistUser> fromUsers(List<User> users) {
        return users.stream().map(TasklistUserMapper::fromUser).toList(); 
    }
}
