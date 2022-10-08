package dev.jasont.taskmesh.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "taskmesh_user") // user is often reserved name in databases
public class User {

    @Id
    private String id;

    @ManyToMany(mappedBy = "users", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("users")
    private List<Tasklist> tasklists = new ArrayList<>();

    public User() {
    }

    public User(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Tasklist> getTasklists() {
        return tasklists;
    }

    public void setTasklists(List<Tasklist> tasklists) {
        this.tasklists = tasklists;
    }

    public User addTasklist(Tasklist tasklist) {
        tasklist.addUser(this);
        tasklists.add(tasklist);

        return this;
    }

    public boolean hasTasklists() {
        return tasklists != null && tasklists.size() > 0;
    }
}
