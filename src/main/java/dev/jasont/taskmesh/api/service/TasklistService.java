package dev.jasont.taskmesh.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.jasont.taskmesh.api.entity.Task;
import dev.jasont.taskmesh.api.entity.Tasklist;
import dev.jasont.taskmesh.api.entity.User;
import dev.jasont.taskmesh.api.repository.TaskRepository;
import dev.jasont.taskmesh.api.repository.TasklistRepository;
import dev.jasont.taskmesh.api.repository.UserRepository;
import dev.jasont.taskmesh.api.util.AuthenticatedUser;
import dev.jasont.taskmesh.api.util.UnauthourizedException;

@Service
public class TasklistService {

    private TasklistRepository tasklistRepository;
    private UserRepository userRepository;
    private TaskRepository taskRepository;

    public TasklistService(@Autowired TasklistRepository tasklistRepository,
            @Autowired UserRepository userRepository,
            @Autowired TaskRepository taskRepository) {
        this.tasklistRepository = tasklistRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public Optional<Tasklist> createTasklist(AuthenticatedUser authenticatedUser, Tasklist tasklist) throws UnauthourizedException {
        // TODO: refactor
        if(tasklist.getUsers().stream().noneMatch(user -> user.getId().equals(authenticatedUser.getId()))) {
            throw new UnauthourizedException();
        }

        var newTasklist = new Tasklist();
        newTasklist.setName(tasklist.getName());

        var tasklistUserIds = tasklist.getUsers().stream().map(User::getId).toList();
        var users = userRepository.findAllById(tasklistUserIds);

        if (users.size() < 1)
            return Optional.empty(); // Tasklist must always have at least 1 user

        // check if authenticatedUser matches any users in Tasklist
        if (users.stream().anyMatch(user -> user.getId().endsWith(authenticatedUser.getId()))) {
            newTasklist.setUsers(users); // replace users from DB
        } else {
            return Optional.empty();
        }

        // note: tasks are optional
        if (tasklist.getTasks().size() > 0) {
            var taskIds = tasklist.getTasks().stream().map(Task::getId).toList();
            var tasks = taskRepository.findAllById(taskIds);
            newTasklist.setTasks(tasks);
        }

        var updatedUsers = userRepository.saveAll(users);
        if (updatedUsers == null)
            return Optional.empty();

        return Optional.ofNullable(tasklistRepository.save(tasklist));
    }

    public Optional<Tasklist> getById(AuthenticatedUser authenticatedUser, Long id) throws UnauthourizedException {
        if (authenticatedUser.getId().equals(id)) {
            var tasklist = tasklistRepository.findById(id);
            if (tasklist.isPresent() && tasklist.get().hasUser(authenticatedUser.getId())) {
                return tasklist;
            }
            return Optional.empty();
        } else {
            throw new UnauthourizedException();
        }
    }

    public List<Tasklist> getAllByUser(AuthenticatedUser authenticatedUser, String id) throws UnauthourizedException {
        if (authenticatedUser.getId().equals(id)) {
            return tasklistRepository.findAllByUsersId(id); // all tasklists involving user
        } else {
            throw new UnauthourizedException();
        }
    }
}
