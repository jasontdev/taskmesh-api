package dev.jasont.taskmesh.api.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Tasklist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @OneToMany(cascade = { CascadeType.ALL })
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

    public boolean hasUser(String userId) {
        return users.stream().anyMatch(user -> user.getId() == userId);
    }
}
