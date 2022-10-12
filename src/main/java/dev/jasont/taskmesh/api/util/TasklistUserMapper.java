package dev.jasont.taskmesh.api.util;

import java.util.List;

import dev.jasont.taskmesh.api.dto.TasklistUser;
import dev.jasont.taskmesh.api.entity.User;

public class TasklistUserMapper {
    
    public static TasklistUser userToTasklistUser(User user) {
        return new TasklistUser(user.getId());
    }

    public static List<TasklistUser> usersToTasklistUsers(List<User> users) {
        return users.stream().map(TasklistUserMapper::userToTasklistUser).toList();
    }
}
