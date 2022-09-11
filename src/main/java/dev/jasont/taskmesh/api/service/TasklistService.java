package dev.jasont.taskmesh.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.azure.core.implementation.Option;

import dev.jasont.taskmesh.api.entity.AuthenticatedUser;
import dev.jasont.taskmesh.api.entity.Task;
import dev.jasont.taskmesh.api.entity.Tasklist;
import dev.jasont.taskmesh.api.entity.User;
import dev.jasont.taskmesh.api.repository.TaskRepository;
import dev.jasont.taskmesh.api.repository.TasklistRepository;
import dev.jasont.taskmesh.api.repository.UserRepository;

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

    public Optional<Tasklist> createTasklist(AuthenticatedUser authenticatedUser, Tasklist tasklist) {
        var newTasklist = new Tasklist();
        newTasklist.setName(tasklist.getName());

        var tasklistUserIds = tasklist.getUsers().stream().map(User::getId).toList();
        var users = userRepository.findAllById(tasklistUserIds); 

        if(users.size() < 1) 
            return Optional.empty(); // Tasklist must always have at least 1 user

        // check if authenticatedUser matches any users in Tasklist
        // TODO: this match fails when it should succeed
        if(users.stream().anyMatch(user -> user.getId() == authenticatedUser.getId())) {
            newTasklist.setUsers(users); // replace users from DB
        } else {
            return Optional.empty();
        }

        // note: tasks are optional
        if(tasklist.getTasks().size() > 0) {
            var taskIds = tasklist.getTasks().stream().map(Task::getId).toList(); 
            var tasks = taskRepository.findAllById(taskIds);
            newTasklist.setTasks(tasks);
        }

        var updatedUsers = userRepository.saveAll(users);
        if(updatedUsers == null)
            return Optional.empty();

        return Optional.ofNullable(tasklistRepository.save(tasklist));
    }

    public Optional<Tasklist> getById(AuthenticatedUser authenticatedUser, Long id) {
        var tasklist = tasklistRepository.findById(id);
        if(tasklist.isPresent() && tasklist.get().hasUser(authenticatedUser.getId())) {
            return tasklist;     
        }
        return Optional.empty();
    }

    public List<Tasklist> getAllByUser(AuthenticatedUser authenticatedUser, String id) {
        if(authenticatedUser.getId() != id);
        return tasklistRepository.findAllByUsersId(id); // all tasklists involving user
    }
}
