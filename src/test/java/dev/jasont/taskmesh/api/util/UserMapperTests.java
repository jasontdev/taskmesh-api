package dev.jasont.taskmesh.api.util;

import dev.jasont.taskmesh.api.entity.Tasklist;
import dev.jasont.taskmesh.api.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserMapperTests {

    @Test
    public void fromEntityWithNoTasklists() {
        var user = new User();
        user.setId("user_id");

        var userDto = UserMapper.fromUser(user);
        Assertions.assertEquals("user_id", userDto.id());
    }

    @Test
    public void fromEntitityWithTasklist() {
        var user = new User();
        user.setId("user_id");

        var tasklist = new Tasklist("Test tasklist");
        user.addTasklist(tasklist);

        var userDto = UserMapper.fromUser(user);

        Assertions.assertEquals("user_id", userDto.id());
        Assertions.assertEquals(1, userDto.tasklists().size());
    }

    @Test
    public void fromEntitityWithTasklists() {
        var user = new User();
        user.setId("user_id");

        var tasklistOne = new Tasklist("Test tasklist one");
        user.addTasklist(tasklistOne);

        var tasklistTwo = new Tasklist("Test tasklist two");
        user.addTasklist(tasklistTwo);

        var userDto = UserMapper.fromUser(user);

        Assertions.assertEquals("user_id", userDto.id());
        Assertions.assertEquals(2, userDto.tasklists().size());
    }
}
