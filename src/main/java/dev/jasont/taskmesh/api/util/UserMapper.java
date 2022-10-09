package dev.jasont.taskmesh.api.util;

import dev.jasont.taskmesh.api.dto.NewUser;
import dev.jasont.taskmesh.api.dto.StoredTasklist;
import dev.jasont.taskmesh.api.dto.StoredUser;
import dev.jasont.taskmesh.api.entity.User;

import java.util.ArrayList;

public class UserMapper {
    public static StoredUser fromUserToDTO(User user) {
        ArrayList<StoredTasklist> tasklists = user.hasTasklists() ?
                new ArrayList<>(TasklistMapper.tasklistsToDTO(user.getTasklists())) : null;

        return new StoredUser(user.getId(), tasklists);
    }

    public static User fromNewUser(NewUser newUser) {
        var user = new User();
        user.setId(newUser.id());

        return user;
    }
}
