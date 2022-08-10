package dev.jasont.taskmesh.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    private String id;

    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_tasklist",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tasklist_id"))
    @JsonIgnoreProperties("users")
    private List<Tasklist> tasklists = new ArrayList<>();

    public User() {
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
}
