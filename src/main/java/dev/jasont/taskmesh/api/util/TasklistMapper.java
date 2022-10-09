package dev.jasont.taskmesh.api.util;

import java.util.ArrayList;
import java.util.List;

import dev.jasont.taskmesh.api.dto.NewTasklist;
import dev.jasont.taskmesh.api.dto.StoredTask;
import dev.jasont.taskmesh.api.dto.StoredTasklist;
import dev.jasont.taskmesh.api.dto.TasklistUser;
import dev.jasont.taskmesh.api.entity.Task;
import dev.jasont.taskmesh.api.entity.Tasklist;
import dev.jasont.taskmesh.api.entity.User;

public class TasklistMapper {

    public static StoredTasklist tasklistToDTO(Tasklist tasklist) {
        ArrayList<StoredTask> tasks = new ArrayList<>();
        if (tasklist.hasTasks()) {
            tasks.addAll(TaskMapper.fromTasks(tasklist.getTasks()));
        }

        // Tasklist should always have users
        List<TasklistUser> users = TasklistUserMapper.fromUsers(tasklist.getUsers());

        return new StoredTasklist(tasklist.getId(), tasklist.getName(), users, tasks);
    }

    public static List<StoredTasklist> tasklistsToDTO(List<Tasklist> tasklists) {
        return tasklists.stream().map(TasklistMapper::tasklistToDTO).toList();
    }

    public static Tasklist fromNewTasklist(NewTasklist newTasklist, List<User> users) {
        var tasklist = new Tasklist(newTasklist.name());
        var tasks = new ArrayList<Task>(TaskMapper.fromNewTasks(newTasklist.tasks(), tasklist));

        tasklist.setTasks(tasks);
        users.forEach(user -> user.addTasklist(tasklist));
        return tasklist;
    }

    public static List<Tasklist> fromNewTasklists(List<NewTasklist> newTasklists, List<User> users) {
        return newTasklists.stream().map(newTasklist -> TasklistMapper.fromNewTasklist(newTasklist, users)).toList();
    }
}
