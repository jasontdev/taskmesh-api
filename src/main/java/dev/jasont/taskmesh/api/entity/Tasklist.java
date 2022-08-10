package dev.jasont.taskmesh.api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import dev.jasont.taskmesh.api.entity.Task;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Tasklist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @OneToMany
    @JsonIgnoreProperties("tasklist")
    private List<Task> tasks = new ArrayList<>();

    @ManyToMany(mappedBy = "tasklists")
    @JsonIgnoreProperties("tasklists")
    private List<User> users = new ArrayList<>();

    public Tasklist() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        users.add(user);
        var userTasklists = user.getTasklists();
        userTasklists.add(this);
    }

    public void addTask(Task task) {
        tasks.add(task);
        task.setTasklist(this);
    }
}
