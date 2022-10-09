package dev.jasont.taskmesh.api.util;

import java.util.ArrayList;

import dev.jasont.taskmesh.api.dto.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        var expectedTasklist = new StoredTasklist(1L, "Testing task list", expectedTasklistUsers , new ArrayList<StoredTask>());
        var actualTasklist = TasklistMapper.tasklistToDTO(tasklist);

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
        var actualTasklist = TasklistMapper.tasklistToDTO(tasklist);

        Assertions.assertEquals(expectedTasklist, actualTasklist);
    }

    @Test
    public void fromNewTasklistEmpty() {
        var user = new User("user_id");
        var users = new ArrayList<User>();
        users.add(user);

        var tasklistUsers = new ArrayList<TasklistUser>();
        tasklistUsers.add(new TasklistUser("user_id"));
        var newTasklist = new NewTasklist("Test tasklist", tasklistUsers, new ArrayList<>());

        var tasklist = TasklistMapper.fromNewTasklist(newTasklist, users);
        Assertions.assertEquals("user_id", tasklist.getUsers().get(0).getId());
        Assertions.assertEquals(user, tasklist.getUsers().get(0));
        Assertions.assertEquals(tasklist, user.getTasklists().get(0));
        Assertions.assertEquals(0, tasklist.getTasks().size());
        Assertions.assertEquals(1, tasklist.getUsers().size());
        Assertions.assertEquals(1, user.getTasklists().size());
    }

    @Test
    public void fromNewTasklistWithTasks() {
        var user = new User("user_id");
        var users = new ArrayList<User>();
        users.add(user);

        var tasklistUsers = new ArrayList<TasklistUser>();
        tasklistUsers.add(new TasklistUser("user_id"));

        var taskOne = new NewTask("Test task one", null);
        var taskTwo = new NewTask("Test task two", null);
        var tasks = new ArrayList<NewTask>();
        tasks.add(taskOne);
        tasks.add(taskTwo);
        var newTasklist = new NewTasklist("Test tasklist", tasklistUsers, tasks);

        var tasklist = TasklistMapper.fromNewTasklist(newTasklist, users);
        Assertions.assertEquals("user_id", tasklist.getUsers().get(0).getId());
        Assertions.assertEquals(user, tasklist.getUsers().get(0));
        Assertions.assertEquals(tasklist, user.getTasklists().get(0));
        Assertions.assertEquals(1, tasklist.getUsers().size());
        Assertions.assertEquals(2, tasklist.getTasks().size());
        Assertions.assertEquals(tasklist, tasklist.getTasks().get(0).getTasklist());
        Assertions.assertEquals(tasklist, tasklist.getTasks().get(1).getTasklist());
        Assertions.assertEquals(1, user.getTasklists().size());
    }
}
