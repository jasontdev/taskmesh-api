package dev.jasont.taskmesh.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.jasont.taskmesh.api.entity.Tasklist;
import dev.jasont.taskmesh.api.entity.TasklistInput;
import dev.jasont.taskmesh.api.repository.TasklistRepository;
import dev.jasont.taskmesh.api.repository.UserRepository;
import dev.jasont.taskmesh.api.util.AuthenticatedUser;
import dev.jasont.taskmesh.api.util.UnauthourizedException;

@Service
public class TasklistService {

    private TasklistRepository tasklistRepository;
    private UserRepository userRepository;

    public TasklistService(@Autowired TasklistRepository tasklistRepository,
            @Autowired UserRepository userRepository) {
        this.tasklistRepository = tasklistRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Optional<Tasklist> createTasklist(AuthenticatedUser authenticatedUser,
            TasklistInput tasklistInput) throws UnauthourizedException {

        // TODO: make sure all users are contacts of authenticatedUser
        if (!tasklistInput.getUserIds().stream().anyMatch(userId -> authenticatedUser.getId().equals(userId)))
            throw new UnauthourizedException();

        var users = userRepository.findAllById(tasklistInput.getUserIds());
        if (users.size() < 0) {
            // TODO: throw an exception so controller knows why we are returning empty
            return Optional.empty();
        }

        // prepare new Tasklist
        var tasklist = new Tasklist(tasklistInput.getName());
        if (tasklistInput.getTasks() != null && tasklistInput.getTasks().size() > 0) {
            // create list of tasks attache to tasklist
            var tasks = tasklistInput.getTasks().stream().map(task -> {
                task.setTasklist(tasklist);
                return task;
            }).toList();
            tasklist.setTasks(tasks);
        }

        // attach each user to the tasklist
        users = users.stream().map(user -> user.addTasklist(tasklist)).toList();

        return Optional.ofNullable(tasklistRepository.save(tasklist));
    }

    public Optional<Tasklist> getById(AuthenticatedUser authenticatedUser, Long id) throws UnauthourizedException {
        var tasklist = tasklistRepository.findById(id);
        if (tasklist.isPresent() && tasklist.get().hasUser(authenticatedUser.getId())) {
            return tasklist;
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
