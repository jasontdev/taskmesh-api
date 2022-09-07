package dev.jasont.taskmesh.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Service;

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

    public Optional<Tasklist> createTasklist(OAuth2AuthenticatedPrincipal accessToken, Tasklist tasklist) {
        var newTasklist = new Tasklist();
        newTasklist.setName(tasklist.getName());

        var authenticatedUserId = Optional.ofNullable(accessToken.getAttribute("sub"));
        if(authenticatedUserId.isEmpty()) 
            return Optional.empty();

        var userIds = tasklist.getUsers().stream().map(User::getId).toList();
        var users = userRepository.findAllById(userIds); 

        if(users.size() < 1) 
            return Optional.empty(); // Tasklist must always have at least 1 user

        // check if authenticatedUserId matches any users in Tasklist
        // TODO: need more advanced security in future
        if(users.stream().anyMatch(user -> user.getId() == authenticatedUserId.get())) {
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

        return Optional.ofNullable(tasklistRepository.save(newTasklist));
    }

    public Optional<Tasklist> getById(OAuth2AuthenticatedPrincipal token, Long id) {
        var tasklist = tasklistRepository.findById(id);
        String authenticatedUserId = token.getAttribute("sub");
        
        if(tasklist.isPresent() && tasklist.get().hasUser(authenticatedUserId)) {
            return tasklist;     
        }
        return Optional.empty();
    }

    public List<Tasklist> getAllByUser(String id) {
        return tasklistRepository.findAllByUsersId(id); // all tasklists involving user
    }
}
