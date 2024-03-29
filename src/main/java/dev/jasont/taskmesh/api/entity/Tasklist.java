package dev.jasont.taskmesh.api.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Tasklist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("tasklist")
    private List<Task> tasks = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "user_tasklist",
            joinColumns = @JoinColumn(name = "tasklist"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonIgnoreProperties("tasklists")
    private List<User> users = new ArrayList<>();

    public Tasklist() {
    }

    public Tasklist(String name) {
        this.name = name;
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
    }

    public void addTask(Task task) {
        tasks.add(task);
        task.setTasklist(this);
    }

    public boolean hasUser(String userId) {
        return users.stream().anyMatch(user -> user.getId().equals(userId));
    }

    public boolean hasTasks() {
        return this.tasks != null && tasks.size() > 0;
    }
}
