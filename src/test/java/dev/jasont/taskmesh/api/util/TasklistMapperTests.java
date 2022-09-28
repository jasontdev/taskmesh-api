package dev.jasont.taskmesh.api.util;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import dev.jasont.taskmesh.api.dto.StoredTask;
import dev.jasont.taskmesh.api.dto.StoredTasklist;
import dev.jasont.taskmesh.api.dto.TasklistUser;
import dev.jasont.taskmesh.api.entity.Task;
import dev.jasont.taskmesh.api.entity.Tasklist;
import dev.jasont.taskmesh.api.entity.User;

public class TasklistMapperTests {

    @Test
    public void emptyTasklist() {
        var user = new User("user_id");
        var tasklist = new Tasklist("Testing task list");
        tasklist.setId(1L);
        user.addTasklist(tasklist);

        var expectedTasklistUsers = new ArrayList<TasklistUser>();
        expectedTasklistUsers.add(new TasklistUser("user_id"));
        var expectedTasklist = new StoredTasklist(1L, "Testing task list", expectedTasklistUsers , null);
        var actualTasklist = TasklistMapper.fromTasklist(tasklist);

        Assertions.assertEquals(expectedTasklist, actualTasklist);
    }

    @Test
    public void hasTasks() {
        var user = new User("user_id");
        var tasklist = new Tasklist("Testing task list");
        tasklist.setId(1L);
        user.addTasklist(tasklist);
        var testTask = new Task("Testing task");
        testTask.setId(1L);
        tasklist.addTask(testTask);


        var expectedTasklistUsers = new ArrayList<TasklistUser>();
        expectedTasklistUsers.add(new TasklistUser("user_id"));

        var expectedTasks = new ArrayList<StoredTask>();
        expectedTasks.add(new StoredTask(1L, 1L, "Testing task", false));

        var expectedTasklist = new StoredTasklist(1L, "Testing task list", expectedTasklistUsers , expectedTasks);
        var actualTasklist = TasklistMapper.fromTasklist(tasklist);

        Assertions.assertEquals(expectedTasklist, actualTasklist);
    }
}
