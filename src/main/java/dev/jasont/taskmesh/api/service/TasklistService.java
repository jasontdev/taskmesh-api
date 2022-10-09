package dev.jasont.taskmesh.api.service;

import java.util.List;
import java.util.Optional;

import dev.jasont.taskmesh.api.dto.NewTask;
import dev.jasont.taskmesh.api.dto.NewTasklist;
import dev.jasont.taskmesh.api.dto.NewUser;
import dev.jasont.taskmesh.api.dto.TasklistUser;
import dev.jasont.taskmesh.api.entity.Task;
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

    private final TasklistRepository tasklistRepository;
    private final UserRepository userRepository;

    public TasklistService(@Autowired TasklistRepository tasklistRepository,
            @Autowired UserRepository userRepository) {
        this.tasklistRepository = tasklistRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Optional<Tasklist> createTasklist(AuthenticatedUser authenticatedUser,
            NewTasklist newTasklist) throws UnauthourizedException {

        // TODO: make sure all users are contacts of authenticatedUser
        if (newTasklist.users().stream().noneMatch(user -> authenticatedUser.getId().equals(user.userId())))
            throw new UnauthourizedException();

        var userIds = newTasklist.users().stream().map(TasklistUser::userId).toList();
        var users = userRepository.findAllById(userIds);
        if (users.size() < 1)
            return Optional.empty();

        // prepare new Tasklist
        var tasklist = new Tasklist(newTasklist.name());
        if (newTasklist.tasks().size() > 0) {
            // create list of tasks attache to tasklist
            var tasks = newTasklist.tasks().stream().map(newTask -> {
                var task = new Task(newTask.name());
                task.setTasklist(tasklist);
                return task;
            }).toList();

            tasklist.setTasks(tasks);
        }

        // attach each user to the tasklist
        users.forEach(user -> user.addTasklist(tasklist));

        return Optional.of(tasklistRepository.save(tasklist));
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
